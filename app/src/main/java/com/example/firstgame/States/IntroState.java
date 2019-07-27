package com.example.firstgame.States;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.firstgame.MyHandler;
import com.example.firstgame.R;

public class IntroState extends State{

    private Bitmap bmp;
    private int ticks;

    public IntroState(MyHandler myHandler) {
        super(myHandler);
        bmp = BitmapFactory.decodeResource(myHandler.getGamePanel().getResources(), R.drawable.intropic);
        ticks = 0;
    }

    @Override
    public void update() {
        if(ticks > 90){
            if(this.myHandler.getGamePanel().isTutorialSeen()){
                this.myHandler.getGamePanel().setCurrentState(this.myHandler.getGamePanel().getGameState());
            }else {
                this.myHandler.getGamePanel().setCurrentState(new TutorialState(myHandler));
            }
        }
        ticks++;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect dest = new Rect(0, 0, this.myHandler.getWidth(), this.myHandler.getHeight());
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bmp, null,dest, paint);

    }
}
