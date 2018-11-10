package com.lincolnhack;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.lincolnhack.Filters.BOX2D_FILTER_PLAYER_PENETRABLE_BARRIER;

public class PaddleBarrier {


    private Body body;
    private World world;

    public PaddleBarrier(World world, float x, float y, float width, float height, float angle) {
        this.world = world;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2(x, y));
        body = world.createBody(bodyDef);
        body.setTransform(x + width / 2,y + height / 2, (float)Math.toRadians(angle));

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width/2, height/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = BOX2D_FILTER_PLAYER_PENETRABLE_BARRIER;
        fixtureDef.filter.maskBits = Filters.MASK_PENETRABLE_BARRIER;
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef);

        polygonShape.dispose();
    }
}
