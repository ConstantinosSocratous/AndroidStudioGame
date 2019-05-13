package com.example.firstgame.States;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.firstgame.Entities.Entity;
import com.example.firstgame.Entities.Player;
import com.example.firstgame.Entities.SideWall;
import com.example.firstgame.Handler;
import com.example.firstgame.Input;
import com.example.firstgame.ObstacleHandler;

public class GameState extends State {

    public final static float sideWallWidth = 0.05f;
    private final int eachPoint = 1;

    private ObstacleHandler obstacleHandler;
    private boolean gameOver, gameRunning, welcomeScreen, instructions, gameOverScreen;
    private int score;

    public GameState(Handler handler){
        super(handler);
        obstacleHandler = new ObstacleHandler(handler);
        
        gameOver = true;
        gameRunning = false;
        
        welcomeScreen = true;
        instructions = true;
        gameOverScreen = false;
    }

    private void startGame(){
        handler.initEntities();
        handler.addPlayer(new Player((int)(handler.getWidth() * 0.45),(int)(handler.getHeight() * 0.80),(int)(handler.getWidth() * 0.08),(int)(handler.getHeight() * 0.05),handler));
        handler.addEntity(new SideWall(0,0,(int)(handler.getWidth()*GameState.sideWallWidth),(int)(handler.getHeight()), handler));
        handler.addEntity(new SideWall((int)(handler.getWidth()*(0.95)),0,(int)(handler.getWidth()*(GameState.sideWallWidth)),(int)(handler.getHeight()),handler));

        gameOver = false;
        gameRunning = true;
        score = 0;
        obstacleHandler.initRoads();

        welcomeScreen = false;
        instructions = false;
        gameOverScreen = false;
    }

    public void update(){
        if(isGameOver() && Input.isClicked){
            startGame();
        }

        for (Entity entity : handler.getEntities()) {
            entity.update();
        }

        if(gameRunning){
            obstacleHandler.createObstacle();
            obstacleHandler.removeObjects();
        }

        //TODO: FIX WAY OF INCREASING ROADS;
        if(score == 10) {
            obstacleHandler.increaseRoadSize();
            score++;
        }
    }

    public void draw(Canvas canvas){
        for(Entity entity: handler.getEntities() ){
            entity.draw(canvas);
        }

        if(gameRunning) {   // If game running show score on top
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(100);
            canvas.drawText("" + score, (int) (handler.getWidth() * 0.50), 100, paint);
        }

        if(gameOverScreen){ //When the player lose
            showGameOver(canvas);
        }

        if(instructions){ //Show instructions
            showInstructions(canvas);
        }

        if(welcomeScreen){ //Show welcome screen
            showWelcomeScreen(canvas);
        }
    }


    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver() {
        System.out.println("GAME OVER");

        this.gameOver = true;
        this.gameRunning = false;

        this.gameOverScreen = true;
        this.instructions = true;

        disableEntitiesToMove();
        handler.getGamePanel().vibrate(500);

        try{
            Thread.sleep(1000);
        }catch (Exception e){e.printStackTrace();}
    }

    private void disableEntitiesToMove(){
        for(Entity entity: handler.getEntities()){
            entity.setCanMove(false);
        }
    }

    public void increaseScore(){
        this.score += eachPoint;
    }

    /**
     * Show welcome screen
     * It is shown only the first time the game opens
     * @param canvas
     */
    private void showWelcomeScreen(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(95);
        canvas.drawText("Click once to start game", (int) (handler.getWidth() * 0.15), (int)(handler.getHeight() * 0.30), paint);
    }

    /**
     * Show the instructions on the screen
     * When game is not running
     * @param canvas
     */
    private void showInstructions(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(70);
        canvas.drawText("Click right or left", (int) (handler.getWidth() * 0.30), (int)(handler.getHeight() * 0.70), paint);
        canvas.drawText("to define the direction", (int) (handler.getWidth() * 0.27), (int)(handler.getHeight() * 0.75), paint);
    }

    /**
     * Show Game over screen
     * Shown when the player lose
     * @param canvas
     */
    private void showGameOver(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(95);
        canvas.drawText("Game Over", (int) (handler.getWidth() * 0.33), (int)(handler.getHeight() * 0.30), paint);
        canvas.drawText("Score: " + score, (int) (handler.getWidth() * 0.35), (int)(handler.getHeight() * 0.40), paint);
    }
}
