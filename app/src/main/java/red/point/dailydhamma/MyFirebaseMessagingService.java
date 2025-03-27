package red.point.dailydhamma;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import red.point.dailydhamma.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String title = "", body = "", imageUrl = "";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        /*Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent mainActivity = new Intent(this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(this, 0, mainActivity,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setSmallIcon(R.drawable.ic_stupa_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapfromUrl(remoteMessage.getData().get("image"))))
                .setColor(getResources().getColor(R.color.colorNotification))
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setContentIntent(mainActivityPendingIntent)
                .build();


        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.notify(0, notification);*/

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent mainActivity = new Intent(this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(
                this,
                0,
                mainActivity,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        if (!remoteMessage.getData().isEmpty()){
//            title = remoteMessage.getData().get("title");
//            body = remoteMessage.getData().get("body");
            imageUrl = remoteMessage.getData().get("image");
        }

        if (remoteMessage.getNotification() != null){
            title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
            body = remoteMessage.getNotification().getBody();
        }


        if (title == null) title = "Default Title";
        if (body == null) body = "Default Body";

        Bitmap largeImage = null;
        if (imageUrl != null && !imageUrl.isEmpty()) {
            largeImage = getBitmapfromUrl(imageUrl);
        } else {
            Log.d("debug-check", "Image URL is null or empty");
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default_channel")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_stupa_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
                .setColor(ContextCompat.getColor(this, R.color.colorNotification))
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(mainActivityPendingIntent);


        if (largeImage != null) {
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(largeImage));
        }

        Notification notification = builder.build();

        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("debug-check", "Notification permission not granted");
            return;
        }

        manager.notify(0, notification);


    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()){
            return null;
        }
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default_channel";
            CharSequence channelName = "Default Channel";
            String channelDescription = "This is the default notification channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
