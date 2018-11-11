package com.lincolnhack.util;

import com.badlogic.gdx.Gdx;

public class SwipeUtil {

    public static boolean nextToEdge(float x, float y){
        return x == Gdx.graphics.getWidth() || y == Gdx.graphics.getHeight() || x == 0 || y == 0;
    }
}