package red.point.dailydhamma;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class DailyDhamma extends Application {
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
