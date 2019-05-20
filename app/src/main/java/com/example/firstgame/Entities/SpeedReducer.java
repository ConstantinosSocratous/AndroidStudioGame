package com.example.firstgame.Entities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.firstgame.Handler;

public class SpeedReducer extends Entity {

    public SpeedReducer(int x, int y, int width, int height, Handler handler){
        super(x,y,width,height,handler);
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
