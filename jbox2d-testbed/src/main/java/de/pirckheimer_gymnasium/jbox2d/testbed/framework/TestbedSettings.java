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
package de.pirckheimer_gymnasium.jbox2d.testbed.framework;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.pirckheimer_gymnasium.jbox2d.testbed.framework.TestbedSetting.SettingType;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Stores all the testbed settings. Automatically populates default settings.
 *
 * @author Daniel Murphy
 */
public class TestbedSettings
{
    public static final String Hz = "Hz";

    public static final String PositionIterations = "Pos Iters";

    public static final String VelocityIterations = "Vel Iters";

    public static final String AllowSleep = "Sleep";

    public static final String WarmStarting = "Warm Starting";

    public static final String SubStepping = "SubStepping";

    public static final String ContinuousCollision = "Continuous Collision";

    public static final String DrawShapes = "Shapes";

    public static final String DrawJoints = "Joints";

    public static final String DrawAABBs = "AABBs";

    public static final String DrawContactPoints = "Contact Points";

    public static final String DrawContactNormals = "Contact Normals";

    public static final String DrawContactImpulses = "Contact Impulses";

    public static final String DrawFrictionImpulses = "Friction Impulses";

    public static final String DrawCOMs = "Center of Mass";

    public static final String DrawStats = "Stats";

    public static final String DrawHelp = "Help";

    public static final String DrawTree = "Dynamic Tree";

    public static final String DrawWireframe = "Wireframe Mode";

    public boolean pause = false;

    public boolean singleStep = false;

    private final List<TestbedSetting> settings;

    private final Map<String, TestbedSetting> settingsMap;

    public TestbedSettings()
    {
        settings = Lists.newArrayList();
        settingsMap = Maps.newHashMap();
        populateDefaultSettings();
    }

    private void populateDefaultSettings()
    {
        addSetting(new TestbedSetting(Hz, SettingType.ENGINE, 60, 1, 400));
        addSetting(new TestbedSetting(PositionIterations, SettingType.ENGINE, 3,
                0, 100));
        addSetting(new TestbedSetting(VelocityIterations, SettingType.ENGINE, 8,
                1, 100));
        addSetting(new TestbedSetting(AllowSleep, SettingType.ENGINE, true));
        addSetting(new TestbedSetting(WarmStarting, SettingType.ENGINE, true));
        addSetting(new TestbedSetting(ContinuousCollision, SettingType.ENGINE,
                true));
        addSetting(new TestbedSetting(SubStepping, SettingType.ENGINE, false));
        addSetting(new TestbedSetting(DrawShapes, SettingType.DRAWING, true));
        addSetting(new TestbedSetting(DrawJoints, SettingType.DRAWING, true));
        addSetting(new TestbedSetting(DrawAABBs, SettingType.DRAWING, false));
        addSetting(new TestbedSetting(DrawContactPoints, SettingType.DRAWING,
                false));
        addSetting(new TestbedSetting(DrawContactNormals, SettingType.DRAWING,
                false));
        addSetting(new TestbedSetting(DrawContactImpulses, SettingType.DRAWING,
                false));
        addSetting(new TestbedSetting(DrawFrictionImpulses, SettingType.DRAWING,
                false));
        addSetting(new TestbedSetting(DrawCOMs, SettingType.DRAWING, false));
        addSetting(new TestbedSetting(DrawStats, SettingType.DRAWING, true));
        addSetting(new TestbedSetting(DrawHelp, SettingType.DRAWING, false));
        addSetting(new TestbedSetting(DrawTree, SettingType.DRAWING, false));
        addSetting(
                new TestbedSetting(DrawWireframe, SettingType.DRAWING, true));
    }

    /**
     * Adds a settings to the settings list
     */
    public void addSetting(TestbedSetting argSetting)
    {
        if (settingsMap.containsKey(argSetting.name))
        {
            throw new IllegalArgumentException(
                    "Settings already contain a setting with name: "
                            + argSetting.name);
        }
        settings.add(argSetting);
        settingsMap.put(argSetting.name, argSetting);
    }

    /**
     * Returns an unmodifiable list of settings
     */
    public List<TestbedSetting> getSettings()
    {
        return Collections.unmodifiableList(settings);
    }

    /**
     * Gets a setting by name.
     */
    public TestbedSetting getSetting(String argName)
    {
        return settingsMap.get(argName);
    }
}
