/*package com.example.myapplication.notification;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseIdService extends FirebaseInstanceIdService{
    @Override
    public void onTokenRefresh() {

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FIREBASE_TOKEN", refreshedToken);

        if (firebaseUser != null){
            updatetoken(refreshedToken);

        }

    }
    private void updatetoken (String refreshtoken){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token =new Token(refreshtoken);
        reference.child(firebaseUser.getUid()).setValue(token);

    }
}*/