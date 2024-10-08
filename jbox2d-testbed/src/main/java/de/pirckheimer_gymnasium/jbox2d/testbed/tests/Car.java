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
import de.pirckheimer_gymnasium.jbox2d.collision.shapes.PolygonShape;
import de.pirckheimer_gymnasium.jbox2d.common.MathUtils;
import de.pirckheimer_gymnasium.jbox2d.common.Vec2;
import de.pirckheimer_gymnasium.jbox2d.dynamics.Body;
import de.pirckheimer_gymnasium.jbox2d.dynamics.BodyDef;
import de.pirckheimer_gymnasium.jbox2d.dynamics.BodyType;
import de.pirckheimer_gymnasium.jbox2d.dynamics.FixtureDef;
import de.pirckheimer_gymnasium.jbox2d.dynamics.joints.Joint;
import de.pirckheimer_gymnasium.jbox2d.dynamics.joints.RevoluteJointDef;
import de.pirckheimer_gymnasium.jbox2d.dynamics.joints.WheelJoint;
import de.pirckheimer_gymnasium.jbox2d.dynamics.joints.WheelJointDef;
import de.pirckheimer_gymnasium.jbox2d.testbed.framework.TestbedSettings;
import de.pirckheimer_gymnasium.jbox2d.testbed.framework.TestbedTest;

/**
 * @repolink https://github.com/google/liquidfun/blob/master/liquidfun/Contributions/Tests/Car.h
 */
public class Car extends TestbedTest
{
    private static final long CAR_TAG = 100L;

    private static final long WHEEL1_TAG = 101L;

    private static final long WHEEL2_TAG = 102L;

    private static final long SPRING1_TAG = 103L;

    private static final long SPRING2_TAG = 104L;

    private Body car;

    private Body wheel1;

    private Body wheel2;

    private float hz;

    private float zeta;

    private float speed;

    private WheelJoint spring1;

    private WheelJoint spring2;

    @Override
    public Long getTag(Body body)
    {
        if (body == car)
        {
            return CAR_TAG;
        }
        if (body == wheel1)
        {
            return WHEEL1_TAG;
        }
        if (body == wheel2)
        {
            return WHEEL2_TAG;
        }
        return super.getTag(body);
    }

    @Override
    public Long getTag(Joint joint)
    {
        if (joint == spring1)
        {
            return SPRING1_TAG;
        }
        if (joint == spring2)
        {
            return SPRING2_TAG;
        }
        return super.getTag(joint);
    }

    @Override
    public void processBody(Body body, Long tag)
    {
        if (tag == CAR_TAG)
        {
            car = body;
        }
        else if (tag == WHEEL1_TAG)
        {
            wheel1 = body;
        }
        else if (tag == WHEEL2_TAG)
        {
            wheel2 = body;
        }
        else
        {
            super.processBody(body, tag);
        }
    }

    @Override
    public void processJoint(Joint joint, Long tag)
    {
        if (tag == SPRING1_TAG)
        {
            spring1 = (WheelJoint) joint;
        }
        else if (tag == SPRING2_TAG)
        {
            spring2 = (WheelJoint) joint;
        }
        else
        {
            super.processJoint(joint, tag);
        }
    }

    @Override
    public boolean isSaveLoadEnabled()
    {
        return true;
    }

    @Override
    public String getTestName()
    {
        return "Car";
    }

