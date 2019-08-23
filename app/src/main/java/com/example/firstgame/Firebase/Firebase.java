package com.example.firstgame.Firebase;



import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Firebase{

    private DatabaseReference updatedDatabase;
    private ArrayList<String> nicknames = new ArrayList<>();
    private ArrayList<User> users;

    public Firebase(){
    }

    public void addScoreUpdated(String user, int score) {
        updatedDatabase = FirebaseDatabase.getInstance().getReference("/playersUpdated/" + user);
        updatedDatabase.child("score").setValue(score);
        start();
    }

    public void addTimesPlayed(String user, int times) {
        updatedDatabase = FirebaseDatabase.getInstance().getReference("/playersUpdated/" + user);
        updatedDatabase.child("timesPlayed").setValue(times);
        start();
    }

    public ArrayList<User> getUsers(){
        return users;
    }

    public ArrayList<String> getNicknames(){
        return nicknames;
    }

    public void start(){
       readData();
    }

    public void readData(){
        updatedDatabase = FirebaseDatabase.getInstance().getReference("/playersUpdated");

        updatedDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                users = new ArrayList<>();
                nicknames = new ArrayList<>();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    nicknames.add(postSnapshot.getKey());
                    long score = (long) postSnapshot.child("score").getValue();
                    long timesPlayed= (long) postSnapshot.child("timesPlayed").getValue();

                    users.add(new User(postSnapshot.getKey(), (int)score, (int) timesPlayed));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("","Error");
            }
        });

    }
}
