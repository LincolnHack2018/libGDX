package com.lincolnhack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Field {

    private static final float BARRIER_THICKNESS = 0.25f;

    World world;
    Stage stage;

    Barrier sideBarrierLeft;
    Barrier sideBarrierRight;
    Barrier goalBarrierLeft;
    Barrier goalBarrierRight;

    public Field(Orientation orientation, World world, Stage stage, Texture barrierTx) {
        this.world = world;
        float worldHeight = stage.getViewport().getWorldHeight();
        float worldWidth = stage.getViewport().getWorldWidth();
        switch (orientation) {
            case HORIZONTAL:

                break;
            case VERTIVAL:
                sideBarrierLeft = new Barrier(barrierTx, world, 0, 0, BARRIER_THICKNESS, worldHeight,0);
                sideBarrierRight = new Barrier(barrierTx, world, worldWidth - BARRIER_THICKNESS, 0, BARRIER_THICKNESS, worldHeight,0);
                goalBarrierLeft = new Barrier(barrierTx, world, 0, 0,  worldWidth / 3, BARRIER_THICKNESS ,0);
                goalBarrierRight = new Barrier(barrierTx, world, worldWidth * 2 / 3, 0,  worldWidth / 3, BARRIER_THICKNESS,0);
                break;
                default:
                    throw new RuntimeException("wtf mate?");
        }


        stage.addActor(sideBarrierLeft);
        stage.addActor(sideBarrierRight);
        stage.addActor(goalBarrierLeft);
        stage.addActor(goalBarrierRight);
    }

}
