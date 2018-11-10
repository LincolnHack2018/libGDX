package com.lincolnhack;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Paddle extends Image {

    private Body body;
    private World world;

    public Paddle(TextureRegion textureRegion, World world, float x, float y, float radius, float angle) {
        super(textureRegion);
        this.setSize(radius, radius);
        this.setOrigin(this.getWidth()/2,this.getHeight()/2);
        this.rotateBy(angle);
        this.setPosition(x,y);
        this.world = world;
        BodyDef groundBodyDef = new BodyDef();

        groundBodyDef.position.set(new Vector2(x, y));
        body = world.createBody(groundBodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(this.getWidth()/2);
        body.setTransform(this.getX()+this.getWidth()/2,this.getY()+this.getHeight()/2, (float)Math.toRadians(angle));

        body.createFixture(circleShape, 0.0f);

        circleShape.dispose();
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

