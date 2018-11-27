package com.lincolnhack;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import static com.lincolnhack.LibGDX.PADDLE;

public class Paddle extends Image {

    private static final float PADDLE_RADIUS = 0.5f;

    Stage stage;
    Body paddleBody;
    World world;
    MouseJoint mouseJoint;
    Vector2 target = new Vector2();
    Vector3 testPoint = new Vector3();
    Body puck;
    Body hitBody;

    public Paddle(Texture texture, Stage stage, World world, Puck puck, float x, float y, float radius, float angle) {
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

    public static Paddle resetPaddle(Orientation orientation, Stage stage, World world, AssetManager assetManager, Puck puck) {

        Texture paddleTx = assetManager.get(PADDLE);
        switch (orientation) {
            case HORIZONTAL_LEFT:
                Paddle paddleH = null;
                paddleH = new Paddle(paddleTx, stage, world, puck,stage.getViewport().getWorldWidth() / 2 - 0.5f, 3f, PADDLE_RADIUS, 0);

                return paddleH;
            case VERTICAL_BOTTOM:
                Paddle paddleV = null;
                paddleV = new Paddle(paddleTx, stage, world, puck,stage.getViewport().getWorldWidth() / 2 - 0.5f, 3f, PADDLE_RADIUS, 0);

                return paddleV;
            case VERTICAL_TOP:
                Paddle paddleV_Top = null;
                paddleV_Top = new Paddle(paddleTx, stage, world, puck,stage.getViewport().getWorldWidth() / 2 - 0.5f, stage.getViewport().getWorldHeight() - 3f, PADDLE_RADIUS, 0);

                return paddleV_Top;

            default:
                throw new RuntimeException("NO!");
        }
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

