package com.desafiolatam.carcollectionv2.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Pedro on 04-07-2017.
 */

public class CurrentUser {
    private FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
    public String uid(){return current.getUid();}
    public DatabaseReference getReference(){
        return FirebaseDatabase.getInstance().getReference().child(uid());
    }
}
