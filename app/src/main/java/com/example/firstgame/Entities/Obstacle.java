package com.example.firstgame.Entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.firstgame.Colors;
import com.example.firstgame.MyHandler;

import java.util.Random;

public class Obstacle extends Entity {

    private int speed;
    private int color;
    private int road;

    public Obstacle(int x, int y, int width, int height, MyHandler myHandler, int speed, int road, int color) {
        super(x, y, width, height, myHandler);
        this.color = color;
        //color = pickRandomColor();
        this.speed = speed;
        this.road = road;
    }

    /**
     * Update the properties
     * of the entity
     */
    public void update(){
        if(!canMove) return;

        y += speed;

        if(y > myHandler.getHeight()){
            toBeRemoved = true;
        }

    }

    /**
     * Draw the entity on the canvas
     * @param canvas
     */
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(color);

        //canvas.drawCircle(x,y,width,paint);
        canvas.drawRect(x,y,x+width,y+height,paint);
    }

    private int pickRandomColor(){
        Random rand = new Random();

        int num = rand.nextInt(Colors.ALL_COLORS.size())+1;
        return Colors.ALL_COLORS.get(num);
    }

    public int getRoad(){
        return road;
    }
}
