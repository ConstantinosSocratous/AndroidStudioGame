package com.example.firstgame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.health.SystemHealthManager;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firstgame.Firebase.Firebase;
import com.example.firstgame.Firebase.User;
import com.example.firstgame.States.GameState;
import com.example.firstgame.States.ScoreBoardState;
import com.example.firstgame.States.State;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


import static android.content.Context.CONNECTIVITY_SERVICE;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {


    private String username = "";
    private Firebase firebase;
    private MainThread mainThread;
    private State gameState,currentState;
    private Handler handler;
    private int width,height;

    public GamePanel(Context context){
        super(context);
        firebase = new Firebase();
        firebase.start();

        getHolder().addCallback(this);
        mainThread = new MainThread(getHolder(),this);
        setFocusable(true);

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        handler = new Handler(width,height,this);

        username = getUsernameFromFile();
        if(username == null || username == ""){
            getUsernameFromInput();
        }else{
            init();
        }
    }

    private void init(){
        gameState = new GameState(handler);
        currentState = gameState;
    }

    /**
     * Request input from user
     */
    private void getUsernameFromInput(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter your nickname to continue");

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                username = input.getText().toString();
                if(isInputValid(username)){
                    saveUsername(username);
                    init();
                }else{
                    getUsernameFromInput();
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    /**
     * Check if user string is valid
     * @param user The input nickname
     * @return
     */
    private boolean isInputValid(String user){

        if(!isConnected()){
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            return false;
        }

        user = user.replace(" ","");
        user = user.replace(".","");
        if(user.length() > 1 ){
            if(firebase.getNicknames().contains(user)){
                Toast.makeText(getContext(), "Nickname already exists", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }else{
            Toast.makeText(getContext(), "Please enter a valid nickname", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mainThread = new MainThread(getHolder(),this);
        mainThread.setRunning(true);
        mainThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(((GameState)gameState).isGameRunning()){
            pauseGame();
        }
        boolean retry = true;

        while(retry){
            try{
                mainThread.setRunning(false);
                mainThread.join();
            }catch (Exception e){e.printStackTrace();}

            retry = false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        Input.x = (int) event.getX();
        Input.y = (int) event.getY();

        if(event.getX() > (handler.getWidth() * 0.5) ){
            Input.left = false;
        }else if(event.getX() <= (handler.getWidth() * 0.5) ){
            Input.left = true;
        }

        if(event.getAction() == MotionEvent.ACTION_UP) {
            Input.isClicked = false;
            return false;
        }else {
            Input.isClicked = true;
            return true;
        }
    }

    public void update(){
        if(currentState != null)
            currentState.update();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        canvas.drawColor(Color.parseColor("#C0C0C0"));

        if(currentState != null)
            currentState.draw(canvas);

    }

    public void setCurrentState(State state){
        this.currentState = state;
    }

    public GameState getGameState(){ return (GameState) (gameState); }

    /**
     * Vibrates the phone
     * @param milliseconds
     */
    public void vibrate(int milliseconds){
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(milliseconds);
        }
    }

    /**
     * Get high score from file
     * @return
     */
    public int getHighScore() {
        String a="0";
        try {
            FileInputStream file = getContext().openFileInput("file.txt");
            InputStreamReader isr = new InputStreamReader(file);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            if ((text = br.readLine()) != null) {
                a = text;
            }

            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(a);
    }

    /**
     * Save highScore
     * @param str
     */
    public void saveScore(int str) {
        String text = ""+str;
        FileOutputStream fos = null;

        try {
            fos = getContext().openFileOutput("file.txt", getContext().MODE_PRIVATE);
            fos.write(text.getBytes());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get username from file
     * @return
     */
    public String getUsernameFromFile() {
        String a="";
        try {
            FileInputStream file = getContext().openFileInput("fileU.txt");
            InputStreamReader isr = new InputStreamReader(file);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            if ((text = br.readLine()) != null) {
                a = text;
            }

            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return a;
    }

    /**
     * Save username
     * @param str
     */
    public void saveUsername(String str) {
        String text = str;
        FileOutputStream fos = null;

        try {
            fos = getContext().openFileOutput("fileU.txt", getContext().MODE_PRIVATE);
            fos.write(text.getBytes());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void pauseGame(){
        ((GameState)(this.gameState)).pauseGame();
    }

    public void resumeGame(){
        ((GameState)(this.gameState)).resumeGame();
    }


    /**
     *  Check if there is internet connection
     */
    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;

        }
    }

    public String getUsername(){
        return this.username;
    }

    public ArrayList<User> getUsers(){
        return firebase.getUsers();
    }

    public Firebase getFirebase() {
        return firebase;
    }
}
