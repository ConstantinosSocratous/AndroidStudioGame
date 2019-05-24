package com.example.firstgame;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.firstgame.Firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity {

    private static MediaPlayer music;
    private static boolean isMuted = false;

    private GamePanel game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        game = new GamePanel(this);

        //Start music
        //TODO: SELECT RANDOMLY A MUSIC FILE
        music = MediaPlayer.create(MainActivity.this,R.raw.positive);
        music.start();

        setContentView(game);

        //RESTART MUSIC WHEN FINISHED
        music.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer music) {
                try{
                    music.seekTo(0);
                    music.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        music.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(isMuted){
            music.pause();
        }else{
            music.start();
        }
    }

    public static void pauseMusic(){
        isMuted = !isMuted;

        if(isMuted){
            music.pause();
        }else{
            music.start();
        }
    }

}
