package com.example.poster.easysellingsellers.lisntener;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by User on 018 18.08.17.
 */

public abstract class MyValueEventListener implements ValueEventListener {
    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
