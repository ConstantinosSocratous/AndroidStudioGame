package com.example.firstgame.Entities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.firstgame.Handler;

public class SideWall extends Entity {

    public SideWall(int x, int y, int width, int height, Handler handler){
        super(x,y,width,height,handler);
    }

    /**
     * Update the properties
     * of the entity
     */
    public void update(){}

    /**
     * Draw the entity on the canvas
     * @param canvas
     */
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawRect(x,y,x+width,y+height,paint);
    }
}
