/*
 * Copyright (c) 2013, Daniel Murphy All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.pirckheimer_gymnasium.jbox2d.testbed.framework;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import de.pirckheimer_gymnasium.jbox2d.common.IViewportTransform;
import de.pirckheimer_gymnasium.jbox2d.common.Vec2;
import de.pirckheimer_gymnasium.jbox2d.dynamics.World;
import de.pirckheimer_gymnasium.jbox2d.serialization.SerializationResult;
import de.pirckheimer_gymnasium.jbox2d.serialization.UnsupportedObjectException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * This class contains most control logic for the testbed and the update loop.
 * It also watches the model to switch tests and populates the model with some
 * loop statistics.
 *
 * @author Daniel Murphy
 */
public abstract class AbstractTestbedController
{
    private static final Logger log = LoggerFactory
            .getLogger(AbstractTestbedController.class);

    public enum UpdateBehavior
    {
        UPDATE_CALLED, UPDATE_IGNORED
    }

    public enum MouseBehavior
    {
        NORMAL, FORCE_Y_FLIP
    }

    public static final int DEFAULT_FPS = 60;

    private TestbedTest currTest = null;

    private TestbedTest nextTest = null;

    private long frameCount;

    private int targetFrameRate;

    private float frameRate = 0;

    private boolean animating = false;

    private final TestbedModel model;

    private boolean savePending, loadPending, resetPending = false;

    private final UpdateBehavior updateBehavior;

    private final MouseBehavior mouseBehavior;

    private final LinkedList<QueueItem> inputQueue;

    private final TestbedErrorHandler errorHandler;

    private float viewportHalfHeight;

    private float viewportHalfWidth;

    public AbstractTestbedController(TestbedModel argModel,
            UpdateBehavior behavior, MouseBehavior mouseBehavior,
            TestbedErrorHandler errorHandler)
    {
        model = argModel;
        inputQueue = Lists.newLinkedList();
        setFrameRate(DEFAULT_FPS);
        updateBehavior = behavior;
        this.errorHandler = errorHandler;
        this.mouseBehavior = mouseBehavior;
        addListeners();
    }

    private void addListeners()
    {
        // time for our controlling
        model.addTestChangeListener((test, index) -> {
            model.getPanel().grabFocus();
            nextTest = test;
        });
    }

    public void load()
    {
        loadPending = true;
    }

    public void save()
    {
        savePending = true;
    }

    public void reset()
    {
        resetPending = true;
    }

    public void queueLaunchBomb()
    {
        synchronized (inputQueue)
        {
            inputQueue.add(new QueueItem());
        }
    }

    public void queuePause()
    {
        synchronized (inputQueue)
        {
            inputQueue.add(new QueueItem(QueueItemType.Pause));
        }
    }

    public void queueMouseUp(Vec2 screenPos, int button)
    {
        synchronized (inputQueue)
        {
            inputQueue.add(
                    new QueueItem(QueueItemType.MouseUp, screenPos, button));
        }
    }

    public void queueMouseDown(Vec2 screenPos, int button)
    {
        synchronized (inputQueue)
        {
            inputQueue.add(
                    new QueueItem(QueueItemType.MouseDown, screenPos, button));
        }
    }

    public void queueMouseMove(Vec2 screenPos)
    {
        synchronized (inputQueue)
        {
            inputQueue
                    .add(new QueueItem(QueueItemType.MouseMove, screenPos, 0));
        }
    }

    public void queueMouseDrag(Vec2 screenPos, int button)
    {
        synchronized (inputQueue)
        {
            inputQueue.add(
                    new QueueItem(QueueItemType.MouseDrag, screenPos, button));
        }
    }

    public void queueKeyPressed(char c, int code)
    {
        synchronized (inputQueue)
        {
            inputQueue.add(new QueueItem(QueueItemType.KeyPressed, c, code));
        }
    }

    public void queueKeyReleased(char c, int code)
    {
        synchronized (inputQueue)
        {
            inputQueue.add(new QueueItem(QueueItemType.KeyReleased, c, code));
        }
    }

    public void updateExtents(float halfWidth, float halfHeight)
    {
        viewportHalfHeight = halfHeight;
        viewportHalfWidth = halfWidth;
        if (currTest != null)
        {
            currTest.getCamera().getTransform().setExtents(halfWidth,
                    halfHeight);
        }
    }

    protected void loopInit()
    {
        model.getPanel().grabFocus();
        if (currTest != null)
        {
            currTest.init(model);
        }
    }

