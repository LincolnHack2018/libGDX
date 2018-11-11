package com.lincolnhack.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Device {
    private String id;
    private float touchDownX;
    private float touchDownY;
    private float touchUpX;
    private float touchUpY;
    private float deviceWidth;
    private float deviceHeight;
}
