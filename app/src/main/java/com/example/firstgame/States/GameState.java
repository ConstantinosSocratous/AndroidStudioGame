package com.example.firstgame.States;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

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
    private Button pauseBtn, musicBtn,scoreBoard, playBtn, resumeBtn;
    private boolean[] levels = new boolean[11];
    private int timesLost=0;

    public GameState(MyHandler myHandler){
        super(myHandler);


        obstacleHandler = new ObstacleHandler(myHandler);
        highScore = this.myHandler.getGamePanel().getHighScore();
        addScoreToFirebase(highScore);

        addTimesPlayedToFirebase(this.myHandler.getGamePanel().getTimesPlayed());

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

        Bitmap bmp = BitmapFactory.decodeResource(myHandler.getGamePanel().getResources(),R.drawable.play);
        playBtn = new Button((int)(myHandler.getWidth() * 0.5 - (bmp.getWidth()/2) ),(int)(myHandler.getHeight() * 0.5- (bmp.getHeight()/2) ),(int)(myHandler.getWidth() * 0.09),
                (int)(myHandler.getHeight() * 0.07), myHandler, R.drawable.play);

        Bitmap bmpResume = BitmapFactory.decodeResource(myHandler.getGamePanel().getResources(),R.drawable.resume);
        resumeBtn = new Button((int)(myHandler.getWidth() * 0.5 - (bmpResume.getWidth()/2) ),(int)(myHandler.getHeight() * 0.5- (bmpResume.getHeight()/2) ),(int)(myHandler.getWidth() * 0.09),
                (int)(myHandler.getHeight() * 0.07), myHandler, R.drawable.resume);
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

        if(Input.isClicked && musicBtn.isClicked() && (pause || !gameRunning)){
            MainActivity.pauseMusic();
            if(musicBtn.getImg() == R.drawable.music){
                musicBtn.setImg(R.drawable.musicoff);
            }else{
                musicBtn.setImg(R.drawable.music);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        if(!gameRunning)
            if(isGameOver() && playBtn.isClicked() && Input.isClicked){
                startGame();
                Log.e("ERROR","PLAY");
            }

        if(Input.isClicked && resumeBtn.isClicked() && pause){
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pause = false;
            Log.e("ERROR","RESUME");
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
            obstacleHandler.increaseSpeed(3,0);
            obstacleHandler.setObstacleWidthHeight(0.085f);
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(2));
            levels[1] = true;
        }else if(score == 30 && !levels[2]) {
            obstacleHandler.increaseRoadSize();
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(3));
            levels[2] = true;
        }else if(score == 77 && !levels[3]) {
            obstacleHandler.increaseRoadSize();
            obstacleHandler.setObstacleWidthHeight(0.10f);
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(4));
            levels[3] = true;
        }else if(score == 102 && !levels[4]) {
            this.myHandler.getPlayer().setSpeed(this.myHandler.getPlayer().getSpeed() + 3);
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(5));
            obstacleHandler.increaseLowerSpeed(2);
            levels[4] = true;
        }else if(score == 130 && !levels[5]) {
            this.myHandler.getPlayer().setSpeed(this.myHandler.getPlayer().getSpeed() + 4);
            obstacleHandler.increaseSpeed(5,3);
            levels[5] = true;
        }else if(score == 170 && !levels[6]) {
            this.myHandler.getPlayer().setSpeed(this.myHandler.getPlayer().getSpeed() + 4);
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(6));
            obstacleHandler.increaseSpeed(5,0);
            levels[6] = true;
        }else if(score == 230 && !levels[7]) {
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(2));
            obstacleHandler.increaseSpeed(2,0);
            levels[7] = true;
        }else if(score == 290 && !levels[8]) {
            this.myHandler.getPlayer().setSpeed(this.myHandler.getPlayer().getSpeed() + 2);
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(3));
            levels[8] = true;
        }else if(score == 350 && !levels[9]) {
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(4));
            obstacleHandler.increaseSpeed(2,0);
            levels[9] = true;
        }else if(score == 450 && !levels[10]) {
            obstacleHandler.setCurrentColor(Colors.ALL_COLORS.get(5));
            obstacleHandler.increaseSpeed(1,2);
            levels[10] = true;
        }
    }

    public void draw(Canvas canvas){
        for(Entity entity: myHandler.getEntities() ){
            entity.draw(canvas);
        }

        if(gameRunning) {   // If game running show score on top
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(65);
            canvas.drawText("" + score, (int) (myHandler.getWidth()/2), 100, paint);
            pauseBtn.draw(canvas);  //Draw pause Button if game is running

        }else{
            musicBtn.draw(canvas);
            showHighScore(canvas);
            showUsername(canvas);
            scoreBoard.draw(canvas);
            playBtn.draw(canvas);
        }

        if(gameOverScreen){ //When the player lose
            showGameOver(canvas);
        }

        if(pause){
            //showGamePause(canvas);
            musicBtn.draw(canvas);
            resumeBtn.draw(canvas);
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver() {
        //getTimesPlayed() {

        addTimesPlayedToFirebase(myHandler.getGamePanel().getTimesPlayed() + 1);
        Input.isClicked = false;
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
            addScoreToFirebase(highScore);
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
     * Show Game over screen
     * Shown when the player lose
     * @param canvas
     */
    private void showGameOver(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(70);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Game Over", (int) (myHandler.getWidth()/2), (int)(myHandler.getHeight() * 0.30), paint);
        canvas.drawText("Score: " + score, (int) (myHandler.getWidth()/2), (int)(myHandler.getHeight() * 0.40), paint);
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
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("High Score: " + highScore, (int) (myHandler.getWidth() /2), (int)(myHandler.getHeight() * 0.90), paint);

    }

    /**
     * Show game pause screen
     */
    private void showUsername(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(60);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(myHandler.getGamePanel().getUsername(), (int) ((myHandler.getWidth() / 2)), (int)(myHandler.getHeight() * 0.04), paint);
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
    public boolean isPause(){
        return pause;
    }

    public ObstacleHandler getObstacleHandler(){
        return obstacleHandler;
    }

    /**
     * Add high score to firebase if there is an interent connection;
     * @param score
     */
    private void addScoreToFirebase(int score){
        if(myHandler.getGamePanel().isConnected()){
            this.myHandler.getGamePanel().getFirebase().addScoreUpdated(this.myHandler.getGamePanel().getUsername(),score);
        }else{
            System.out.println("NO INTERNET");
        }
    }

    private void addTimesPlayedToFirebase(int times){
        this.myHandler.getGamePanel().saveTimesPlayed(times);
        if(myHandler.getGamePanel().isConnected()){
            this.myHandler.getGamePanel().getFirebase().addTimesPlayed(this.myHandler.getGamePanel().getUsername(),times);
        }else{
            System.out.println("NO INTERNET");
        }
    }
}
