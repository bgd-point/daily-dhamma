package red.point.dailydhamma;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class DailyDhamma extends Application {
    public void onCreate() {
        super.onCreate();

        // your app writes the data locally to the device so your app can maintain state while
        // offline, even if the user or operating system restarts the app
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
