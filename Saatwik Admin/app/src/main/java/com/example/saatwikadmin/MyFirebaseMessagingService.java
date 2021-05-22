package com.example.saatwikadmin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingServce";
    final String CHANNEL_ID = "personal notifications";
    final int NOTIFICATION_ID = 001;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String notificationTitle = "New Order", notificationBody = "Chal kaamala laag";

        // Check if message contains a notification payload
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + notificationBody);
            notificationTitle = "New Order";
            notificationBody = "Chal kaamala laag";
        }

        // If you want to fire a local notification (that notification on the top of the phone screen)
        // you should fire it from here
        sendLocalNotification(notificationTitle, notificationBody);
    }

    private void sendLocalNotification(String notificationTitle, String notificationBody) {
//        Intent intent = new Intent(this, AdminActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(R.raw.ringtone);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setAutoCancel(true)   //Automatically delete the notification
//                .setSmallIcon(R.drawable.logo3) //Notification icon
//                .setContentIntent(pendingIntent)
//                .setContentTitle(notificationTitle)
//                .setContentText(notificationBody)
//                .setSound(defaultSoundUri);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(1234, notificationBuilder.build());
//
//        String channelName = context.getString(R.string.channel_name);

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.logo3);
        builder.setContentTitle("New Order Arrived");
        builder.setContentText("C'mmon be fast");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setSound(defaultSoundUri);

        Intent intent = new Intent(this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            CharSequence name = "Personal Notifications";
            String description = "Include all personal notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
