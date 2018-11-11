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

public class Paddle extends Image implements InputProcessor {

    private Stage stage;
    private Body paddleBody;
    private World world;
    private MouseJoint mouseJoint;
    private Vector2 target = new Vector2();
    private Vector3 testPoint = new Vector3();
    private Body puck;
    private Body hitBody;

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
                paddleH = new Paddle(paddleTx, stage, world, puck,stage.getViewport().getWorldWidth() / 2 - 0.5f, 3f, 1, 0);

                return paddleH;
            case VERTICAL_BOTTOM:
                Paddle paddleV = null;
                paddleV = new Paddle(paddleTx, stage, world, puck,stage.getViewport().getWorldWidth() / 2 - 0.5f, 3f, 1, 0);

                return paddleV;
            case VERTICAL_TOP:
                Paddle paddleV_Top = null;
                paddleV_Top = new Paddle(paddleTx, stage, world, puck,stage.getViewport().getWorldWidth() / 2 - 0.5f, stage.getViewport().getWorldHeight() - 3f, 1, 0);

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

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (character=='d') {
            float h = stage.getViewport().getWorldHeight();
            float w = stage.getViewport().getWorldWidth();
            stage.getCamera().position.x = w/2;
            stage.getCamera().position.y = h * 3/2;

            return true;
        }
        if (character=='b') {
            float h = stage.getViewport().getWorldHeight();
            float w = stage.getViewport().getWorldWidth();
            stage.getCamera().position.x = w/2;
            stage.getCamera().position.y = h/2;

            return true;
        }
        return false;
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


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        testPoint.set(screenX, screenY, 0);
        stage.getCamera().unproject(testPoint);

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

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (mouseJoint != null) {
            world.destroyJoint(mouseJoint);
            mouseJoint = null;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (mouseJoint != null) {
            stage.getCamera().unproject(testPoint.set(screenX, screenY, 0));
            mouseJoint.setTarget(target.set(testPoint.x, testPoint.y));
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

