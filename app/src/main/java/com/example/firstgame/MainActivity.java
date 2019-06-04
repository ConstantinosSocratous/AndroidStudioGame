package com.example.firstgame;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    private static MediaPlayer music;
    private static boolean isMuted = false;

    private GamePanel game;
    private VideoView video;
    private int stopPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        game = new GamePanel(this);

        if(isTutorialSeen()){   //CHECK IF TUTORIAL IS SEEN
            try {
                setContentView(game);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            //Show tutorial video
            setContentView(R.layout.activity_main);
            video = (VideoView) findViewById(R.id.videoView);
            String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.tutuorial;
            Uri uri = Uri.parse(uriPath);
            try{
                video.setVideoURI(uri);
                video.requestFocus();
                video.start();
            }catch (Exception e){
                e.printStackTrace();
            }

            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                public void onCompletion(MediaPlayer mp) {
                    setTutorialToSeen("1");
                    setContentView(game);
                }
            });

        }

        //-------------------------\\

        //Start music
        //TODO: SELECT RANDOMLY A MUSIC FILE
        music = MediaPlayer.create(MainActivity.this,R.raw.positive);
        music.start();

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

        if(!isTutorialSeen()) {
            stopPosition = video.getCurrentPosition(); //stopPosition is an int
            video.pause();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(isMuted){
            music.pause();
        }else{
            music.start();
        }

        if(!isTutorialSeen()) {
            video.seekTo(stopPosition);
            video.start();
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

    //---------------------------------\\

    /**
     * Get username from file
     * @return
     */
    public boolean isTutorialSeen() {
        String a="";
        try {
            FileInputStream file = game.getContext().openFileInput("tutorial.txt");
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

        if(a.equals("1")){
            return true;
        }else{
            return false;
        }

    }

    /**
     * Save username
     */
    public void setTutorialToSeen(String str) {
        String text = str;
        FileOutputStream fos = null;

        try {
            fos = game.getContext().openFileOutput("tutorial.txt", game.getContext().MODE_PRIVATE);
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
