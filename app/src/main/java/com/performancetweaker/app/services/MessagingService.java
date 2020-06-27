package com.performancetweaker.app.services;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.performancetweaker.app.R;
import com.performancetweaker.app.ui.MainActivity;
import com.performancetweaker.app.utils.Constants;

public class MessagingService extends FirebaseMessagingService {
    private static final int NOTIFICATION_ID = 6578;

    @Override
    public void onNewToken(String token) {
        Log.d(Constants.App_Tag, "Refreshed token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Log.d(Constants.App_Tag, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            showNotifications(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

    }

    private void showNotifications(String title, String msg) {
        Intent i = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
                i, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentText(msg)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
    }

}
