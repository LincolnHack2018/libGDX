package com.lincolnhack;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import lombok.Data;
import lombok.Getter;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;
import static com.lincolnhack.LibGDX.BARRIER;
import static com.lincolnhack.LibGDX.GOAL_BOTTOM;
import static com.lincolnhack.LibGDX.GOAL_LEFT;
import static com.lincolnhack.LibGDX.GOAL_TOP;
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

    public Field(float x, float y, Orientation orientation, World world, Stage stage, AssetManager assetManager, Puck puck) {
        this.world = world;
        this.stage = stage;
        float worldHeight = stage.getViewport().getWorldHeight();
        float worldWidth = stage.getViewport().getWorldWidth();

        Texture barrierTx = assetManager.get(BARRIER);
        barrierTx.setWrap(Repeat, Repeat);

        switch (orientation) {
            case HORIZONTAL_LEFT:
                sideBarrierLeft = new Barrier(barrierTx, world, x, y + worldHeight - BARRIER_THICKNESS, worldWidth, BARRIER_THICKNESS,0);
                sideBarrierRight = new Barrier(barrierTx, world, x, y, worldWidth, BARRIER_THICKNESS,0);
                goalBarrierLeft = new Barrier(barrierTx, world, x, y + (worldHeight * 2 / 3),  BARRIER_THICKNESS,worldHeight / 3,0);
                goalBarrierRight = new Barrier(barrierTx, world, x, y, BARRIER_THICKNESS, worldHeight / 3,0);
                bottom = new PaddleBarrier(world, x, y, BARRIER_THICKNESS, worldHeight, 0);
                top = new PaddleBarrier(world, x+ worldWidth, y, BARRIER_THICKNESS, worldHeight, 0);
                goal = new Goal(assetManager.get(GOAL_LEFT), world, x, y + (worldHeight / 3),  3,worldHeight/3, 0);
                break;
            case VERTICAL_BOTTOM:
                sideBarrierLeft = new Barrier(barrierTx, world, x, y, BARRIER_THICKNESS, worldHeight,0);
                sideBarrierRight = new Barrier(barrierTx, world, x + (worldWidth - BARRIER_THICKNESS), y, BARRIER_THICKNESS, worldHeight,0);
                goalBarrierLeft = new Barrier(barrierTx, world, x, y,  worldWidth / 3, BARRIER_THICKNESS ,0);
                goalBarrierRight = new Barrier(barrierTx, world, x + (worldWidth * 2 / 3), y,  worldWidth / 3, BARRIER_THICKNESS,0);
                bottom = new PaddleBarrier(world, x, y, worldWidth, BARRIER_THICKNESS, 0);
                top = new PaddleBarrier(world, x, y + worldHeight, worldWidth, BARRIER_THICKNESS, 0);
                goal = new Goal(assetManager.get(GOAL_BOTTOM), world, x + (worldWidth / 3), y,  worldWidth/3,1.5f, 0);
                break;
            case VERTICAL_TOP:
                sideBarrierLeft = new Barrier(barrierTx, world, x, y, BARRIER_THICKNESS, worldHeight,0);
                sideBarrierRight = new Barrier(barrierTx, world, x + worldWidth - BARRIER_THICKNESS, y, BARRIER_THICKNESS, worldHeight,0);
                goalBarrierLeft = new Barrier(barrierTx, world, x, y + worldHeight - BARRIER_THICKNESS,  worldWidth / 3, BARRIER_THICKNESS ,0);
                goalBarrierRight = new Barrier(barrierTx, world, x + (worldWidth * 2 / 3), y + worldHeight - BARRIER_THICKNESS,  worldWidth / 3, BARRIER_THICKNESS,0);
                bottom = new PaddleBarrier(world, x, y, worldWidth, BARRIER_THICKNESS, 0);
                top = new PaddleBarrier(world, x, y + worldHeight, worldWidth, BARRIER_THICKNESS, 0);
                goal = new Goal(assetManager.get(GOAL_TOP), world, x + (worldWidth / 3), y + worldHeight - 1.5f,  worldWidth/3,1.5f, 0);
                break;
            default:
                throw new RuntimeException("wtf mate?");
        }



        stage.addActor(goal);
        stage.addActor(sideBarrierLeft);
        stage.addActor(sideBarrierRight);
        stage.addActor(goalBarrierLeft);
        stage.addActor(goalBarrierRight);

        scoringField = createScoringField(orientation, worldWidth, worldHeight);
    }

    public int update(Puck puck, int score) {
        if (scoringField.contains(puck.getScoringCircle())) {

            score = score + 1;
            return score;

        }
        return score;
    }

    public Rectangle createScoringField(Orientation orientation, float worldWidth, float worldHeight) {
        Rectangle scoringField = new Rectangle();
        switch (orientation) {
            case HORIZONTAL_LEFT:
                scoringField.setWidth(worldWidth);
                scoringField.setHeight(worldHeight);
                scoringField.setPosition(0f, -worldWidth + 1);
                break;
            case VERTICAL_BOTTOM:
                scoringField.setWidth(worldWidth);
                scoringField.setHeight(worldHeight);
                scoringField.setPosition(0f, -worldHeight + 1);
                break;
            case VERTICAL_TOP:
                scoringField.setWidth(worldWidth);
                scoringField.setHeight(worldHeight);
                scoringField.setPosition(0f, worldHeight - 1);
                break;
            default:
                throw new RuntimeException("NO!");

        }
        return scoringField;
    }



    public void reset() {

    }

}
