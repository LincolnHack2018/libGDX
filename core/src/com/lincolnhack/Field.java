package com.lincolnhack;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.lincolnhack.data.Direction;
import com.lincolnhack.data.Pair;
import com.lincolnhack.data.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.Getter;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;
import static com.lincolnhack.LibGDX.BARRIER;
import static com.lincolnhack.LibGDX.GOAL_BOTTOM;
import static com.lincolnhack.LibGDX.GOAL_LEFT;
import static com.lincolnhack.LibGDX.GOAL_TOP;
import static com.lincolnhack.LibGDX.PADDLE;
import static com.lincolnhack.data.Direction.BOTTOM;
import static com.lincolnhack.data.Direction.LEFT;
import static com.lincolnhack.data.Direction.RIGHT;
import static com.lincolnhack.data.Direction.TOP;

@Data
public class Field {
    List<Vector2> points;

    public Field(Map<Direction, List<Pair<Float>>> sides, float fieldWidth, float fieldHeight) {
        points = new ArrayList<>();

        List<Pair<Float>> list = sides.get(BOTTOM);
        points.add(new Vector2(0, 0));
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Pair<Float> pair = list.get(i);
                pair = getNormalisedValues(pair, fieldWidth);
                if(null != pair) {
                    points.add(new Vector2(pair.getFirst(), 0));
                    points.add(new Vector2(pair.getSecond(), 0));
                }
            }
        }
        points.add(new Vector2(fieldWidth, 0));

        list = sides.get(RIGHT);
        points.add(new Vector2(fieldWidth, 0));
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Pair<Float> pair = list.get(i);
                pair = getNormalisedValues(pair, fieldHeight);
                if(null != pair) {
                    points.add(new Vector2(fieldWidth , pair.getFirst()));
                    points.add(new Vector2(fieldWidth , pair.getSecond()));
                }
            }
        }
        points.add(new Vector2(fieldWidth, fieldHeight));

        list = sides.get(TOP);
        points.add(new Vector2(0, fieldHeight));
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Pair<Float> pair = list.get(i);
                pair = getNormalisedValues(pair, fieldWidth);
                if(null != pair) {
                    points.add(new Vector2(pair.getFirst(), fieldHeight));
                    points.add(new Vector2(pair.getSecond(), fieldHeight));
                }
            }
        }
        points.add(new Vector2(fieldWidth, fieldHeight));


        list = sides.get(LEFT);
        points.add(new Vector2(0, 0));
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Pair<Float> pair = list.get(i);
                pair = getNormalisedValues(pair, fieldHeight);
                if(null != pair) {
                    points.add(new Vector2(0, pair.getFirst()));
                    points.add(new Vector2(0, pair.getSecond()));
                }
            }
        }
        points.add(new Vector2(0, fieldHeight));

    }

    private Pair<Float> getNormalisedValues(Pair<Float> pair, float maxValue) {
        if (pair.getFirst() >= 0 && pair.getFirst() <= maxValue
                && pair.getSecond() >= 0 && pair.getSecond() <= maxValue) {
            return pair;
        } else if (pair.getFirst() < 0 && pair.getSecond() >= 0) {
            return new Pair<>(0f, pair.getSecond());
        } else if (pair.getFirst() >= 0 && pair.getSecond() < 0) {
            return new Pair<>(pair.getFirst(), 0f);
        } else if (pair.getFirst() > maxValue && pair.getSecond() <= maxValue) {
            return new Pair<>(maxValue, pair.getSecond());
        } else if (pair.getFirst() <= maxValue && pair.getSecond() > maxValue) {
            return new Pair<>(pair.getFirst(), maxValue);
        }
        return null;
    }

    public void draw(ShapeRenderer shapeRenderer, Color color) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(color);
        for (int i = 0; i + 1 < points.size(); i+=2) {
            shapeRenderer.line(points.get(i), points.get(i+1));
        }
        shapeRenderer.end();
    }

}
