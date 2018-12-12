package com.lincolnhack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class Debug {

    public static final float BOUNDARY = 1f;

    public static void drawBoundaries(ShapeRenderer shapeRenderer, Stage stage) {
        float worldWidth = stage.getViewport().getWorldWidth();
        float worldHeight = stage.getViewport().getWorldHeight();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.line(new Vector2(BOUNDARY, 0), new Vector2(BOUNDARY, worldHeight));
        shapeRenderer.line(new Vector2(0, BOUNDARY), new Vector2(worldWidth, BOUNDARY));

        shapeRenderer.line(new Vector2(0, worldHeight - BOUNDARY), new Vector2(worldWidth, worldHeight - BOUNDARY));
        shapeRenderer.line(new Vector2(worldWidth - BOUNDARY, 0), new Vector2(worldWidth - BOUNDARY, worldHeight));
        shapeRenderer.end();
    }
}
