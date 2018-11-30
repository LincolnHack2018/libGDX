package com.lincolnhack.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.lincolnhack.Filters;
import com.lincolnhack.Puck;


public class Paddle extends Image {

    public static final float PADDLE_RADIUS = 0.5f;

    Stage stage;
    Body paddleBody;
    World world;
    MouseJoint mouseJoint;
    Vector2 target = new Vector2();
    Vector3 testPoint = new Vector3();
    Body puck;
    Body hitBody;

    Paddle(Texture texture, Stage stage, World world, Puck puck, float x, float y, float radius, float angle) {
        super(texture);
        this.stage = stage;
        this.puck = puck.getBody();
        this.setSize(radius * 2, radius * 2);
        this.setOrigin(this.getWidth()/2,this.getHeight()/2);
        this.rotateBy(angle);
        this.setPosition(x,y);
        this.world = world;

        MassData mass = new MassData();
        mass.mass = 10f;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(x, y));
        paddleBody = world.createBody(bodyDef);
        paddleBody.setTransform(this.getX()+this.getWidth()/2,this.getY()+this.getHeight()/2, (float)Math.toRadians(angle));
        paddleBody.setMassData(mass);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Filters.BOX2D_FILTER_PADDLE;
        fixtureDef.filter.maskBits = Filters.MASK_PADDLE;
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1f;
        paddleBody.createFixture(fixtureDef);
        paddleBody.setAngularDamping(1f);
        paddleBody.setLinearDamping(1f);

        circleShape.dispose();
    }

    @Override
    public void act(float delta) {
        //this.setRotation(paddleBody.getAngle()*  MathUtils.radiansToDegrees);

        this.setPosition(paddleBody.getPosition().x-this.getWidth()/2,paddleBody.getPosition().y-this.getHeight()/2);
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }


}

