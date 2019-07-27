package com.example.firstgame;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;


public class MainActivity extends Activity {

    private static MediaPlayer music;
    private static boolean isMuted = false;

    private GamePanel game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        MobileAds.initialize(this,
                "ca-app-pub-7737590834918939~1600807424");

        InterstitialAd mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId("ca-app-pub-7737590834918939/7760797763");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        game = new GamePanel(this, new Handler(this.getMainLooper()),mInterstitialAd );
        setContentView(game);


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
