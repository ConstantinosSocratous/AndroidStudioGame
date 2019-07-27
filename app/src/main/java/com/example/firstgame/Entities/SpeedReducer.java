package com.example.firstgame.Entities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.firstgame.MyHandler;

public class SpeedReducer extends Entity {

    public SpeedReducer(int x, int y, int width, int height, MyHandler myHandler){
        super(x,y,width,height, myHandler);
    }

    public void update(){
        y += 15;
    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0,0,0));

        canvas.drawRect(x,y,x+width,y+height,paint);
    }
}
