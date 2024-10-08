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
package de.pirckheimer_gymnasium.jbox2d.dynamics.joints;

import de.pirckheimer_gymnasium.jbox2d.common.Vec2;
import de.pirckheimer_gymnasium.jbox2d.dynamics.Body;

/**
 * Revolute joint definition. This requires defining an anchor point where the
 * bodies are joined. The definition uses local anchor points so that the
 * initial configuration can violate the constraint slightly. You also need to
 * specify the initial relative angle for joint limits. This helps when saving
 * and loading a game. The local anchor points are measured from the body's
 * origin rather than the center of mass because:<br/>
 * <ul>
 * <li>you might not know where the center of mass will be.</li>
 * <li>if you add/remove shapes from a body and recompute the mass, the joints
 * will be broken.</li>
 * </ul>
 *
 * @author Daniel Murphy
 */
public class RevoluteJointDef extends JointDef
{
    /**
     * The local anchor point relative to body1's origin.
     */
    public Vec2 localAnchorA;

    /**
     * The local anchor point relative to body2's origin.
     */
    public Vec2 localAnchorB;

    /**
     * The body2 angle minus body1 angle in the reference state (radians).
     */
    public float referenceAngle;

    /**
     * A flag to enable joint limits.
     */
    public boolean enableLimit;

    /**
     * The lower angle for the joint limit (radians).
     */
    public float lowerAngle;

    /**
     * The upper angle for the joint limit (radians).
     */
    public float upperAngle;

    /**
     * A flag to enable the joint motor.
     */
    public boolean enableMotor;

    /**
     * The desired motor speed. Usually in radians per second.
     */
    public float motorSpeed;

    /**
     * The maximum motor torque used to achieve the desired motor speed. Usually
     * in N-m.
     */
    public float maxMotorTorque;

    public RevoluteJointDef()
    {
        super(JointType.REVOLUTE);
        localAnchorA = new Vec2(0.0f, 0.0f);
        localAnchorB = new Vec2(0.0f, 0.0f);
        referenceAngle = 0.0f;
        lowerAngle = 0.0f;
        upperAngle = 0.0f;
        maxMotorTorque = 0.0f;
        motorSpeed = 0.0f;
        enableLimit = false;
        enableMotor = false;
    }

    /**
     * Initialize the bodies, anchors, and reference angle using the world
     * anchor.
     */
    public void initialize(final Body b1, final Body b2, final Vec2 anchor)
    {
        bodyA = b1;
        bodyB = b2;
        bodyA.getLocalPointToOut(anchor, localAnchorA);
        bodyB.getLocalPointToOut(anchor, localAnchorB);
        referenceAngle = bodyB.getAngle() - bodyA.getAngle();
    }
}