    private void initTest(TestbedTest test)
    {
        test.init(model);
        test.getCamera().getTransform().setExtents(viewportHalfWidth,
                viewportHalfHeight);
        model.getPanel().grabFocus();
    }

    /**
     * Called by the main run loop. If the update behavior is set to
     * {@link UpdateBehavior#UPDATE_IGNORED}, then this needs to be called
     * manually to update the input and test.
     */
    public void updateTest()
    {
        if (resetPending)
        {
            if (currTest != null)
            {
                currTest.init(model);
            }
            resetPending = false;
            model.getPanel().grabFocus();
        }
        if (savePending)
        {
            if (currTest != null)
            {
                _save();
            }
            savePending = false;
            model.getPanel().grabFocus();
        }
        if (loadPending)
        {
            if (currTest != null)
            {
                _load();
            }
            loadPending = false;
            model.getPanel().grabFocus();
        }
        if (currTest == null)
        {
            synchronized (inputQueue)
            {
                inputQueue.clear();
                return;
            }
        }
        IViewportTransform transform = currTest.getCamera().getTransform();
        // process our input
        while (!inputQueue.isEmpty())
        {
            QueueItem i = null;
            synchronized (inputQueue)
            {
                if (!inputQueue.isEmpty())
                {
                    i = inputQueue.pop();
                }
            }
            if (i == null)
            {
                continue;
            }
            boolean oldFlip = transform.isYFlip();
            if (mouseBehavior == MouseBehavior.FORCE_Y_FLIP)
            {
                transform.setYFlip(true);
            }
            currTest.getCamera().getTransform().getScreenToWorld(i.p, i.p);
            if (mouseBehavior == MouseBehavior.FORCE_Y_FLIP)
            {
                transform.setYFlip(oldFlip);
            }
            switch (i.type)
            {
            case KeyPressed:
                if (i.c != KeyEvent.CHAR_UNDEFINED)
                {
                    model.getKeys()[i.c] = true;
                }
                model.getCodedKeys()[i.code] = true;
                currTest.keyPressed(i.c, i.code);
                break;

            case KeyReleased:
                if (i.c != KeyEvent.CHAR_UNDEFINED)
                {
                    model.getKeys()[i.c] = false;
                }
                model.getCodedKeys()[i.code] = false;
                currTest.keyReleased(i.c, i.code);
                break;

            case MouseDown:
                currTest.mouseDown(i.p, i.button);
                break;

            case MouseMove:
                currTest.mouseMove(i.p);
                break;

            case MouseUp:
                currTest.mouseUp(i.p, i.button);
                break;

            case MouseDrag:
                currTest.mouseDrag(i.p, i.button);
                break;

            case LaunchBomb:
                currTest.launchBomb();
                break;

            case Pause:
                model.getSettings().pause = !model.getSettings().pause;
                break;
            }
        }
        if (currTest != null)
        {
            currTest.step(model.getSettings());
        }
    }

    public void nextTest()
    {
        int index = model.getCurrTestIndex() + 1;
        index %= model.getTestsSize();
        while (!model.isTestAt(index) && index < model.getTestsSize() - 1)
        {
            index++;
        }
        if (model.isTestAt(index))
        {
            model.setCurrTestIndex(index);
        }
    }

    public void lastTest()
    {
        int index = model.getCurrTestIndex() - 1;
        while (index >= 0 && !model.isTestAt(index))
        {
            if (index == 0)
            {
                index = model.getTestsSize() - 1;
            }
            else
            {
                index--;
            }
        }
        if (model.isTestAt(index))
        {
            model.setCurrTestIndex(index);
        }
    }

    public void playTest(int argIndex)
    {
        if (argIndex == -1)
        {
            return;
        }
        while (!model.isTestAt(argIndex))
        {
            if (argIndex + 1 < model.getTestsSize())
            {
                argIndex++;
            }
            else
            {
                return;
            }
        }
        model.setCurrTestIndex(argIndex);
    }

    public void setFrameRate(int fps)
    {
        if (fps <= 0)
        {
            throw new IllegalArgumentException(
                    "Fps cannot be less than or equal to zero");
        }
        targetFrameRate = fps;
        frameRate = fps;
    }

    public int getFrameRate()
    {
        return targetFrameRate;
    }

