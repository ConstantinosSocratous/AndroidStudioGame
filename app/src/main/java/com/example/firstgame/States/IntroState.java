package com.example.firstgame.States;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.firstgame.Handler;
import com.example.firstgame.R;

public class IntroState extends State{

    private Bitmap bmp;
    private int ticks;

    public IntroState(Handler handler) {
        super(handler);
        bmp = BitmapFactory.decodeResource(handler.getGamePanel().getResources(), R.drawable.intropic);
        ticks = 0;
    }

    @Override
    public void update() {
        if(ticks > 90){
            if(this.handler.getGamePanel().isTutorialSeen()){
                this.handler.getGamePanel().setCurrentState(this.handler.getGamePanel().getGameState());
            }else {
                this.handler.getGamePanel().setCurrentState(new TutorialState(handler));
            }
        }
        ticks++;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect dest = new Rect(0, 0, this.handler.getWidth(), this.handler.getHeight());
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bmp, null,dest, paint);

    }
}
