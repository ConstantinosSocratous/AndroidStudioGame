package com.example.firstgame.Firebase;



import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/players");

    //TODO: FIND WAY TO HAVE USER NAME
    public static void addScore(String user, int score){
        mDatabase.child(user).setValue(score);
    }

}
