package com.example.firstgame.States;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.firstgame.Button;
import com.example.firstgame.Colors;
import com.example.firstgame.Entities.Entity;
import com.example.firstgame.Entities.Player;
import com.example.firstgame.Entities.SideWall;
import com.example.firstgame.GamePanel;
import com.example.firstgame.MyHandler;
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
    private Button pauseBtn, musicBtn,scoreBoard;
    private boolean[] levels = new boolean[7];
    private int timesLost=0;

    public GameState(MyHandler myHandler){
        super(myHandler);


        obstacleHandler = new ObstacleHandler(myHandler);
        highScore = this.myHandler.getGamePanel().getHighScore();
        addToFirebase(highScore);

        gameOver = true;
        gameRunning = false;

        welcomeScreen = true;
        instructions = true;
        gameOverScreen = false;

        pauseBtn = new Button((int)(myHandler.getWidth() * 0.85),(int)(myHandler.getHeight() * 0.01),(int)(myHandler.getWidth() * 0.09),
                (int)(myHandler.getHeight() * 0.07), myHandler, R.drawable.pause);

        musicBtn = new Button((int)(myHandler.getWidth() * 0.01),(int)(myHandler.getHeight() * 0.01),(int)(myHandler.getWidth() * 0.09),
                (int)(myHandler.getHeight() * 0.07), myHandler, R.drawable.music);

        scoreBoard = new Button((int)(myHandler.getWidth() * 0.85),(int)(myHandler.getHeight() * 0.90),(int)(myHandler.getWidth() * 0.09),
                (int)(myHandler.getHeight() * 0.07), myHandler, R.drawable.score);

    }

    private void startGame(){

        for(int i=0; i<levels.length; i++){
            levels[i] = false;
        }

        myHandler.initEntities();
        myHandler.addPlayer(new Player((int)(myHandler.getWidth() * 0.45),(int)(myHandler.getHeight() * 0.80),(int)(myHandler.getWidth() * 0.075),(int)(myHandler.getHeight() * 0.045), myHandler));
        myHandler.addEntity(new SideWall(0,0,(int)(myHandler.getWidth()*GameState.sideWallWidth),(int)(myHandler.getHeight()), myHandler));
        myHandler.addEntity(new SideWall((int)(myHandler.getWidth()*(0.95)),0,(int)(myHandler.getWidth()*(GameState.sideWallWidth)),(int)(myHandler.getHeight()), myHandler));

        gameOver = false;
        gameRunning = true;
        score = 0;
        obstacleHandler.init();

        welcomeScreen = false;
        instructions = false;
        gameOverScreen = false;

    }

    public void update(){
        if(Input.isClicked && scoreBoard.isClicked() && !gameRunning){
            this.myHandler.getGamePanel().setCurrentState(new ScoreBoardState(myHandler));
            return;
        }

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
            for (Entity entity : myHandler.getEntities()) {
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
            obstacleHandler.setObstacleWidthHeight(0.065f);
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(2));
            levels[1] = true;
        }else if(score == 35 && !levels[2]) {
            obstacleHandler.increaseSpeed(3,0);
            obstacleHandler.setObstacleWidthHeight(0.08f);
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(3));
            levels[2] = true;
        }else if(score == 77 && !levels[3]) {
            obstacleHandler.increaseRoadSize();
            obstacleHandler.setObstacleWidthHeight(0.09f);
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(4));
            levels[3] = true;
        }else if(score == 102 && !levels[4]) {
            this.myHandler.getPlayer().setSpeed(this.myHandler.getPlayer().getSpeed() + 3);
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(2));
            obstacleHandler.setObstacleWidthHeight(0.10f);
            obstacleHandler.increaseLowerSpeed(2);
            levels[4] = true;
        }else if(score == 130 && !levels[5]) {
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(3));
            this.myHandler.getPlayer().setSpeed(this.myHandler.getPlayer().getSpeed() + 4);
            obstacleHandler.increaseSpeed(5,3);
            levels[5] = true;
        }else if(score == 170 && !levels[6]) {
            this.myHandler.getPlayer().setSpeed(this.myHandler.getPlayer().getSpeed() + 4);
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(1));
            obstacleHandler.increaseSpeed(5,0);
            levels[6] = true;
        }
    }

    public void draw(Canvas canvas){
        for(Entity entity: myHandler.getEntities() ){
            entity.draw(canvas);
        }

        if(gameRunning) {   // If game running show score on top
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(100);
            canvas.drawText("" + score, (int) (myHandler.getWidth() * 0.50), 100, paint);

            pauseBtn.draw(canvas);  //Draw pause Button if game is running

        }else{
            musicBtn.draw(canvas);
            showHighScore(canvas);
            showUsername(canvas);
            scoreBoard.draw(canvas);
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
        timesLost++;

        this.gameOver = true;
        this.gameRunning = false;

        this.gameOverScreen = true;
        this.instructions = true;

        disableEntitiesToMove();
        myHandler.getGamePanel().vibrate(500);


        if(score > highScore) { //Save high-score
            this.myHandler.getGamePanel().saveScore(score);
            highScore = this.myHandler.getGamePanel().getHighScore();
            addToFirebase(highScore);
        }

        try{
            Thread.sleep(1000);
        }catch (Exception e){e.printStackTrace();}

        //SHOW ADS
        if(timesLost % 3 == 0)
            GamePanel.handler.post(GamePanel.runnable);

    }

    private void disableEntitiesToMove(){
        for(Entity entity: myHandler.getEntities()){
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
        canvas.drawText("Click to play", (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.30), paint);
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
        canvas.drawText("Click right or left", (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.70), paint);
        canvas.drawText("to define the direction", (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.75), paint);
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
        canvas.drawText("Game Over", (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.30), paint);
        canvas.drawText("Score: " + score, (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.40), paint);
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
        canvas.drawText("High Score: " + highScore, (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.90), paint);

    }

    /**
     * Show game pause screen
     */
    private void showGamePause(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(95);
        canvas.drawText("Pause", (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.40), paint);
        paint.setTextSize(75);
        canvas.drawText("Click to continue" , (int) (myHandler.getWidth() * 0.20), (int)(myHandler.getHeight() * 0.50), paint);
    }

    /**
     * Show game pause screen
     */
    private void showUsername(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(60);
        canvas.drawText(myHandler.getGamePanel().getUsername(), (int) (myHandler.getWidth() * 0.30), (int)(myHandler.getHeight() * 0.03), paint);
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
        if(myHandler.getGamePanel().isConnected()){
            this.myHandler.getGamePanel().getFirebase().addScore(this.myHandler.getGamePanel().getUsername(),score);
        }else{
            System.out.println("NO INTERNET");
        }

    }
}
