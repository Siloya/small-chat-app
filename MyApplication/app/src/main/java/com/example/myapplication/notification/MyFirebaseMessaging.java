package com.example.myapplication.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;
import com.example.myapplication.specfiqchat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessaging extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "MyChannel"; // Replace with your own channel ID
    private static final String CHANNEL_NAME = "My Channel"; // Replace with your own channel name

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM message received here
        // You can customize the handling of the received message based on your requirements
        // TODO: Implement your desired logic here
        super.onMessageReceived(remoteMessage);
        String sented = remoteMessage.getData().get("sented");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null && sented.equals(firebaseUser.getUid())){
            // Example: Send a notification with the message body as the content
            sendNotification(remoteMessage);

        }


    }

    @Override
    public void onNewToken(String token) {
        // Handle token refresh here
        // You can send the updated token to your server or perform any other action with it
        // TODO: Implement your desired logic here
        super.onNewToken(token);
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Bundle bundle= new Bundle();
        bundle.putString("userid",user);
        Intent intent = new Intent(this, specfiqchat.class);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Build the notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24) // Set the small icon for the notification
                        .setContentTitle(title) // Set the title of the notification
                        .setContentText(body) // Set the message body of the notification
                        .setAutoCancel(true) // Allow auto-cancel when the notification is clicked
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

       int i = 0;
       if(j>0){
           i =j ;
       }

        // Show the notification
        notificationManager.notify(i /* ID of notification */, notificationBuilder.build());

        // Extract the message body from the remote message
       // String messageBody = remoteMessage.getNotification().getBody();

        // Create an Intent for the main activity of your app
       // Intent intent = new Intent(this, specfiqchat.class);
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
              //  PendingIntent.FLAG_ONE_SHOT);

      //  String channelId = getString(R.string.default_notification_channel_id); // Set your notification channel ID here
        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

       /* // Build the notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24) // Set the small icon for the notification
                        .setContentTitle(getString(R.string.app_name)) // Set the title of the notification
                        .setContentText(messageBody) // Set the message body of the notification
                        .setAutoCancel(true) // Allow auto-cancel when the notification is clicked
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);*/

        // Check if the device is running Android Oreo or higher
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }*/

        // Show the notification
        //notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
