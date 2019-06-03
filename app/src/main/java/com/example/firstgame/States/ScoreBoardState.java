package com.example.firstgame.States;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.example.firstgame.Button;
import com.example.firstgame.Firebase.Firebase;
import com.example.firstgame.Firebase.User;
import com.example.firstgame.Handler;
import com.example.firstgame.Input;
import com.example.firstgame.MainActivity;
import com.example.firstgame.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class ScoreBoardState extends State {

    private boolean isConnectedToInternet;
    private PriorityQueue<User> users;
    private ArrayList<User> usersList;
    private Button next,previous,back;
    private int width,height;

    public ScoreBoardState(Handler handler) {
        super(handler);

//        next = new Button((int) (handler.getWidth() * 0.85), (int) (handler.getHeight() * 0.90), (int) (handler.getWidth() * 0.09),
//                (int) (handler.getHeight() * 0.07), handler, R.drawable.next);
//
//        previous = new Button((int) (handler.getWidth() * 0.08), (int) (handler.getHeight() * 0.90), (int) (handler.getWidth() * 0.10),
//                (int) (handler.getHeight() * 0.10), handler, R.drawable.previous);

        back = new Button((int) (handler.getWidth() * 0.01), (int) (handler.getHeight() * 0.01), (int) (handler.getWidth() * 0.05),
                (int) (handler.getHeight() * 0.05), handler, R.drawable.exit);

        width = this.handler.getWidth();
        height = this.handler.getHeight();

        if(handler.getGamePanel().isConnected()) {
            readUsers();
            isConnectedToInternet = true;
        }else{
            isConnectedToInternet = false;
        }
    }

    /**
     * Put users in decreasing order based on their score
     */
    private void readUsers(){
        ArrayList<User> temp = this.handler.getGamePanel().getUsers();
        try {
            users = new PriorityQueue<User>(temp.size(), new UserComparator());
        }catch (Exception e){
            e.printStackTrace();
        }

        for (User user : temp) {
            users.add(user);
        }

        int size = users.size();
        usersList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            User u = users.poll();
            usersList.add(u);
        }
    }

    @Override
    public void update() {
        if (Input.isClicked && back.isClicked()) {
            this.handler.getGamePanel().setCurrentState(this.handler.getGamePanel().getGameState());

            if(handler.getGamePanel().isConnected()) {
                this.handler.getGamePanel().getFirebase().start();
            }

            try {
                Thread.sleep(250);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
    }

    @Override
    public void draw(Canvas canvas) {
//        next.draw(canvas);
//        previous.draw(canvas);
        back.draw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#993333"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        canvas.drawRect((int)(width*0.07),(int)(height*0.11),(int)(width*0.92),(int)(height*0.95),paint);

        if(isConnectedToInternet) {
            drawPlayers(canvas);
        }else{
            Paint paint1 = new Paint();
            paint1.setColor(Color.BLACK);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint1.setTextSize(60);
            canvas.drawText("NO INTERNET CONNECTION",(int)(width*0.10),(int)(height*0.20),paint1);
        }
    }

    private void drawPlayers(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(60);

        int startHeight = (int)(height*0.15);

        int index = 1;
        boolean myPositionShown = false;
        for(User user : usersList){

            if(startHeight > (int)(height*0.93))break;

            if(user.getName().equals(this.handler.getGamePanel().getUsername())){
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                myPositionShown = true;
            }
            //Draw Name
            canvas.drawText(index + ")  " + user.getName(),(int)(width*0.12),startHeight,paint);

            //Draw Score
            canvas.drawText(""+user.getScore(),(int)(width*0.80),startHeight,paint);

            startHeight += (int)(height*0.05);
            index++;
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        }

        if(!myPositionShown){
            for (int i=1; i< usersList.size(); i++){
                if(usersList.get(i).getName().equals(this.handler.getGamePanel().getUsername())){
                    paint.setTextSize(50);
                    canvas.drawText("My position: "+i,(int)(width*0.40),(int)(height*0.03),paint);
                }
            }
        }

    }

    public class UserComparator implements Comparator<User> {
        public int compare(User u1, User u2) {
            if (u1.getScore() < u2.getScore())
                return 1;
            else if (u1.getScore() > u2.getScore())
                return -1;
            return 0;
        }
    }
}
