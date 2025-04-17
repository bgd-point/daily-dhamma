package red.point.dailydhamma;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import red.point.dailydhamma.utils.NotificationManagerHandler;


public class DailyDhamma extends Application {

    public NotificationManagerHandler notifHandler;

    public void onCreate() {
        super.onCreate();

        notifHandler = new NotificationManagerHandler(getApplicationContext());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        notifHandler.createNotificationChannel();
    }
}
