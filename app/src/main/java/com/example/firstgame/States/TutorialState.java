package com.example.firstgame.States;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.firstgame.Entities.Entity;
import com.example.firstgame.Entities.Obstacle;
import com.example.firstgame.Entities.Player;
import com.example.firstgame.Entities.SideWall;
import com.example.firstgame.Handler;
import com.example.firstgame.Input;
import com.example.firstgame.ObstacleHandler;

import java.util.ArrayList;

public class TutorialState extends  State {

    private int numOfTicks;
    private Player player;


    public TutorialState(Handler handler) {
        super(handler);
        init();
    }

    private void init(){
        numOfTicks = 0;
        player = new Player((int)(handler.getWidth() * 0.45),(int)(handler.getHeight() * 0.80),(int)(handler.getWidth() * 0.075),(int)(handler.getHeight() * 0.045),handler);
        handler.addPlayer(player);
        handler.addEntity(new SideWall(0,0,(int)(handler.getWidth()*GameState.sideWallWidth),(int)(handler.getHeight()), handler));
        handler.addEntity(new SideWall((int)(handler.getWidth()*(0.95)),0,(int)(handler.getWidth()*(GameState.sideWallWidth)),(int)(handler.getHeight()),handler));

    }

    @Override
    public void update() {
        if(numOfTicks > 1250){
            this.handler.initEntities();
            this.handler.getGamePanel().setTutorialToSeen("1");
            this.handler.getGamePanel().setCurrentState(this.handler.getGamePanel().getGameState());
        }

        for(Entity e: this.handler.getEntities()){
            e.update();
        }

        if(player.isCollisionWithObstacle()){
            handler.getGamePanel().vibrate(300);
        }
        numOfTicks++;


    }

    @Override
    public void draw(Canvas canvas) {
        for(Entity e: this.handler.getEntities()){
            e.draw(canvas);
        }

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(70);



        if(isBetween(numOfTicks,10,130)){
            paint.setTextSize(80);
            canvas.drawText("Welcome!", (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.30), paint);
        }
        if(isBetween(numOfTicks,135, 350)){
            paint.setTextSize(70);
            canvas.drawText("Try clicking", (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.30), paint);
            canvas.drawText("left or right", (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.37), paint);
            canvas.drawText("to change the direction", (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.44), paint);
            canvas.drawText("of the square", (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.52), paint);
        }
        if(isBetween(numOfTicks,360, 560)){
            paint.setTextSize(70);
            canvas.drawText("Now click and hold", (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.30), paint);
            canvas.drawText("left or right", (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.37), paint);
            canvas.drawText("to stick to the walls", (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.44), paint);
        }
        if(isBetween(numOfTicks,570, 650)){
            paint.setTextSize(70);
            canvas.drawText("Now avoid the obstacles", (int) (handler.getWidth() * 0.10), (int)(handler.getHeight() * 0.10), paint);
        }

        if(numOfTicks ==  550){
            handler.addEntity(new Obstacle((int)(handler.getWidth()*0.20) , (int)(-300), (int) (handler.getWidth() * (ObstacleHandler.dimension)),
                    (int) (handler.getHeight() * (ObstacleHandler.dimension)), handler, 10,1));
        }

        if(numOfTicks ==  750){
            handler.addEntity(new Obstacle((int)(handler.getWidth()*0.80) , (int)(-300), (int) (handler.getWidth() * (ObstacleHandler.dimension)),
                    (int) (handler.getHeight() * (ObstacleHandler.dimension)), handler, 15,1));
        }

        if(numOfTicks ==  850){
            handler.addEntity(new Obstacle((int)(handler.getWidth()*0.50) , (int)(-300), (int) (handler.getWidth() * (ObstacleHandler.dimension)),
                    (int) (handler.getHeight() * (ObstacleHandler.dimension)), handler, 15,1));
        }

        if(numOfTicks ==  940){
            handler.addEntity(new Obstacle((int)(handler.getWidth()*0.20) , (int)(-300), (int) (handler.getWidth() * (ObstacleHandler.dimension)),
                    (int) (handler.getHeight() * (ObstacleHandler.dimension)), handler, 12,1));
        }

        if(isBetween(numOfTicks,1070, 1250)){
            paint.setTextSize(70);
            canvas.drawText("Good job", (int) (handler.getWidth() * 0.10), (int)(handler.getHeight() * 0.30), paint);
            canvas.drawText("Your are ready to play", (int) (handler.getWidth() * 0.10), (int)(handler.getHeight() * 0.37), paint);
        }
    }

    private boolean isBetween(int num,int a, int b){
        if(num >= a && num <= b) return true;
        else return  false;

    }
}
