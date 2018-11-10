package com.lincolnhack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Barrier extends Image {

    private Body body;
    private World world;

    public Barrier(Texture texture, World world, float x, float y, float width, float height, float angle) {
        super(texture);

        this.setSize(width, height);
        this.setOrigin(this.getWidth()/2,this.getHeight()/2);
        this.rotateBy(angle);
        this.setPosition(x,y);
        this.world = world;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2(x, y));
        body = world.createBody(bodyDef);
        body.setTransform(this.getX()+this.getWidth()/2,this.getY()+this.getHeight()/2, (float)Math.toRadians(angle));

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(this.getWidth()/2, this.getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Filters.BOX2D_FILTER_IMPENETRABLE_BARRIER;
        fixtureDef.filter.maskBits = Filters.MASK_IMPENETRABLE_BARRIER;
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 0f;
        body.createFixture(fixtureDef);

        polygonShape.dispose();
    }

    @Override
    public void act(float delta) {
        body.setTransform(this.getX()+this.getWidth()/2,this.getY()+this.getHeight()/2, (float)Math.toRadians(getRotation()));
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
