package com.lincolnhack.objects;

import lombok.Data;

@Data
public class Touch {

    float x;
    float y;
    boolean down;
    boolean up;
    boolean dragged;

    public Touch() {
        x = 0f;
        y = 0f;
        down = false;
        up = false;
        dragged = false;
    }
}
