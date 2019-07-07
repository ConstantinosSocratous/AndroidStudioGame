package com.example.firstgame.Entities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.inputmethod.CursorAnchorInfo;

import com.example.firstgame.Handler;
import com.example.firstgame.Input;

import java.util.ArrayList;

public class Player extends Entity {

    private int speed = 19;
    private boolean directionRight = true;

    public Player(int x, int y, int width, int height, Handler handler){
        super(x,y,width,height,handler);
    }

    /**
     * Update the properties of the entity
     */
    public void update(){
        if(!canMove) return;    //Check if can move

        if(isCollisionWithObstacle() && handler.getGamePanel().getCurrentState().equals(this.handler.getGamePanel().getGameState())){  //Check if collision with obstacle
            handler.getGamePanel().getGameState().setGameOver();
            return;
        }

        if(Input.left && Input.isClicked){  //Take input
            directionRight = false;
        }else if(!Input.left && Input.isClicked){
            directionRight = true;
        }

        //---------------CHECK COLLISION WITH SIDEWALLS--------------------\\
        if(isCollisionWithSideWalls() && Input.isClicked && directionRight && (x > handler.getWidth()/2)){
            return;
        }
        if(isCollisionWithSideWalls() && Input.isClicked && !directionRight && (x < handler.getWidth()/2)){
            return;
        }

        if(isCollisionWithSideWalls() && Input.isClicked && directionRight){
            // DO NOTHING
        }else if(isCollisionWithSideWalls() && Input.isClicked && !directionRight){
            // DO NOTHING
        }else if(isCollisionWithSideWalls()){ //Check collision with side walls
            directionRight = !directionRight;
        }
        //--------------------------------------------------------------\\

        if(directionRight){
            x+=speed;
        }else{
            x-=speed;
        }

    }

    /**
     * Return true if there is collision with SideWall
     * @return Boolean
     */
    private boolean isCollisionWithSideWalls(){
        ArrayList<Entity> entities = handler.getEntities();
        for(Entity entity: entities){
            if(entity instanceof SideWall){
              if(entity.getRectangle().intersect(this.getRectangle()))
             return true;
            }
        }
        return false;
    }

    /**
     * Return true if there is collision with Obstacle
     * @return
     */
    public boolean isCollisionWithObstacle(){
        ArrayList<Entity> entities = handler.getEntities();
        for(Entity entity: entities){
            if(entity instanceof Obstacle){
                if(entity.getRectangle().intersect(this.getRectangle()))
                    return true;
            }
        }
        return false;
    }

    /**
     * Draw the entity on the canvas
     * @param canvas
     */
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.rgb(12,40,120));

        canvas.drawRect(x,y,x+width,y+height,paint);
    }

    public void setSpeed(int num){
        this.speed = num;
    }
    public int getSpeed(){
        return speed;
    }
}
