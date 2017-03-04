package red.point.dailydhamma;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import red.point.dailydhamma.fragment.DailyFragment;
import red.point.dailydhamma.fragment.FavoriteFragment;
import red.point.dailydhamma.fragment.ListFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    public FragmentTransaction transaction;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        // Bottom navigation listener
        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigation.getMenu().clear();
        bottomNavigation.inflateMenu(R.menu.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id){
                    case R.id.action_daily:
                        fragment = new DailyFragment();
                        break;
                    case R.id.action_list:
                        fragment = new ListFragment();
                        break;
                    case R.id.action_favorite:
                        fragment = new FavoriteFragment();
                        break;
                }

                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });

        DatabaseReference questionAnswerRef = database.getReference("question-answer");

        // The Firebase Realtime Database synchronizes and stores a local copy of the data for active listeners
        questionAnswerRef.keepSynced(true);

        // Save device token
        String token = FirebaseInstanceId.getInstance().getToken();
        DatabaseReference devicesRef = database.getReference("devices/token/" + token);
        devicesRef.setValue(true);

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, new DailyFragment()).commit();
    }
}
