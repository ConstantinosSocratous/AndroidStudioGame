package com.example.firstgame.States;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.example.firstgame.Button;
import com.example.firstgame.Firebase.User;
import com.example.firstgame.MyHandler;
import com.example.firstgame.Input;
import com.example.firstgame.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class ScoreBoardState extends State {

    private boolean isConnectedToInternet;
    private PriorityQueue<User> users;
    private ArrayList<User> usersList;
    private Button next,previous,back;
    private int width,height;

    public ScoreBoardState(MyHandler myHandler) {
        super(myHandler);

//        next = new Button((int) (myHandler.getWidth() * 0.85), (int) (myHandler.getHeight() * 0.90), (int) (myHandler.getWidth() * 0.09),
//                (int) (myHandler.getHeight() * 0.07), myHandler, R.drawable.next);
//
//        previous = new Button((int) (myHandler.getWidth() * 0.08), (int) (myHandler.getHeight() * 0.90), (int) (myHandler.getWidth() * 0.10),
//                (int) (myHandler.getHeight() * 0.10), myHandler, R.drawable.previous);

        back = new Button((int) (myHandler.getWidth() * 0.01), (int) (myHandler.getHeight() * 0.01), (int) (myHandler.getWidth() * 0.05),
                (int) (myHandler.getHeight() * 0.05), myHandler, R.drawable.exit);

        width = this.myHandler.getWidth();
        height = this.myHandler.getHeight();

        if(myHandler.getGamePanel().isConnected()) {
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
        ArrayList<User> temp = this.myHandler.getGamePanel().getUsers();
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
            this.myHandler.getGamePanel().setCurrentState(this.myHandler.getGamePanel().getGameState());

            if(myHandler.getGamePanel().isConnected()) {
                this.myHandler.getGamePanel().getFirebase().start();
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

            if(user.getName().equals(this.myHandler.getGamePanel().getUsername())){
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
                if(usersList.get(i).getName().equals(this.myHandler.getGamePanel().getUsername())){
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
