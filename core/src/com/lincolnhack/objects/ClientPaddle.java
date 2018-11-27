package com.lincolnhack.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.lincolnhack.Puck;
import com.lincolnhack.data.Pair;

public class ClientPaddle extends Paddle {

    public ClientPaddle(Texture texture, Stage stage, World world, Puck puck, float x, float y, float radius, float angle) {
        super(texture, stage, world, puck, x, y, radius, angle);
    }

    QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture (Fixture fixture) {

            if (fixture.getBody() == puck) return true;

            if (fixture.testPoint(testPoint.x, testPoint.y)) {
                hitBody = fixture.getBody();
                return false;
            } else
                return true;
        }
    };

    public void update(Pair<Float> touch) {
        testPoint.set(touch.getFirst(), touch.getSecond(), 0);

        hitBody = null;
        world.QueryAABB(callback, testPoint.x - 0.1f, testPoint.y - 0.1f, testPoint.x + 0.1f, testPoint.y + 0.1f);

        if (hitBody != null) {
            MouseJointDef def = new MouseJointDef();
            def.bodyA = puck;
            def.bodyB = paddleBody;
            def.dampingRatio = 1f;
            def.frequencyHz = 10f;
            def.collideConnected = true;
            def.target.set(testPoint.x, testPoint.y);
            def.maxForce = 10000.0f * hitBody.getMass();

            mouseJoint = (MouseJoint)world.createJoint(def);
            hitBody.setAwake(true);
        }
    }
}
