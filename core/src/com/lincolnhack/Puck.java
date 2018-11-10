package com.lincolnhack;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Puck extends Image {
    private Body body;
    private World world;

    public Puck(TextureRegion textureRegion, World world, float x, float y, float radius, float angle) {
        super(textureRegion);

        this.setSize(radius, radius);
        this.setOrigin(this.getWidth()/2,this.getHeight()/2);
        this.rotateBy(angle);
        this.setPosition(x,y);
        this.world = world;
        BodyDef puckBodyDef = new BodyDef();
        puckBodyDef.type = BodyDef.BodyType.DynamicBody;

        puckBodyDef.position.set(new Vector2(x, y));

        // If we always want to apply force at the center of the body, use the following
        body = world.createBody(puckBodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(this.getWidth()/2);
        body.setTransform(this.getX()+this.getWidth()/2,this.getY()+this.getHeight()/2, (float)Math.toRadians(angle));


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 5f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution= 1f;
        Fixture fixture = body.createFixture(fixtureDef);


        circleShape.dispose();
    }

    @Override
    public void act(float delta) {
        //body.setTransform(this.getX()+this.getWidth()/2,this.getY()+this.getHeight()/2, (float)Math.toRadians(getRotation()));
        super.act(delta);

        this.setRotation(body.getAngle()*  MathUtils.radiansToDegrees);

        this.setPosition(body.getPosition().x-this.getWidth()/2,body.getPosition().y-this.getHeight()/2);


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
