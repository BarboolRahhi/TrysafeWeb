package com.trysafe.trysafe.FirebaseServices;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.trysafe.trysafe.MainActivity;
import com.trysafe.trysafe.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirebaseService extends FirebaseMessagingService {

    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData() != null) {

            String imagrUri = remoteMessage.getData().get("image");
            String title = remoteMessage.getData().get("title");
            String about = remoteMessage.getData().get("about");
            bitmap = getBitmapfromUri(imagrUri);
            sendNotification(title, about, bitmap);
        }
    }

    private Bitmap getBitmapfromUri(String imagrUri) {
        try {
            URL url = new URL(imagrUri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendNotification(String title, String message, Bitmap image) {
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotification")
                .setLargeIcon(image)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.store_icon)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image))
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent)
                .setContentText(message);

        NotificationManagerCompat manger = NotificationManagerCompat.from(this);
        manger.notify(0, builder.build());
    }
}
