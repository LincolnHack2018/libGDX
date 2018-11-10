package com.lincolnhack;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import lombok.Data;
import lombok.Getter;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;
import static com.lincolnhack.LibGDX.BARRIER;
import static com.lincolnhack.LibGDX.GOAL;
import static com.lincolnhack.LibGDX.PADDLE;

@Data
public class Field {

    private static final float BARRIER_THICKNESS = 0.25f;

    World world;
    Stage stage;

    @Getter Actor paddle;
    Actor goal;

    Barrier sideBarrierLeft;
    Barrier sideBarrierRight;
    Barrier goalBarrierLeft;
    Barrier goalBarrierRight;

    Rectangle scoringField;

    PaddleBarrier top;
    PaddleBarrier bottom;

    public Field(Orientation orientation, World world, Stage stage, AssetManager assetManager, Puck puck) {
        this.world = world;
        float worldHeight = stage.getViewport().getWorldHeight();
        float worldWidth = stage.getViewport().getWorldWidth();

        Texture paddleTx = assetManager.get(PADDLE);
        Texture goalTx = assetManager.get(GOAL);

        Texture barrierTx = assetManager.get(BARRIER);
        barrierTx.setWrap(Repeat, Repeat);

        switch (orientation) {
            case HORIZONTAL:
                sideBarrierLeft = new Barrier(barrierTx, world, 0, worldHeight - BARRIER_THICKNESS, worldWidth, BARRIER_THICKNESS,0);
                sideBarrierRight = new Barrier(barrierTx, world, 0, 0, worldWidth, BARRIER_THICKNESS,0);
                goalBarrierLeft = new Barrier(barrierTx, world, 0, worldHeight * 2 / 3,  BARRIER_THICKNESS,worldHeight / 3,0);
                goalBarrierRight = new Barrier(barrierTx, world, 0, 0, BARRIER_THICKNESS, worldHeight / 3,0);
                bottom = new PaddleBarrier(world, 0, 0, BARRIER_THICKNESS, worldHeight, 0);
                top = new PaddleBarrier(world, worldWidth, 0, BARRIER_THICKNESS, worldHeight, 0);
                goal = new Goal(goalTx, world, 0, worldHeight / 2 + 1.5f,  3,1.5f, -90);
                break;
            case VERTICAL:
                sideBarrierLeft = new Barrier(barrierTx, world, 0, 0, BARRIER_THICKNESS, worldHeight,0);
                sideBarrierRight = new Barrier(barrierTx, world, worldWidth - BARRIER_THICKNESS, 0, BARRIER_THICKNESS, worldHeight,0);
                goalBarrierLeft = new Barrier(barrierTx, world, 0, 0,  worldWidth / 3, BARRIER_THICKNESS ,0);
                goalBarrierRight = new Barrier(barrierTx, world, worldWidth * 2 / 3, 0,  worldWidth / 3, BARRIER_THICKNESS,0);
                bottom = new PaddleBarrier(world, 0, 0, worldWidth, BARRIER_THICKNESS, 0);
                top = new PaddleBarrier(world, 0, worldHeight, worldWidth, BARRIER_THICKNESS, 0);
                goal = new Goal(goalTx, world, stage.getViewport().getWorldWidth() / 2 - 1.5f, 0,  3,1.5f, 0);
                break;
            default:
                throw new RuntimeException("wtf mate?");
        }


        paddle = resetPaddle(orientation, assetManager);

        stage.addActor(goal);
        stage.addActor(sideBarrierLeft);
        stage.addActor(sideBarrierRight);
        stage.addActor(goalBarrierLeft);
        stage.addActor(goalBarrierRight);
        stage.addActor(paddle);

        scoringField = new Rectangle();
        scoringField.setWidth(worldWidth);
        scoringField.setHeight(worldHeight);
        scoringField.setPosition(0f, - worldHeight + 1);
    }

    public int update(Puck puck, int score) {
        if (scoringField.contains(puck.getScoringCircle())) {
            score = score + 1;
            return score;

        }
        return score;
    }

    public Paddle resetPaddle(Orientation orientation, AssetManager assetManager) {
        final Paddle paddle = null;
        switch (orientation) {
            case HORIZONTAL:
                //paddle = new Paddle(paddleTx, stage, world, (Puck) puck,stage.getViewport().getWorldWidth() / 2 - 0.5f, 3f, 1, 0);
                paddle.addListener(new DragListener() {
                    public void drag(InputEvent event, float x, float y, int pointer) {
                        paddle.moveBy(x - paddle.getWidth() / 2, y - paddle.getHeight() / 2);
                    }
                });
                return paddle;
            case VERTICAL:
                //paddle = new Paddle(paddleTx, stage, world, (Puck) puck,stage.getViewport().getWorldWidth() / 2 - 0.5f, 3f, 1, 0);
                paddle.addListener(new DragListener() {
                    public void drag(InputEvent event, float x, float y, int pointer) {
                        paddle.moveBy(x - paddle.getWidth() / 2, y - paddle.getHeight() / 2);
                    }
                });
                return paddle;

            default:
                throw new RuntimeException("NO!");
        }
    }

    public void reset() {

    }

}
