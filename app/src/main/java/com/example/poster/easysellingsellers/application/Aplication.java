package com.example.poster.easysellingsellers.application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by User on 017 17.08.17.
 */

public class Aplication extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
