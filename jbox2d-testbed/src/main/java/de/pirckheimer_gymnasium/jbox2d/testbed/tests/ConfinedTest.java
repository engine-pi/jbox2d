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
package de.pirckheimer_gymnasium.jbox2d.testbed.tests;

import de.pirckheimer_gymnasium.jbox2d.collision.shapes.CircleShape;
import de.pirckheimer_gymnasium.jbox2d.collision.shapes.EdgeShape;
import de.pirckheimer_gymnasium.jbox2d.common.Vec2;
import de.pirckheimer_gymnasium.jbox2d.dynamics.Body;
import de.pirckheimer_gymnasium.jbox2d.dynamics.BodyDef;
import de.pirckheimer_gymnasium.jbox2d.dynamics.BodyType;
import de.pirckheimer_gymnasium.jbox2d.dynamics.FixtureDef;
import de.pirckheimer_gymnasium.jbox2d.testbed.framework.TestbedSettings;
import de.pirckheimer_gymnasium.jbox2d.testbed.framework.TestbedTest;

/**
 * @author Daniel Murphy
 *
 * @repolink https://github.com/google/liquidfun/blob/master/liquidfun/Box2D/Testbed/Tests/Confined.h
 */
public class ConfinedTest extends TestbedTest
{
    int columnCount = 0;

    int rowCount = 0;

    @Override
    public boolean isSaveLoadEnabled()
    {
        return true;
    }

    @Override
    public String getTestName()
    {
        return "Confined";
    }

    @Override
    public void initTest(boolean argDeserialized)
    {
        if (argDeserialized)
        {
            return;
        }
        {
            BodyDef bd = new BodyDef();
            Body ground = getWorld().createBody(bd);
            EdgeShape shape = new EdgeShape();
            // Floor
            shape.set(new Vec2(-10.0f, 0.0f), new Vec2(10.0f, 0.0f));
            ground.createFixture(shape, 0.0f);
            // Left wall
            shape.set(new Vec2(-10.0f, 0.0f), new Vec2(-10.0f, 20.0f));
            ground.createFixture(shape, 0.0f);
            // Right wall
            shape.set(new Vec2(10.0f, 0.0f), new Vec2(10.0f, 20.0f));
            ground.createFixture(shape, 0.0f);
            // Roof
            shape.set(new Vec2(-10.0f, 20.0f), new Vec2(10.0f, 20.0f));
            ground.createFixture(shape, 0.0f);
        }
        float radius = 0.5f;
        CircleShape shape = new CircleShape();
        shape.p.setZero();
        shape.radius = radius;
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1.0f;
        fd.friction = 0.1f;
        for (int j = 0; j < columnCount; ++j)
        {
            for (int i = 0; i < rowCount; ++i)
            {
                BodyDef bd = new BodyDef();
                bd.type = BodyType.DYNAMIC;
                bd.position.set(-10.0f + (2.1f * j + 1.0f + 0.01f * i) * radius,
                        (2.0f * i + 1.0f) * radius);
                Body body = getWorld().createBody(bd);
                body.createFixture(fd);
            }
        }
        getWorld().setGravity(new Vec2(0.0f, 0.0f));
    }

    public void createCircle()
    {
        float radius = 2.0f;
        CircleShape shape = new CircleShape();
        shape.p.setZero();
        shape.radius = radius;
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1.0f;
        fd.friction = 0.0f;
        Vec2 p = new Vec2((float) Math.random(), 3.0f + (float) Math.random());
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position = p;
        // bd.allowSleep = false;
        Body body = getWorld().createBody(bd);
        body.createFixture(fd);
    }

    @Override
    public void step(TestbedSettings settings)
    {
        super.step(settings);
        for (Body b = getWorld().getBodyList(); b != null; b = b.getNext())
        {
            if (b.getType() != BodyType.DYNAMIC)
            {
                continue;
            }
            Vec2 p = b.getPosition();
            if (p.x <= -10.0f || 10.0f <= p.x || p.y <= 0.0f || 20.0f <= p.y)
            {
                p.x += 0.0;
            }
        }
        addTextLine("Press 'c' to create a circle");
    }

    @Override
    public void keyPressed(char argKeyChar, int argKeyCode)
    {
        switch (argKeyChar)
        {
        case 'c':
            createCircle();
            break;
        }
    }
}
