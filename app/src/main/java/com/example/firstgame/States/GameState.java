package com.example.firstgame.States;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.firstgame.Button;
import com.example.firstgame.Entities.Entity;
import com.example.firstgame.Entities.Player;
import com.example.firstgame.Entities.SideWall;
import com.example.firstgame.Handler;
import com.example.firstgame.Input;
import com.example.firstgame.MainActivity;
import com.example.firstgame.ObstacleHandler;
import com.example.firstgame.R;

public class GameState extends State {

    public final static float sideWallWidth = 0.05f;
    private final int eachPoint = 1;

    private ObstacleHandler obstacleHandler;
    private boolean gameOver, gameRunning, welcomeScreen, instructions, gameOverScreen;
    private boolean pause = false;
    private int score,highScore;
    private boolean musicMute = false;
    private Button pauseBtn, musicBtn;

    public GameState(Handler handler){
        super(handler);
        obstacleHandler = new ObstacleHandler(handler);
        highScore = this.handler.getGamePanel().getHighScore();
        
        gameOver = true;
        gameRunning = false;
        
        welcomeScreen = true;
        instructions = true;
        gameOverScreen = false;

        pauseBtn = new Button((int)(handler.getWidth() * 0.85),(int)(handler.getHeight() * 0.01),(int)(handler.getWidth() * 0.09),
                (int)(handler.getHeight() * 0.07), handler, R.drawable.pause);

        musicBtn = new Button((int)(handler.getWidth() * 0.06),(int)(handler.getHeight() * 0.01),(int)(handler.getWidth() * 0.09),
                (int)(handler.getHeight() * 0.07), handler, R.drawable.music);

    }

    private void startGame(){
        handler.initEntities();
        handler.addPlayer(new Player((int)(handler.getWidth() * 0.45),(int)(handler.getHeight() * 0.80),(int)(handler.getWidth() * 0.08),(int)(handler.getHeight() * 0.05),handler));
        handler.addEntity(new SideWall(0,0,(int)(handler.getWidth()*GameState.sideWallWidth),(int)(handler.getHeight()), handler));
        handler.addEntity(new SideWall((int)(handler.getWidth()*(0.95)),0,(int)(handler.getWidth()*(GameState.sideWallWidth)),(int)(handler.getHeight()),handler));

        gameOver = false;
        gameRunning = true;
        score = 0;
        obstacleHandler.init();

        welcomeScreen = false;
        instructions = false;
        gameOverScreen = false;

    }

    public void update(){
        if(Input.isClicked && musicBtn.isClicked()){
            MainActivity.pauseMusic();
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }

        if(isGameOver() && Input.isClicked){
            startGame();
        }

        if(Input.isClicked){
            pause = false;
        }

        if(pause) return;

        if(gameRunning && pauseBtn.isClicked()){   //Check if pause is clicked
            pause = true;
            return;
        }

        if(!pause) {
            for (Entity entity : handler.getEntities()) {
                entity.update();
            }
        }

        if(gameRunning){
            obstacleHandler.createObstacle();
            obstacleHandler.removeObjects();
        }

        //TODO: FIX WAY OF INCREASING ROADS;
        if(score == 8) {
            obstacleHandler.increaseRoadSize();
            score++;
        }else if(score == 16) {
            obstacleHandler.increaseRoadSize();
            score++;
        }else if(score == 34) {
            obstacleHandler.increaseSpeed();
            score++;
        }else if(score == 60) {
            obstacleHandler.increaseRoadSize();
            score++;
        }else if(score == 100) {
            this.handler.getPlayer().setSpeed(this.handler.getPlayer().getSpeed() + 4);
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

            pauseBtn.draw(canvas);  //Draw pause Button if game is running

        }else{
            musicBtn.draw(canvas);
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

        if(pause){
            showGamePause(canvas);
        }
    }


    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver() {
        this.gameOver = true;
        this.gameRunning = false;

        this.gameOverScreen = true;
        this.instructions = true;

        disableEntitiesToMove();
        handler.getGamePanel().vibrate(500);


        if(score > highScore) { //Save high-score
            this.handler.getGamePanel().save(score);
            highScore = this.handler.getGamePanel().getHighScore();
        }

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
        canvas.drawText("Click to play", (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.30), paint);
    }

    /**
     * Show the instructions on the screen
     * When game is not running
     * @param canvas
     */
    private void showInstructions(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        canvas.drawText("Click right or left", (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.70), paint);
        canvas.drawText("to define the direction", (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.75), paint);
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
        canvas.drawText("Game Over", (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.30), paint);
        canvas.drawText("Score: " + score, (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.40), paint);
        canvas.drawText("High Score: " + highScore, (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.50), paint);

    }

    /**
     * Show game pause screen
     */
    private void showGamePause(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(95);
        canvas.drawText("Pause", (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.40), paint);
        paint.setTextSize(75);
        canvas.drawText("Click to continue" , (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.50), paint);
    }

    public void pauseGame(){
        pause = true;
    }

    public boolean isGameRunning(){
        return gameRunning;
    }

    public void resumeGame(){
        pause = false;
    }

    public ObstacleHandler getObstacleHandler(){
        return obstacleHandler;
    }
}
