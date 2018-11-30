package com.lincolnhack;

public class Filters {
    public static final short BOX2D_FILTER_PUCK = 0x0001;
    public static final short BOX2D_FILTER_PADDLE = 0x0002;
    public static final short BOX2D_FILTER_PLAYER_PENETRABLE_BARRIER = 0x0004;
    public static final short BOX2D_FILTER_IMPENETRABLE_BARRIER = 0x0008;

    public static final short MASK_PUCK = BOX2D_FILTER_PADDLE | BOX2D_FILTER_IMPENETRABLE_BARRIER;
    public static final short MASK_PADDLE = BOX2D_FILTER_PUCK | BOX2D_FILTER_PLAYER_PENETRABLE_BARRIER | BOX2D_FILTER_IMPENETRABLE_BARRIER;
    public static final short MASK_PENETRABLE_BARRIER = BOX2D_FILTER_PADDLE | BOX2D_FILTER_IMPENETRABLE_BARRIER;
    public static final short MASK_IMPENETRABLE_BARRIER = -1;

}
