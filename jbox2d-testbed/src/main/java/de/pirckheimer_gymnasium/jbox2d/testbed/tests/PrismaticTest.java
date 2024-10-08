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

import de.pirckheimer_gymnasium.jbox2d.collision.shapes.EdgeShape;
import de.pirckheimer_gymnasium.jbox2d.collision.shapes.PolygonShape;
import de.pirckheimer_gymnasium.jbox2d.common.MathUtils;
import de.pirckheimer_gymnasium.jbox2d.common.Vec2;
import de.pirckheimer_gymnasium.jbox2d.dynamics.Body;
import de.pirckheimer_gymnasium.jbox2d.dynamics.BodyDef;
import de.pirckheimer_gymnasium.jbox2d.dynamics.BodyType;
import de.pirckheimer_gymnasium.jbox2d.dynamics.joints.Joint;
import de.pirckheimer_gymnasium.jbox2d.dynamics.joints.PrismaticJoint;
import de.pirckheimer_gymnasium.jbox2d.dynamics.joints.PrismaticJointDef;
import de.pirckheimer_gymnasium.jbox2d.testbed.framework.TestbedSettings;
import de.pirckheimer_gymnasium.jbox2d.testbed.framework.TestbedTest;

/**
 * @author Daniel Murphy
 *
 * @repolink https://github.com/google/liquidfun/blob/master/liquidfun/Box2D/Testbed/Tests/Prismatic.h
 */
public class PrismaticTest extends TestbedTest
{
    private static final long JOINT_TAG = 1;

    PrismaticJoint joint;

    @Override
    public Long getTag(Joint joint)
    {
        if (joint == this.joint)
            return JOINT_TAG;
        return super.getTag(joint);
    }

    @Override
    public void processJoint(Joint joint, Long tag)
    {
        if (tag == JOINT_TAG)
        {
            this.joint = (PrismaticJoint) joint;
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
    public void initTest(boolean deserialized)
    {
        if (deserialized)
        {
            return;
        }
        Body ground = null;
        {
            BodyDef bd = new BodyDef();
            ground = getWorld().createBody(bd);
            EdgeShape shape = new EdgeShape();
            shape.set(new Vec2(-40.0f, 0.0f), new Vec2(40.0f, 0.0f));
            ground.createFixture(shape, 0.0f);
        }
        {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(2.0f, 0.5f);
            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.position.set(-10.0f, 10.0f);
            bd.angle = 0.5f * MathUtils.PI;
            bd.allowSleep = false;
            Body body = getWorld().createBody(bd);
            body.createFixture(shape, 5.0f);
            PrismaticJointDef pjd = new PrismaticJointDef();
            // Bouncy limit
            Vec2 axis = new Vec2(2.0f, 1.0f);
            axis.normalize();
            pjd.initialize(ground, body, new Vec2(0.0f, 0.0f), axis);
            // Non-bouncy limit
            // pjd.Initialize(ground, body, Vec2(-10.0f, 10.0f), Vec2(1.0f,
            // 0.0f));
            pjd.motorSpeed = 10.0f;
            pjd.maxMotorForce = 10000.0f;
            pjd.enableMotor = true;
            pjd.lowerTranslation = 0.0f;
            pjd.upperTranslation = 20.0f;
            pjd.enableLimit = true;
            joint = (PrismaticJoint) getWorld().createJoint(pjd);
        }
    }

    @Override
    public void step(TestbedSettings settings)
    {
        super.step(settings);
        addTextLine("Keys: (l) limits, (m) motors, (s) speed");
        float force = joint.getMotorForce(1);
        addTextLine("Motor Force = " + force);
    }

    @Override
    public void keyPressed(char argKeyChar, int argKeyCode)
    {
        switch (argKeyChar)
        {
        case 'l':
            joint.enableLimit(!joint.isLimitEnabled());
            getModel().getKeys()['l'] = false;
            break;

        case 'm':
            joint.enableMotor(!joint.isMotorEnabled());
            getModel().getKeys()['m'] = false;
            break;

        case 's':
            joint.setMotorSpeed(-joint.getMotorSpeed());
            getModel().getKeys()['s'] = false;
            break;
        }
    }

    @Override
    public String getTestName()
    {
        return "Prismatic";
    }
}
