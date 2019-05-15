package com.example.firstgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.firstgame.Entities.Entity;
import com.example.firstgame.Entities.Player;
import com.example.firstgame.Entities.SideWall;
import com.example.firstgame.States.GameState;
import com.example.firstgame.States.State;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.support.v4.content.ContextCompat.getSystemService;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread mainThread;
    private State gameState,currentState;
    private Handler handler;
    private int width,height;


    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        mainThread = new MainThread(getHolder(),this);
        setFocusable(true);

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        handler = new Handler(width,height,this);

        init();
    }

    private void init(){
        gameState = new GameState(handler);
        currentState = gameState;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mainThread = new MainThread(getHolder(),this);
        mainThread.setRunning(true);
        mainThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(true){
            try{
                mainThread.setRunning(false);
                mainThread.join();
            }catch (Exception e){e.printStackTrace();}

            retry = false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getX() > (handler.getWidth() * 0.5) ){
            Input.left = false;
            //return false;
        }else if(event.getX() <= (handler.getWidth() * 0.5) ){
            Input.left = true;
            //return true;
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
        canvas.drawColor(Color.WHITE);

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

    public int getHighScore() {

        String a="";
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

    public void save(int str) {

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


}
