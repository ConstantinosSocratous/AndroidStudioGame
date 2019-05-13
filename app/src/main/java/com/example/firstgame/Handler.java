package com.example.firstgame;

import com.example.firstgame.Entities.Entity;
import com.example.firstgame.Entities.Player;

import java.util.ArrayList;

public class Handler {

    private ArrayList<Entity> entities;
    private GamePanel gamePanel;
    private Player player;
    private int width,height;

    public Handler(int w, int h, GamePanel gamePanel){
        entities = new ArrayList<>();
        this.width = w;
        this.height = h;
        this.gamePanel = gamePanel;
    }

    public ArrayList<Entity> getEntities(){
        return entities;
    }

    public void addEntity(Entity e){
        entities.add(e);
    }
    public void removeEntity(Entity e){
        entities.remove(e);
    }

    public void addPlayer(Player pl){
        this.player = pl;
        addEntity(player);
    }

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

    public GamePanel getGamePanel(){
        return this.gamePanel;
    }

    public void initEntities(){
        this.entities = new ArrayList<>();
    }
}