    public float getCalculatedFrameRate()
    {
        return frameRate;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public long getFrameCount()
    {
        return frameCount;
    }

    public boolean isAnimating()
    {
        return animating;
    }

    public synchronized void start()
    {
        if (!isAnimating())
        {
            startAnimator();
        }
        else
        {
            log.warn("Animation is already animating.");
        }
    }

    public synchronized void stop()
    {
        animating = false;
        stopAnimator();
    }

    public void startAnimator()
    {
        animating = true;
    }

    public void stopAnimator()
    {
        animating = false;
    }

    protected long startTime, beforeTime, afterTime, updateTime, timeDiff,
            sleepTime, timeSpent;

    protected void stepAndRender()
    {
        float timeInSecs;
        if (nextTest != null)
        {
            initTest(nextTest);
            model.setRunningTest(nextTest);
            if (currTest != null)
            {
                currTest.exit();
            }
            currTest = nextTest;
            nextTest = null;
        }
        timeSpent = beforeTime - updateTime;
        if (timeSpent > 0)
        {
            timeInSecs = timeSpent * 1.0f / 1000000000.0f;
            updateTime = System.nanoTime();
            frameRate = (frameRate * 0.9f) + (1.0f / timeInSecs) * 0.1f;
            model.setCalculatedFps(frameRate);
        }
        else
        {
            updateTime = System.nanoTime();
        }
        render(model.getPanel());
        frameCount++;
        afterTime = System.nanoTime();
        timeDiff = afterTime - beforeTime;
        sleepTime = (1000000000 / targetFrameRate - timeDiff) / 1000000;
        beforeTime = System.nanoTime();
    }

    protected void render(TestbedPanel panel)
    {
        if (panel.render())
        {
            if (currTest != null
                    && updateBehavior == UpdateBehavior.UPDATE_CALLED)
            {
                updateTest();
            }
            panel.paintScreen();
        }
    }

    private void _save()
    {
        SerializationResult result;
        try
        {
            result = currTest.getSerializer().serialize(currTest.getWorld());
        }
        catch (UnsupportedObjectException e1)
        {
            log.error("Error serializing world", e1);
            if (errorHandler != null)
                errorHandler.serializationError(e1,
                        "Error serializing the object: " + e1);
            return;
        }
        try
        {
            FileOutputStream fos = new FileOutputStream(currTest.getFilename());
            result.writeTo(fos);
            fos.flush();
            fos.close();
        }
        catch (FileNotFoundException e)
        {
            log.error("File not found exception while saving", e);
            if (errorHandler != null)
                errorHandler.serializationError(e,
                        "File not found exception while saving: "
                                + currTest.getFilename());
        }
        catch (IOException e)
        {
            log.error("Exception while writing world", e);
            if (errorHandler != null)
                errorHandler.serializationError(e,
                        "Error while writing world: " + e);
        }
        log.debug("Serialized world to {}", currTest.getFilename());
    }

    private void _load()
    {
        World w;
        try
        {
            FileInputStream fis = new FileInputStream(currTest.getFilename());
            w = currTest.getDeserializer().deserializeWorld(fis);
            fis.close();
        }
        catch (FileNotFoundException e)
        {
            log.error("File not found error while loading", e);
            if (errorHandler != null)
                errorHandler.serializationError(e,
                        "File not found exception while loading: "
                                + currTest.getFilename());
            return;
        }
        catch (UnsupportedObjectException e)
        {
            log.error("Error deserializing object", e);
            if (errorHandler != null)
                errorHandler.serializationError(e,
                        "Error deserializing the object: " + e);
            return;
        }
        catch (IOException e)
        {
            log.error("Exception while reading world", e);
            if (errorHandler != null)
                errorHandler.serializationError(e,
                        "Error while reading world: " + e);
            return;
        }
        log.debug("Deserialized world from {}", currTest.getFilename());
        currTest.init(w, true);
    }
}

enum QueueItemType
{
    MouseDown, MouseMove, MouseUp, MouseDrag, KeyPressed, KeyReleased,
    LaunchBomb, Pause
}

class QueueItem
{
    public QueueItemType type;

    public Vec2 p = new Vec2();

    public char c;

    public int button;

    public int code;

    public QueueItem()
    {
        type = QueueItemType.LaunchBomb;
    }

    public QueueItem(QueueItemType t)
    {
        type = t;
    }

    public QueueItem(QueueItemType t, Vec2 pt, int button)
    {
        type = t;
        p.set(pt);
        this.button = button;
    }

    public QueueItem(QueueItemType t, char cr, int cd)
    {
        type = t;
        c = cr;
        code = cd;
    }
}
