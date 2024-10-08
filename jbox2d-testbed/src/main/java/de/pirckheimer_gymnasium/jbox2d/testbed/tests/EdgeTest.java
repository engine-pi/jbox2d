package de.pirckheimer_gymnasium.jbox2d.testbed.tests;

import de.pirckheimer_gymnasium.jbox2d.collision.shapes.CircleShape;
import de.pirckheimer_gymnasium.jbox2d.collision.shapes.EdgeShape;
import de.pirckheimer_gymnasium.jbox2d.collision.shapes.PolygonShape;
import de.pirckheimer_gymnasium.jbox2d.common.Vec2;
import de.pirckheimer_gymnasium.jbox2d.dynamics.Body;
import de.pirckheimer_gymnasium.jbox2d.dynamics.BodyDef;
import de.pirckheimer_gymnasium.jbox2d.dynamics.BodyType;
import de.pirckheimer_gymnasium.jbox2d.testbed.framework.TestbedTest;

/**
 * @author Daniel Murphy
 *
 * @repolink https://github.com/google/liquidfun/blob/master/liquidfun/Box2D/Testbed/Tests/EdgeTest.h
 */
public class EdgeTest extends TestbedTest
{
    @Override
    public void initTest(boolean deserialized)
    {
        {
            Body ground = getGroundBody();
            Vec2 v1 = new Vec2(-10.0f, 0.0f), v2 = new Vec2(-7.0f, -2.0f),
                    v3 = new Vec2(-4.0f, 0.0f);
            Vec2 v4 = new Vec2(0.0f, 0.0f), v5 = new Vec2(4.0f, 0.0f),
                    v6 = new Vec2(7.0f, 2.0f), v7 = new Vec2(10.0f, 0.0f);
            EdgeShape shape = new EdgeShape();
            shape.set(v1, v2);
            shape.hasVertex3 = true;
            shape.vertex3.set(v3);
            ground.createFixture(shape, 0.0f);
            shape.set(v2, v3);
            shape.hasVertex0 = true;
            shape.hasVertex3 = true;
            shape.vertex0.set(v1);
            shape.vertex3.set(v4);
            ground.createFixture(shape, 0.0f);
            shape.set(v3, v4);
            shape.hasVertex0 = true;
            shape.hasVertex3 = true;
            shape.vertex0.set(v2);
            shape.vertex3.set(v5);
            ground.createFixture(shape, 0.0f);
            shape.set(v4, v5);
            shape.hasVertex0 = true;
            shape.hasVertex3 = true;
            shape.vertex0.set(v3);
            shape.vertex3.set(v6);
            ground.createFixture(shape, 0.0f);
            shape.set(v5, v6);
            shape.hasVertex0 = true;
            shape.hasVertex3 = true;
            shape.vertex0.set(v4);
            shape.vertex3.set(v7);
            ground.createFixture(shape, 0.0f);
            shape.set(v6, v7);
            shape.hasVertex0 = true;
            shape.vertex0.set(v5);
            ground.createFixture(shape, 0.0f);
        }
        {
            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.position.set(-0.5f, 0.6f);
            bd.allowSleep = false;
            Body body = world.createBody(bd);
            CircleShape shape = new CircleShape();
            shape.radius = 0.5f;
            body.createFixture(shape, 1.0f);
        }
        {
            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.position.set(1.0f, 0.6f);
            bd.allowSleep = false;
            Body body = world.createBody(bd);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.5f, 0.5f);
            body.createFixture(shape, 1.0f);
        }
    }

    @Override
    public String getTestName()
    {
        return "Edge Test";
    }
}
