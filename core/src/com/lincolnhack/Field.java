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

        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Pair<Float> pair = list.get(i);
                if(pair.getFirst() > pair.getSecond()){
                    points.add(new Vector2(fieldWidth, 0));
                }else{
                    points.add(new Vector2(0, 0));
                }
                pair = getNormalisedValues(pair, fieldWidth);
                if(null != pair) {
                    points.add(new Vector2(pair.getFirst(), 0));
                    points.add(new Vector2(pair.getSecond(), 0));
                }
                if(pair.getFirst() > pair.getSecond()){
                    points.add(new Vector2(0, 0));
                }else{
                    points.add(new Vector2(fieldWidth, 0));
                }
            }
        }
        else{
            points.add(new Vector2(0, 0));
            points.add(new Vector2(fieldWidth, 0));
        }

        list = sides.get(RIGHT);

        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Pair<Float> pair = list.get(i);
                if(pair.getFirst() > pair.getSecond()){
                    points.add(new Vector2(fieldWidth, fieldHeight));
                }else{
                    points.add(new Vector2(fieldWidth, 0));
                }
                pair = getNormalisedValues(pair, fieldHeight);
                if(null != pair) {
                    points.add(new Vector2(fieldWidth , pair.getFirst()));
                    points.add(new Vector2(fieldWidth , pair.getSecond()));
                }
                if(pair.getFirst() > pair.getSecond()){
                    points.add(new Vector2(fieldWidth, 0));
                }else{
                    points.add(new Vector2(fieldWidth, fieldHeight));
                }
            }
        }
        else{
            points.add(new Vector2(fieldWidth, 0));
            points.add(new Vector2(fieldWidth, fieldHeight));
        }

        list = sides.get(TOP);

        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Pair<Float> pair = list.get(i);
                if(pair.getFirst() > pair.getSecond()){
                    points.add(new Vector2(fieldWidth, fieldHeight));
                }else{
                    points.add(new Vector2(0, 0));
                }
                pair = getNormalisedValues(pair, fieldWidth);
                if(null != pair) {
                    points.add(new Vector2(pair.getFirst(), fieldHeight));
                    points.add(new Vector2(pair.getSecond(), fieldHeight));
                }
                if(pair.getFirst() > pair.getSecond()){
                    points.add(new Vector2(0, 0));
                }else{
                    points.add(new Vector2(fieldWidth, fieldHeight));
                }
            }
        }else {
            points.add(new Vector2(0, fieldHeight));
            points.add(new Vector2(fieldWidth, fieldHeight));
        }


        list = sides.get(LEFT);

        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Pair<Float> pair = list.get(i);
                if(pair.getFirst() > pair.getSecond()){
                    points.add(new Vector2(0, fieldHeight));
                }else{
                    points.add(new Vector2(0, pair.getFirst()));
                }
                pair = getNormalisedValues(pair, fieldHeight);
                if(null != pair) {
                    points.add(new Vector2(0, pair.getFirst()));
                    points.add(new Vector2(0, pair.getSecond()));
                }
                if(pair.getFirst() > pair.getSecond()){
                    points.add(new Vector2(0, 0));
                }else{
                    points.add(new Vector2(0, fieldHeight));
                }
            }
        }else{
            points.add(new Vector2(0, 0));
            points.add(new Vector2(0, fieldHeight));
        }

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
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        for (int i = 0; i + 1 < points.size(); i+=2) {

            shapeRenderer.rectLine(points.get(i), points.get(i+1), 0.5f);
        }
        shapeRenderer.end();
    }

}
