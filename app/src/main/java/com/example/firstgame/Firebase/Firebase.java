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

    private DatabaseReference database;
    private ArrayList<String> nicknames = new ArrayList<>();
    private ArrayList<User> users;

    public Firebase(){
        database = FirebaseDatabase.getInstance().getReference("/players");
    }

    public void addScore(String user, int score) {
        database.child(user).setValue(score);
        start();
    }

    public ArrayList<User> getUsers(){
        return users;
    }

    public ArrayList<String> getNicknames(){
        return nicknames;
    }

    public void start(){

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                users = new ArrayList<>();
                nicknames = new ArrayList<>();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    nicknames.add(postSnapshot.getKey());
                    users.add(new User(postSnapshot.getKey(), Integer.parseInt(postSnapshot.getValue().toString())));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("","Error");
            }
        });

    }
}
