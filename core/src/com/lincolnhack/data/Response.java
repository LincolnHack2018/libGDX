package com.lincolnhack.data;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Response {
    private String id;
    private ArrayList<Pair<Float>> intersectDistances;
    private Direction direction;
    private boolean mainDevice;
    private float screenOneX;
    private float screenOneY;
    private float screenTwoX;
    private float screenTwoY;
}
