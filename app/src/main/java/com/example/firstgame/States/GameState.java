package com.example.firstgame.States;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.firstgame.Button;
import com.example.firstgame.Entities.Entity;
import com.example.firstgame.Entities.Player;
import com.example.firstgame.Entities.SideWall;
import com.example.firstgame.Firebase.Firebase;
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
    private Button pauseBtn, musicBtn;
    private boolean[] levels = new boolean[7];

    public GameState(Handler handler){
        super(handler);
        obstacleHandler = new ObstacleHandler(handler);
        highScore = this.handler.getGamePanel().getHighScore();
        addToFirebase(highScore);
        
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

        for(int i=0; i<levels.length; i++){
            levels[i] = false;
        }

        handler.initEntities();
        handler.addPlayer(new Player((int)(handler.getWidth() * 0.45),(int)(handler.getHeight() * 0.80),(int)(handler.getWidth() * 0.075),(int)(handler.getHeight() * 0.045),handler));
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
        //LEVEL UPDATES
        if(score == 8 && !levels[0]) {
            obstacleHandler.increaseRoadSize();
            levels[0] = true;
        }else if(score == 17 && !levels[1]) {
            obstacleHandler.increaseRoadSize();
            levels[1] = true;
        }else if(score == 35 && !levels[2]) {
            obstacleHandler.increaseSpeed(4,1);
            levels[2] = true;
        }else if(score == 67 && !levels[3]) {
            obstacleHandler.increaseRoadSize();
            levels[3] = true;
        }else if(score == 102 && !levels[4]) {
            this.handler.getPlayer().setSpeed(this.handler.getPlayer().getSpeed() + 3);
            obstacleHandler.increaseLowerSpeed(2);
            levels[4] = true;
        }else if(score == 130 && !levels[5]) {
            this.handler.getPlayer().setSpeed(this.handler.getPlayer().getSpeed() + 4);
            obstacleHandler.increaseSpeed(5,3);
            levels[5] = true;
        }else if(score == 170 && !levels[6]) {
            this.handler.getPlayer().setSpeed(this.handler.getPlayer().getSpeed() + 4);
            obstacleHandler.increaseSpeed(5,0);
            levels[6] = true;
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
            showHighScore(canvas);
            showUsername(canvas);
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
            musicBtn.draw(canvas);
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
            this.handler.getGamePanel().saveScore(score);
            highScore = this.handler.getGamePanel().getHighScore();
            addToFirebase(highScore);
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
    }

    /**
     * Show Game over screen
     * Shown when the player lose
     * @param canvas
     */
    private void showHighScore(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(65);
        canvas.drawText("High Score: " + highScore, (int) (handler.getWidth() * 0.20), (int)(handler.getHeight() * 0.90), paint);

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

    /**
     * Show game pause screen
     */
    private void showUsername(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(60);
        canvas.drawText(handler.getGamePanel().getUsername(), (int) (handler.getWidth() * 0.30), (int)(handler.getHeight() * 0.03), paint);
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

    /**
     * Add high score to firebase if there is an interent connection;
     * @param score
     */
    private void addToFirebase(int score){
        if(handler.getGamePanel().isConnected()){
            Firebase.addScore(this.handler.getGamePanel().getUsername(),score);
        }else{
            System.out.println("NO INTERNET");
        }

    }
}
