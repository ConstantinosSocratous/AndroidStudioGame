package com.example.firstgame.States;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.firstgame.Colors;
import com.example.firstgame.Entities.Entity;
import com.example.firstgame.Entities.Obstacle;
import com.example.firstgame.Entities.Player;
import com.example.firstgame.Entities.SideWall;
import com.example.firstgame.MyHandler;
import com.example.firstgame.ObstacleHandler;

public class TutorialState extends  State {

    private int numOfTicks;
    private Player player;


    public TutorialState(MyHandler myHandler) {
        super(myHandler);
        init();
    }

    private void init(){
        numOfTicks = 0;
        player = new Player((int)(myHandler.getWidth() * 0.45),(int)(myHandler.getHeight() * 0.80),(int)(myHandler.getWidth() * 0.075),(int)(myHandler.getHeight() * 0.045), myHandler);
        myHandler.addPlayer(player);
        myHandler.addEntity(new SideWall(0,0,(int)(myHandler.getWidth()*GameState.sideWallWidth),(int)(myHandler.getHeight()), myHandler));
        myHandler.addEntity(new SideWall((int)(myHandler.getWidth()*(0.95)),0,(int)(myHandler.getWidth()*(GameState.sideWallWidth)),(int)(myHandler.getHeight()), myHandler));

    }

    @Override
    public void update() {
        if(numOfTicks > 1250){
            this.myHandler.initEntities();
            this.myHandler.getGamePanel().setTutorialToSeen("1");
            this.myHandler.getGamePanel().setCurrentState(this.myHandler.getGamePanel().getGameState());
        }

        for(Entity e: this.myHandler.getEntities()){
            e.update();
        }

        if(player.isCollisionWithObstacle()){
            myHandler.getGamePanel().vibrate(300);
        }
        numOfTicks++;


    }

    @Override
    public void draw(Canvas canvas) {
        for(Entity e: this.myHandler.getEntities()){
            e.draw(canvas);
        }

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(70);



        if(isBetween(numOfTicks,10,130)){
            paint.setTextSize(80);
            canvas.drawText("Welcome!", (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.30), paint);
        }
        if(isBetween(numOfTicks,135, 350)){
            paint.setTextSize(70);
            canvas.drawText("Try clicking", (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.30), paint);
            canvas.drawText("left or right", (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.37), paint);
            canvas.drawText("to change the direction", (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.44), paint);
            canvas.drawText("of the square", (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.52), paint);
        }
        if(isBetween(numOfTicks,360, 560)){
            paint.setTextSize(70);
            canvas.drawText("Now click and hold", (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.30), paint);
            canvas.drawText("left or right", (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.37), paint);
            canvas.drawText("to stick to the walls", (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.44), paint);
        }
        if(isBetween(numOfTicks,570, 650)){
            paint.setTextSize(70);
            canvas.drawText("Now avoid the obstacles", (int) (myHandler.getWidth() * 0.10), (int)(myHandler.getHeight() * 0.10), paint);
        }

        if(numOfTicks ==  550){
            myHandler.addEntity(new Obstacle((int)(myHandler.getWidth()*0.20) , (int)(-300), (int) (myHandler.getWidth() * (ObstacleHandler.dimension)),
                    (int) (myHandler.getHeight() * (ObstacleHandler.dimension)), myHandler, 10,1, Colors.ALL_COLORS.get(1)));
        }

        if(numOfTicks ==  750){
            myHandler.addEntity(new Obstacle((int)(myHandler.getWidth()*0.80) , (int)(-300), (int) (myHandler.getWidth() * (ObstacleHandler.dimension)),
                    (int) (myHandler.getHeight() * (ObstacleHandler.dimension)), myHandler, 15,1, Colors.ALL_COLORS.get(1)));
        }

        if(numOfTicks ==  850){
            myHandler.addEntity(new Obstacle((int)(myHandler.getWidth()*0.50) , (int)(-300), (int) (myHandler.getWidth() * (ObstacleHandler.dimension)),
                    (int) (myHandler.getHeight() * (ObstacleHandler.dimension)), myHandler, 15,1,Colors.ALL_COLORS.get(1)));
        }

        if(numOfTicks ==  940){
            myHandler.addEntity(new Obstacle((int)(myHandler.getWidth()*0.20) , (int)(-300), (int) (myHandler.getWidth() * (ObstacleHandler.dimension)),
                    (int) (myHandler.getHeight() * (ObstacleHandler.dimension)), myHandler, 12,1,Colors.ALL_COLORS.get(1)));
        }

        if(isBetween(numOfTicks,1070, 1250)){
            paint.setTextSize(70);
            canvas.drawText("Good job", (int) (myHandler.getWidth() * 0.10), (int)(myHandler.getHeight() * 0.30), paint);
            canvas.drawText("Your are ready to play", (int) (myHandler.getWidth() * 0.10), (int)(myHandler.getHeight() * 0.37), paint);
        }
    }

    private boolean isBetween(int num,int a, int b){
        if(num >= a && num <= b) return true;
        else return  false;

    }
}
