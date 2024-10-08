package de.pirckheimer_gymnasium.jbox2d.testbed.tests;

import de.pirckheimer_gymnasium.jbox2d.collision.shapes.CircleShape;
import de.pirckheimer_gymnasium.jbox2d.collision.shapes.EdgeShape;
import de.pirckheimer_gymnasium.jbox2d.collision.shapes.PolygonShape;
import de.pirckheimer_gymnasium.jbox2d.common.Vec2;
import de.pirckheimer_gymnasium.jbox2d.dynamics.Body;
import de.pirckheimer_gymnasium.jbox2d.dynamics.BodyDef;
import de.pirckheimer_gymnasium.jbox2d.dynamics.BodyType;
import de.pirckheimer_gymnasium.jbox2d.particle.ParticleGroupDef;
import de.pirckheimer_gymnasium.jbox2d.particle.ParticleType;
import de.pirckheimer_gymnasium.jbox2d.testbed.framework.TestbedSettings;
import de.pirckheimer_gymnasium.jbox2d.testbed.framework.TestbedTest;

/**
 * @author Daniel Murphy
 */
public class ParticleTypes extends TestbedTest
{
    Body circle;

    int flags = ParticleType.tensileParticle;

    @Override
    public void initTest(boolean deserialized)
    {
        {
            BodyDef bd = new BodyDef();
            Body ground = world.createBody(bd);
            {
                PolygonShape shape = new PolygonShape();
                Vec2[] vertices = new Vec2[] { new Vec2(-40, -10),
                        new Vec2(40, -10), new Vec2(40, 0), new Vec2(-40, 0) };
                shape.set(vertices, 4);
                ground.createFixture(shape, 0.0f);
            }
            {
                PolygonShape shape = new PolygonShape();
                Vec2[] vertices = new Vec2[] { new Vec2(-40, -1),
                        new Vec2(-20, -1), new Vec2(-20, 20),
                        new Vec2(-40, 30) };
                shape.set(vertices, 4);
                ground.createFixture(shape, 0.0f);
            }
            {
                PolygonShape shape = new PolygonShape();
                Vec2[] vertices = new Vec2[] { new Vec2(20, -1),
                        new Vec2(40, -1), new Vec2(40, 30), new Vec2(20, 20) };
                shape.set(vertices, 4);
                ground.createFixture(shape, 0.0f);
            }
        }
        world.setParticleRadius(0.2f);
        {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(20, 10, new Vec2(0, 10), 0);
            ParticleGroupDef pd = new ParticleGroupDef();
            pd.flags = pd.flags;
            pd.shape = shape;
            world.createParticleGroup(pd);
        }
        {
            BodyDef bd = new BodyDef();
            bd.type = BodyType.KINEMATIC;
            Body body = world.createBody(bd);
            circle = body;
            CircleShape shape = new CircleShape();
            shape.p.set(0, 5);
            shape.radius = 1;
            body.createFixture(shape, 0.1f);
            body.setLinearVelocity(new Vec2(-6, 0.0f));
        }
        {
            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            Body body = world.createBody(bd);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(1, 1, new Vec2(-10, 5), 0);
            body.createFixture(shape, 0.1f);
        }
        {
            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            Body body = world.createBody(bd);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(1, 1, new Vec2(10, 5), 0.5f);
            body.createFixture(shape, 0.1f);
        }
        {
            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            Body body = world.createBody(bd);
            EdgeShape shape = new EdgeShape();
            shape.set(new Vec2(0, 20), new Vec2(1, 21));
            body.createFixture(shape, 0.1f);
        }
        {
            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            Body body = world.createBody(bd);
            EdgeShape shape = new EdgeShape();
            shape.set(new Vec2(3, 20), new Vec2(4, 21));
            body.createFixture(shape, 0.1f);
        }
        {
            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            Body body = world.createBody(bd);
            EdgeShape shape = new EdgeShape();
            shape.set(new Vec2(-3, 21), new Vec2(-2, 20));
            body.createFixture(shape, 0.1f);
        }
    }

    @Override
    public void step(TestbedSettings settings)
    {
        super.step(settings);
        Vec2 p = circle.getTransform().p;
        Vec2 v = circle.getLinearVelocity();
        if ((p.x < -10.0f && v.x < 0.0f) || (p.x > 10.0f && v.x > 0.0f))
        {
            v.x = -v.x;
            circle.setLinearVelocity(v);
        }
        int[] flagsBuffer = world.getParticleFlagsBuffer();
        for (int i = 0; i < world.getParticleCount(); i++)
        {
            flagsBuffer[i] = flags;
        }
        addTextLine("'a' Clear");
        addTextLine(
                "'e' Elastic " + ((flags & ParticleType.elasticParticle) != 0));
        addTextLine(
                "'q' Powder  " + ((flags & ParticleType.powderParticle) != 0));
        addTextLine(
                "'t' Tensile " + ((flags & ParticleType.tensileParticle) != 0));
        addTextLine(
                "'v' Viscous " + ((flags & ParticleType.viscousParticle) != 0));
    }

    @Override
    public void keyPressed(char keyCar, int keyCode)
    {
        super.keyPressed(keyCar, keyCode);
        int toggle = 0;
        switch (keyCar)
        {
        case 'a':
            flags = 0;
            break;

        case 'e':
            toggle = ParticleType.elasticParticle;
            break;

        case 'q':
            toggle = ParticleType.powderParticle;
            break;

        case 't':
            toggle = ParticleType.tensileParticle;
            break;

        case 'v':
            toggle = ParticleType.viscousParticle;
            break;
        }
        if (toggle != 0)
        {
            if ((flags & toggle) != 0)
            {
                flags = flags & ~toggle;
            }
            else
            {
                flags = flags | toggle;
            }
        }
    }

    @Override
    public String getTestName()
    {
        return "ParticleTypes";
    }
}
