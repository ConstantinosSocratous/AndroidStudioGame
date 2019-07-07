package com.example.firstgame;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;


public class MainActivity extends Activity {

    private static MediaPlayer music;
    private static boolean isMuted = false;

    private GamePanel game;
    private VideoView video,intro;

    private int stopPosition=0, introStop=0;
    private boolean showIntro=true, showTutorial=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        game = new GamePanel(this);
        setContentView(game);

        //------------------------------------\\
        /*
        setTutorialToSeen("0");
        setContentView(R.layout.activity_main);
        intro = (VideoView) findViewById(R.id.videoView);

        String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.intropic;
        Uri uri = Uri.parse(uriPath);
        try{
            intro.setVideoURI(uri);
            intro.requestFocus();
            intro.start();
        }catch (Exception e){
            e.printStackTrace();
        }

        intro.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                showTutorial = true;
                showIntro = false;
                showTutorial();
            }
        });
        */
        //-----------------------------------\\

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

    /*
    private void showTutorial(){
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


            String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.tutorialavi;
            Uri uri = Uri.parse(uriPath);
            try{

                video.setVideoURI(uri);
                video.requestFocus();
                video.start();
            }catch (Exception e){
                e.printStackTrace();
            }

            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    setTutorialToSeen("1");
                    setContentView(game);
                }
            });
        }
    }
    */

    @Override
    protected void onPause(){
        super.onPause();
        music.pause();

        /*
        if(!isTutorialSeen() && showTutorial) {
            stopPosition = video.getCurrentPosition(); //stopPosition is an int
            video.pause();
        }

        if(showIntro){
            introStop = intro.getCurrentPosition();
            intro.pause();
        }*/

    }

    @Override
    protected void onResume(){
        super.onResume();

        if(isMuted){
            music.pause();
        }else{
            music.start();
        }

        /*
        if(!isTutorialSeen() && showTutorial) {
            video.seekTo(stopPosition);
            video.start();
        }

        if(showIntro){
            intro.seekTo(introStop);
            intro.start();
        }
        */
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
     * Check if tutorial is seen
     * @return
     */
    /*
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
    */

    /**
     * Set tutorial to seen
     */
    /*
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
    */

}
