package com.example.firstgame;

import android.graphics.Color;

import java.util.HashMap;

public class Colors {

    public static HashMap<Integer, Integer> ALL_COLORS =  new HashMap<Integer, Integer>(){{
        put(1,Color.DKGRAY);
        put(2,Color.GREEN);
        put(3,Color.GRAY);
        put(4,Color.YELLOW);
        put(5,Color.LTGRAY);
        put(6,Color.CYAN);
        put(7,Color.BLACK);
    }};

    public Colors(){}
}

