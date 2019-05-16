package com.example.firstgame;

import android.graphics.Color;

import com.example.firstgame.Entities.Entity;
import com.example.firstgame.Entities.Obstacle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ObstacleHandler {

    private int speedUpper = 25, speedLower = 10;
    private Handler handler;
    private int roadSize = 3;   //3-7
    private Random random;
    private HashMap<Integer, Boolean> roads;

    private float dimension = 0.10f;


    public ObstacleHandler(Handler handler){
        this.handler = handler;
        random = new Random();
        roads  =  new HashMap<Integer,Boolean>();

        init();
    }

    /**
     * Handles the creation of the obstacles
     */
    //TODO: IMPROVE
    public void createObstacle(){
        int widthTemp = handler.getWidth();
        float roadWidth = (float)((widthTemp*0.90)/roadSize);
        float roadWidthPercentage = (roadWidth/widthTemp);
        int bound = (int)(roadWidth - (widthTemp*0.10));


        for(Integer num : roads.keySet()){

            if(! roads.get(num)){ //If the road is empty
                int speed = random.nextInt(speedUpper) + speedLower;

                //Calculate X
                int x = random.nextInt((int) (bound)) + (int)((num-1)*(widthTemp* roadWidthPercentage))
                        + (int) (widthTemp * 0.05);

                //Calculate y
                float y = random.nextInt(40) + 10;
                y = y/100;
                y = - (handler.getHeight() * (y));

                handler.addEntity(new Obstacle(x , (int)y, (int) (handler.getWidth() * (dimension)),
                        (int) (handler.getHeight() * (dimension)), handler, speed,num));

                roads.put(num,true);

            }
        }
    }

    public void removeObjects(){
        ArrayList<Entity> temp = new ArrayList<>();
        for(Entity entity: handler.getEntities()){
            if(entity.isToBeRemoved()){
                temp.add(entity);
            }
        }

        for(Entity entity: temp){
            roads.put(((Obstacle) entity).getRoad(),false);
            handler.removeEntity(entity);
            handler.getGamePanel().getGameState().increaseScore();  //Increase score for every obstacle that is removed
        }
    }

    public void init(){
        for(int i=1; i<=roadSize; i++){
            roads.put(i,false);
        }
        roadSize = 3;
        speedLower=10;
        speedUpper=25;
    }

    public int getRoadSize(){
        return roadSize;
    }

    public void increaseRoadSize(){
        roadSize++;
        roads.put(roadSize,false);
    }

    public void increaseSpeed(){
        speedLower+=1;
        speedUpper+=5;
    }





}
