/*
 * Copyright (c) 2013, Daniel Murphy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright notice,
 * 	  this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright notice,
 * 	  this list of conditions and the following disclaimer in the documentation
 * 	  and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.pirckheimer_gymnasium.jbox2d.testbed.framework.j2d;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

import de.pirckheimer_gymnasium.jbox2d.testbed.framework.TestbedCamera.ZoomType;
import de.pirckheimer_gymnasium.jbox2d.common.Vec2;
import de.pirckheimer_gymnasium.jbox2d.testbed.framework.AbstractTestbedController;
import de.pirckheimer_gymnasium.jbox2d.testbed.framework.TestbedModel;
import de.pirckheimer_gymnasium.jbox2d.testbed.framework.TestbedTest;

import com.google.common.collect.Lists;

public class AWTPanelHelper
{
    static boolean screenDragButtonDown = false;

    static boolean mouseJointButtonDown = false;

    /**
     * Adds common help text and listeners for awt-based testbeds.
     */
    public static void addHelpAndPanelListeners(Component panel,
            final TestbedModel model,
            final AbstractTestbedController controller,
            final int screenDragButton)
    {
        final Vec2 oldDragMouse = new Vec2();
        final Vec2 mouse = new Vec2();
        List<String> help = Lists.newArrayList();
        help.add("Click and drag the left mouse button to move objects.");
        help.add("Click and drag the right mouse button to move the view.");
        help.add("Shift-Click to aim a bullet, or press space.");
        help.add("Scroll to zoom in/out on the mouse position");
        help.add("Press '[' or ']' to change tests, and 'r' to restart.");
        model.setImplSpecificHelp(help);
        panel.addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            TestbedTest currTest = model.getCurrTest();
            if (currTest == null)
            {
                return;
            }
            ZoomType zoom = notches < 0 ? ZoomType.ZOOM_IN : ZoomType.ZOOM_OUT;
            currTest.getCamera().zoomToPoint(mouse, zoom);
        });
        panel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent arg0)
            {
                if (arg0.getButton() == screenDragButton)
                {
                    screenDragButtonDown = false;
                }
                else if (model.getCodedKeys()[KeyEvent.VK_SHIFT]
                        && !mouseJointButtonDown)
                {
                    controller.queueMouseUp(new Vec2(arg0.getX(), arg0.getY()),
                            TestbedTest.BOMB_SPAWN_BUTTON);
                }
                else
                {
                    if (arg0.getButton() == TestbedTest.MOUSE_JOINT_BUTTON)
                    {
                        mouseJointButtonDown = false;
                    }
                    controller.queueMouseUp(new Vec2(arg0.getX(), arg0.getY()),
                            arg0.getButton());
                }
            }

            @Override
            public void mousePressed(MouseEvent arg0)
            {
                if (arg0.getButton() == screenDragButton)
                {
                    screenDragButtonDown = true;
                    oldDragMouse.set(arg0.getX(), arg0.getY());
                }
                else if (model.getCodedKeys()[KeyEvent.VK_SHIFT])
                {
                    controller.queueMouseDown(
                            new Vec2(arg0.getX(), arg0.getY()),
                            TestbedTest.BOMB_SPAWN_BUTTON);
                }
                else
                {
                    if (arg0.getButton() == TestbedTest.MOUSE_JOINT_BUTTON)
                    {
                        mouseJointButtonDown = true;
                    }
                    controller.queueMouseDown(
                            new Vec2(arg0.getX(), arg0.getY()),
                            arg0.getButton());
                }
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent arg0)
            {
                mouse.set(arg0.getX(), arg0.getY());
                controller.queueMouseMove(new Vec2(mouse));
            }

            @Override
            public void mouseDragged(MouseEvent arg0)
            {
                mouse.set(arg0.getX(), arg0.getY());
                if (screenDragButtonDown)
                {
                    TestbedTest currTest = model.getCurrTest();
                    if (currTest == null)
                    {
                        return;
                    }
                    Vec2 diff = oldDragMouse.sub(mouse);
                    currTest.getCamera().moveWorld(diff);
                    oldDragMouse.set(mouse);
                }
                else if (mouseJointButtonDown)
                {
                    controller.queueMouseDrag(new Vec2(mouse),
                            TestbedTest.MOUSE_JOINT_BUTTON);
                }
                else if (model.getCodedKeys()[KeyEvent.VK_SHIFT])
                {
                    controller.queueMouseDrag(
                            new Vec2(arg0.getX(), arg0.getY()),
                            TestbedTest.BOMB_SPAWN_BUTTON);
                }
                else
                {
                    controller.queueMouseDrag(new Vec2(mouse),
                            arg0.getButton());
                }
            }
        });
        panel.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent arg0)
            {
                controller.queueKeyReleased(arg0.getKeyChar(),
                        arg0.getKeyCode());
            }

            @Override
            public void keyPressed(KeyEvent arg0)
            {
                char c = arg0.getKeyChar();
                controller.queueKeyPressed(c, arg0.getKeyCode());
                switch (c)
                {
                case '[':
                    controller.lastTest();
                    break;

                case ']':
                    controller.nextTest();
                    break;

                case 'r':
                    controller.reset();
                    break;

                case ' ':
                    controller.queueLaunchBomb();
                    break;

                case 'p':
                    controller.queuePause();
                    break;
                }
            }
        });
    }
}
