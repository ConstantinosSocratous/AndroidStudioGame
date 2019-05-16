package com.example.firstgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Button {

    private int x,y,width,height;

    public Button(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Draw the entity on the canvas
     * @param canvas
     */
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);

        canvas.drawRect(x,y,x+width,y+height,paint);
    }

    public boolean isClicked(){
        Rect temp = new Rect(Input.x, Input.y,Input.x + 3,Input.y + 3);
        Rect thisRect = new Rect(x,y,x+width,y+height);
        if(thisRect.intersect(temp)) {
            return true;
        }
        return false;
    }
}
