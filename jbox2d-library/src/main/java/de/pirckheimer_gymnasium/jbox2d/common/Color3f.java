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
package de.pirckheimer_gymnasium.jbox2d.common;

/**
 * Similar to {@code javax.vecmath.Color3f} holder
 *
 * @author ewjordan
 */
public class Color3f
{
    public static final Color3f WHITE = new Color3f(1, 1, 1);

    public static final Color3f BLACK = new Color3f(0, 0, 0);

    public static final Color3f BLUE = new Color3f(0, 0, 1);

    public static final Color3f GREEN = new Color3f(0, 1, 0);

    public static final Color3f RED = new Color3f(1, 0, 0);

    public float x;

    public float y;

    public float z;

    public Color3f()
    {
        x = y = z = 0;
    }

    public Color3f(float r, float g, float b)
    {
        x = r;
        y = g;
        z = b;
    }

    public void set(float r, float g, float b)
    {
        x = r;
        y = g;
        z = b;
    }

    public void set(Color3f argColor)
    {
        x = argColor.x;
        y = argColor.y;
        z = argColor.z;
    }
}
