package com.example.firstgame;

import android.graphics.Color;

import com.example.firstgame.Entities.Entity;
import com.example.firstgame.Entities.Obstacle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ObstacleHandler {

    private Handler handler;
    private int roadSize = 3;
    private Random random;
    private HashMap<Integer, Boolean> roads;
    private float dimension = 0.10f;


    public ObstacleHandler(Handler handler){
        this.handler = handler;
        random = new Random();
        roads  =  new HashMap<Integer,Boolean>();

        initRoads();
    }

    /**
     * Handles the creation of the obstacles
     */
    public void createObstacle(){
        int widthTemp = handler.getWidth();
        float roadWidth = (float)((widthTemp*0.90)/roadSize);
        float roadWidthPercentage = (roadWidth/widthTemp);
        int bound = (int)(roadWidth - (widthTemp*0.10));

        for(Integer num : roads.keySet()){
            if(! roads.get(num)){ //If the road is empty

                int speed = random.nextInt(30) + 10;
                int x = random.nextInt((int) (bound)) + (int)((num-1)*(widthTemp* roadWidthPercentage))
                        + (int) (widthTemp * 0.05);

                int y = -(int) (handler.getHeight() * (0.20));

                handler.addEntity(new Obstacle(x , y, (int) (handler.getWidth() * (dimension)),
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

    public void initRoads(){
        for(int i=1; i<=roadSize; i++){
            roads.put(i,false);
        }
    }

    public int getRoadSize(){
        return roadSize;
    }

    public void increaseRoadSize(){
        roadSize++;
        roads.put(roadSize,false);
    }





}
