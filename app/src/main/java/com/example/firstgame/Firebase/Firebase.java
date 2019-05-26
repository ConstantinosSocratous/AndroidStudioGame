package com.example.firstgame.Firebase;



import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Firebase {

    public static DatabaseReference database = FirebaseDatabase.getInstance().getReference("/players");

    public static void addScore(String user, int score) {
        database.child(user).setValue(score);
    }

}
