package com.lincolnhack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Field {

    World world;
    Stage stage;

    Barrier sideBarrierLeft;
    Barrier sideBarrierRight;
    Barrier goalBarrierLeft;
    Barrier goalBarrierRight;

    public Field(float width, float height, Orientation orientation, World world, Stage stage, Texture barrierTx) {
        this.world = world;

        switch (orientation) {
            case HORIZONTAL:

                break;
            case VERTIVAL:
                sideBarrierLeft = new Barrier(barrierTx, world, 0, 0, 32, stage.getViewport().getScreenHeight(),0);
                sideBarrierRight = new Barrier(barrierTx, world, stage.getViewport().getScreenWidth() - 32, 0, 32, stage.getViewport().getScreenHeight(),0);
                goalBarrierLeft = new Barrier(barrierTx, world, -16, 0,  stage.getViewport().getScreenWidth()/4, 32,0);
                goalBarrierRight = new Barrier(barrierTx, world, stage.getViewport().getScreenWidth() * 3 / 4 - 16, 0,  stage.getViewport().getScreenWidth()/4, 32,0);
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
