package com.lincolnhack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.lincolnhack.data.Direction;
import com.lincolnhack.data.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

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
        points.add(new Vector2(0, 1));
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Pair<Float> pair = list.get(i);
                pair = getNormalisedValues(pair, fieldWidth);
                if(null != pair) {
                    points.add(new Vector2(pair.getFirst(), 1));
                    points.add(new Vector2(pair.getSecond(), 1));
                }
            }
        }
        points.add(new Vector2(fieldWidth, 1));

        list = sides.get(RIGHT);
        points.add(new Vector2(fieldWidth-1, 0));
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Pair<Float> pair = list.get(i);
                pair = getNormalisedValues(pair, fieldHeight);
                if(null != pair) {
                    points.add(new Vector2(fieldWidth -1, pair.getFirst()));
                    points.add(new Vector2(fieldWidth -1, pair.getSecond()));
                }
            }
        }
        points.add(new Vector2(fieldWidth-1, fieldHeight));

        list = sides.get(TOP);
        points.add(new Vector2(0, fieldHeight-1));
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Pair<Float> pair = list.get(i);
                pair = getNormalisedValues(pair, fieldWidth);
                if(null != pair) {
                    points.add(new Vector2(pair.getFirst(), fieldHeight-1));
                    points.add(new Vector2(pair.getSecond(), fieldHeight-1));
                }
            }
        }
        points.add(new Vector2(fieldWidth, fieldHeight-1));


        list = sides.get(LEFT);
        points.add(new Vector2(1, 0));
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Pair<Float> pair = list.get(i);
                pair = getNormalisedValues(pair, fieldHeight);
                if(null != pair) {
                    points.add(new Vector2(1, pair.getFirst()));
                    points.add(new Vector2(1, pair.getSecond()));
                }
            }
        }
        points.add(new Vector2(1, fieldHeight));

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

            shapeRenderer.rectLine(points.get(i), points.get(i+1), 10);
        }
        shapeRenderer.end();
    }

}
