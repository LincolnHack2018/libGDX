package com.lincolnhack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.io.FileReader;

import lombok.Getter;

public class Puck extends Image {

    @Getter private Circle scoringCircle;
    @Getter private Body body;
    private World world;

    public Puck(Texture texture, World world, float x, float y, float radius, float angle) {
        super(texture);

        this.setSize(radius * 2, radius * 2);
        this.setOrigin(this.getWidth()/2,this.getHeight()/2);
        this.rotateBy(angle);
        this.setPosition(x,y);
        this.world = world;

        BodyDef puckBodyDef = new BodyDef();
        puckBodyDef.type = BodyDef.BodyType.DynamicBody;
        puckBodyDef.position.set(new Vector2(x, y));
        body = world.createBody(puckBodyDef);
        body.setTransform(this.getX()+this.getWidth()/2,this.getY()+this.getHeight()/2, (float)Math.toRadians(angle));

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Filters.BOX2D_FILTER_PUCK;
        fixtureDef.filter.maskBits = Filters.MASK_PUCK;
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 100f;
        fixtureDef.restitution= 1f;
        body.createFixture(fixtureDef);

        circleShape.dispose();

        scoringCircle = new Circle();
        scoringCircle.setRadius(radius);
        scoringCircle.setPosition(x, y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.setRotation(body.getAngle()*  MathUtils.radiansToDegrees);

        this.setPosition(body.getPosition().x-this.getWidth()/2,body.getPosition().y-this.getHeight()/2);

        this.scoringCircle.setPosition(body.getPosition().x-this.getWidth()/2,body.getPosition().y-this.getHeight()/2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
