package com.example.firstgame;

import android.graphics.Color;

import java.util.HashMap;

public class Colors {

    public static HashMap<Integer, Integer> ALL_COLORS =  new HashMap<Integer, Integer>(){{
        //Color.parseColor("#C0C0C0")
        put(1,Color.GRAY);
        put(2,Color.rgb(160,00,55));
        put(3,Color.rgb(34,98,12));
        put(4,Color.rgb(32, 98, 98));
        put(5,Color.rgb(102, 0, 102));
        put(6,Color.rgb(102, 51, 0));
    }};

    public Colors(){}
}

