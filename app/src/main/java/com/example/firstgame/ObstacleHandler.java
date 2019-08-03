package com.example.firstgame;

import com.example.firstgame.Entities.Entity;
import com.example.firstgame.Entities.Obstacle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class ObstacleHandler {

    private int speedUpper = 18, speedLower = 10;
    private MyHandler myHandler;
    private int roadSize = 3;   //3-7
    private Random random;
    private HashMap<Integer, Boolean> roads;
    private int currentColor;

    public static final float dimension = 0.10f;
    private float obstacleWidthHeight = 0.05f;


    public ObstacleHandler(MyHandler myHandler){
        this.myHandler = myHandler;
        random = new Random();
        roads  =  new HashMap<Integer,Boolean>();

        init();
    }

    /**
     * Handles the creation of the obstacles
     */
    //TODO: IMPROVE CREATION OF OBJECTS
    public void createObstacle(){
        int widthTemp = myHandler.getWidth();
        float roadWidth = (float)((widthTemp*0.90)/roadSize);
        float roadWidthPercentage = (roadWidth/widthTemp);
        int bound = (int)(roadWidth - (widthTemp*obstacleWidthHeight));

        Set<Integer> keyset = roads.keySet();
        for(Integer num : keyset){

            if(! roads.get(num)){ //If the road is empty
                int speed = random.nextInt(speedUpper) + speedLower;

                //Calculate X
                int x = random.nextInt((int) (bound)) + (int)((num-1)*(widthTemp* roadWidthPercentage))
                        + (int) (widthTemp * 0.05);

                //Calculate y
                float y = random.nextInt(40) + 10;
                y = y/100;
                y = - (myHandler.getHeight() * (y));

                if(x > widthTemp*0.90) continue;

                myHandler.addEntity(new Obstacle(x , (int)y, (int) (myHandler.getWidth() * (obstacleWidthHeight)),
                        (int) (myHandler.getHeight() * (dimension)), myHandler, speed,num, currentColor));

                roads.put(num,true);

            }
        }
    }

    public void removeObjects(){
        ArrayList<Entity> temp = new ArrayList<>();
        for(Entity entity: myHandler.getEntities()){
            if(entity.isToBeRemoved()){
                temp.add(entity);
            }
        }

        for(Entity entity: temp){
            roads.put(((Obstacle) entity).getRoad(),false);
            myHandler.removeEntity(entity);
            myHandler.getGamePanel().getGameState().increaseScore();  //Increase score for every obstacle that is removed
        }
    }

    public void init(){
        for(int i=1; i<=roadSize; i++){
            roads.put(i,false);
        }
        roadSize = 3;
        speedLower=10;
        speedUpper=18;
        currentColor = Colors.ALL_COLORS.get(1);
        obstacleWidthHeight = 0.05f;
    }

    public void setObstacleWidthHeight(float num){
        this.obstacleWidthHeight = num;
    }

    public void setCurrentColor(int color){
        this.currentColor = color;
    }

    public void increaseRoadSize(){
        roadSize++;
        roads.put(roadSize,false);
    }

    public void increaseSpeed(){
        speedUpper+= 6;
    }

    public void increaseSpeed(int upper, int lower){
        speedUpper+= upper;
        speedLower+=lower;
    }

    public void increaseLowerSpeed(int num){
        speedLower+=num;
    }






}
