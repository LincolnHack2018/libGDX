package com.lincolnhack.data;

import lombok.Data;

@Data
public class Response {
    private String id;
    private float intersectX;
    private float intersectY;
    private float intersectPlus;
    private float intersectMinus;
    private Direction direction;
}