    @Override
    public void initTest(boolean deserialized)
    {
        if (deserialized)
        {
            return;
        }
        hz = 4.0f;
        zeta = 0.7f;
        speed = 50.0f;
        Body ground = null;
        {
            BodyDef bd = new BodyDef();
            ground = world.createBody(bd);
            EdgeShape shape = new EdgeShape();
            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.density = 0.0f;
            fd.friction = 0.6f;
            shape.set(new Vec2(-20.0f, 0.0f), new Vec2(20.0f, 0.0f));
            ground.createFixture(fd);
            float hs[] = { 0.25f, 1.0f, 4.0f, 0.0f, 0.0f, -1.0f, -2.0f, -2.0f,
                    -1.25f, 0.0f };
            float x = 20.0f, y1 = 0.0f, dx = 5.0f;
            for (int i = 0; i < 10; ++i)
            {
                float y2 = hs[i];
                shape.set(new Vec2(x, y1), new Vec2(x + dx, y2));
                ground.createFixture(fd);
                y1 = y2;
                x += dx;
            }
            for (int i = 0; i < 10; ++i)
            {
                float y2 = hs[i];
                shape.set(new Vec2(x, y1), new Vec2(x + dx, y2));
                ground.createFixture(fd);
                y1 = y2;
                x += dx;
            }
            shape.set(new Vec2(x, 0.0f), new Vec2(x + 40.0f, 0.0f));
            ground.createFixture(fd);
            x += 80.0f;
            shape.set(new Vec2(x, 0.0f), new Vec2(x + 40.0f, 0.0f));
            ground.createFixture(fd);
            x += 40.0f;
            shape.set(new Vec2(x, 0.0f), new Vec2(x + 10.0f, 5.0f));
            ground.createFixture(fd);
            x += 20.0f;
            shape.set(new Vec2(x, 0.0f), new Vec2(x + 40.0f, 0.0f));
            ground.createFixture(fd);
            x += 40.0f;
            shape.set(new Vec2(x, 0.0f), new Vec2(x, 20.0f));
            ground.createFixture(fd);
        }
        // Teeter
        {
            BodyDef bd = new BodyDef();
            bd.position.set(140.0f, 1.0f);
            bd.type = BodyType.DYNAMIC;
            Body body = world.createBody(bd);
            PolygonShape box = new PolygonShape();
            box.setAsBox(10.0f, 0.25f);
            body.createFixture(box, 1.0f);
            RevoluteJointDef jd = new RevoluteJointDef();
            jd.initialize(ground, body, body.getPosition());
            jd.lowerAngle = -8.0f * MathUtils.PI / 180.0f;
            jd.upperAngle = 8.0f * MathUtils.PI / 180.0f;
            jd.enableLimit = true;
            world.createJoint(jd);
            body.applyAngularImpulse(100.0f);
        }
        // Bridge
        {
            int N = 20;
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(1.0f, 0.125f);
            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.density = 1.0f;
            fd.friction = 0.6f;
            RevoluteJointDef jd = new RevoluteJointDef();
            Body prevBody = ground;
            for (int i = 0; i < N; ++i)
            {
                BodyDef bd = new BodyDef();
                bd.type = BodyType.DYNAMIC;
                bd.position.set(161.0f + 2.0f * i, -0.125f);
                Body body = world.createBody(bd);
                body.createFixture(fd);
                Vec2 anchor = new Vec2(160.0f + 2.0f * i, -0.125f);
                jd.initialize(prevBody, body, anchor);
                world.createJoint(jd);
                prevBody = body;
            }
            Vec2 anchor = new Vec2(160.0f + 2.0f * N, -0.125f);
            jd.initialize(prevBody, ground, anchor);
            world.createJoint(jd);
        }
        // Boxes
        {
            PolygonShape box = new PolygonShape();
            box.setAsBox(0.5f, 0.5f);
            Body body = null;
            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.position.set(230.0f, 0.5f);
            body = world.createBody(bd);
            body.createFixture(box, 0.5f);
            bd.position.set(230.0f, 1.5f);
            body = world.createBody(bd);
            body.createFixture(box, 0.5f);
            bd.position.set(230.0f, 2.5f);
            body = world.createBody(bd);
            body.createFixture(box, 0.5f);
            bd.position.set(230.0f, 3.5f);
            body = world.createBody(bd);
            body.createFixture(box, 0.5f);
            bd.position.set(230.0f, 4.5f);
            body = world.createBody(bd);
            body.createFixture(box, 0.5f);
        }
        // Car
        {
            PolygonShape chassis = new PolygonShape();
            Vec2 vertices[] = new Vec2[8];
            vertices[0] = new Vec2(-1.5f, -0.5f);
            vertices[1] = new Vec2(1.5f, -0.5f);
            vertices[2] = new Vec2(1.5f, 0.0f);
            vertices[3] = new Vec2(0.0f, 0.9f);
            vertices[4] = new Vec2(-1.15f, 0.9f);
            vertices[5] = new Vec2(-1.5f, 0.2f);
            chassis.set(vertices, 6);
            CircleShape circle = new CircleShape();
            circle.radius = 0.4f;
            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.position.set(0.0f, 1.0f);
            car = world.createBody(bd);
            car.createFixture(chassis, 1.0f);
            FixtureDef fd = new FixtureDef();
            fd.shape = circle;
            fd.density = 1.0f;
            fd.friction = 0.9f;
            bd.position.set(-1.0f, 0.35f);
            wheel1 = world.createBody(bd);
            wheel1.createFixture(fd);
            bd.position.set(1.0f, 0.4f);
            wheel2 = world.createBody(bd);
            wheel2.createFixture(fd);
            WheelJointDef jd = new WheelJointDef();
            Vec2 axis = new Vec2(0.0f, 1.0f);
            jd.initialize(car, wheel1, wheel1.getPosition(), axis);
            jd.motorSpeed = 0.0f;
            jd.maxMotorTorque = 20.0f;
            jd.enableMotor = true;
            jd.frequencyHz = hz;
            jd.dampingRatio = zeta;
            spring1 = (WheelJoint) world.createJoint(jd);
            jd.initialize(car, wheel2, wheel2.getPosition(), axis);
            jd.motorSpeed = 0.0f;
            jd.maxMotorTorque = 10.0f;
            jd.enableMotor = false;
            jd.frequencyHz = hz;
            jd.dampingRatio = zeta;
            spring2 = (WheelJoint) world.createJoint(jd);
        }
    }

    @Override
    public void keyPressed(char argKeyChar, int argKeyCode)
    {
        switch (argKeyChar)
        {
        case 'a':
            spring1.enableMotor(true);
            spring1.setMotorSpeed(speed);
            break;

        case 's':
            spring1.enableMotor(true);
            spring1.setMotorSpeed(0.0f);
            break;

        case 'd':
            spring1.enableMotor(true);
            spring1.setMotorSpeed(-speed);
            break;

        case 'q':
            hz = MathUtils.max(0.0f, hz - 1.0f);
            spring1.setSpringFrequencyHz(hz);
            spring2.setSpringFrequencyHz(hz);
            break;

        case 'e':
            hz += 1.0f;
            spring1.setSpringFrequencyHz(hz);
            spring2.setSpringFrequencyHz(hz);
            break;
        }
    }

    @Override
    public void keyReleased(char argKeyChar, int argKeyCode)
    {
        super.keyReleased(argKeyChar, argKeyCode);
        switch (argKeyChar)
        {
        case 'a':
        case 's':
        case 'd':
            spring1.enableMotor(false);
            break;
        }
    }

    @Override
    public float getDefaultCameraScale()
    {
        return 15;
    }

    @Override
    public synchronized void step(TestbedSettings settings)
    {
        super.step(settings);
        addTextLine(
                "Keys: left = a, brake = s, right = d, hz down = q, hz up = e");
        addTextLine("frequency = " + hz + " hz, damping ratio = " + zeta);
        getCamera().setCamera(car.getPosition());
    }
}
