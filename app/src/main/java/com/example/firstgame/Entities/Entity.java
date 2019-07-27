package com.example.firstgame.Entities;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.firstgame.MyHandler;

public class Entity {

    protected int x,y,width,height;
    protected MyHandler myHandler;
    protected boolean toBeRemoved = false;
    protected boolean canMove = true;

    public Entity(int x, int y, int width, int height, MyHandler myHandler){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.myHandler = myHandler;
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
    public void draw(Canvas canvas){}

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() { return x; }

    public void setX(int x) { this.x = x; }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Rect getRectangle(){
        return new Rect(x,y,x+width,y+height);
    }

    public boolean isToBeRemoved(){return toBeRemoved;}

    public void setCanMove(boolean bool){
        this.canMove = bool;
    }
}
