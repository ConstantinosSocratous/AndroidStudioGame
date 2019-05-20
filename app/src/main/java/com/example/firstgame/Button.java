package com.example.firstgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


public class Button {

    private int x,y,width,height;
    private Handler handler;
    private int img;

    public Button(int x, int y, int width, int height, Handler handler, int img){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.handler = handler;
        this.img = img;
    }

    /**
     * Draw the entity on the canvas
     * @param canvas
     */
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);

        Bitmap bmp = BitmapFactory.decodeResource(handler.getGamePanel().getResources(),this.img);
        canvas.drawBitmap(bmp, x,y, paint);
    }

    public boolean isClicked(){
        Rect temp = new Rect(Input.x, Input.y,Input.x + 3,Input.y + 3);
        Rect thisRect = new Rect(x,y,x+width,y+height);
        if(thisRect.intersect(temp)) {
            return true;
        }
        return false;
    }

    public void setImg(int num){
        this.img = num;
    }
}
