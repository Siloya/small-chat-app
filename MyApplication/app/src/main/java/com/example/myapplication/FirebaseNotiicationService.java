package com.example.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.notification.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class FirebaseNotiicationService extends FirebaseMessagingService {
    String CHANNEL_ID = "1000";
    FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Handle FCM message received here
        // You can customize the handling of the received message based on your requirements
        super.onMessageReceived(remoteMessage);
        //  String sented = remoteMessage.getData().get("sented");
        // firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //  if(firebaseUser != null && sented.equals(firebaseUser.getUid())){
        // Example: Send a notification with the message body as the content
        //sendNotification(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> map = remoteMessage.getData();

                Log.d("TAG", "onMessageReceived: Chat Notification");

                String title = map.get("title");
                String message = map.get("message");
                String hisID = map.get("hisID");
                String hisImage = map.get("myImage");
                String chatID = map.get("chatID");
               String hisName = map.get("hisName");

                Log.d("TAG", "onMessageReceived: chatID is " + chatID + "\n hisID" + hisID);
                    createNormalNotification(title, message, hisID, hisImage, chatID, hisName);
            }
         else Log.d("TAG", "onMessageReceived: no data ");

    }

    @Override
    public void onNewToken(String token) {
        // Handle token refresh here
        // You can send the updated token to your server or perform any other action with it
        // TODO: Implement your desired logic here
        super.onNewToken(token);
        updatetoken(token);
    }

    private void updatetoken (String refreshtoken){
       // FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        //DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("token");
        Token token =new Token(refreshtoken);
        databaseReference.setValue(token);

    }

    private void createNormalNotification(String title, String message, String hisID, String hisImage, String chatID,String hisName) {

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setColor(ResourcesCompat.getColor(getResources(), R.color.purple_200, null))
                .setSound(uri);

        Intent intent = new Intent(this,specfiqchat.class);
        intent.putExtra("title", title );
        intent.putExtra("chatID", chatID);
        intent.putExtra("hisID", hisID);
        intent.putExtra("hisImage", hisImage);
        intent.putExtra("hisImage", hisName);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(new Random().nextInt(85 - 65), builder.build());

    }












}
