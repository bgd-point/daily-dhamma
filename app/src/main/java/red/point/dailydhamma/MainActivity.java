package red.point.dailydhamma;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import red.point.dailydhamma.R;
import red.point.dailydhamma.fragment.AboutFragment;
import red.point.dailydhamma.fragment.DailyFragment;
import red.point.dailydhamma.fragment.ListFragment;
import red.point.dailydhamma.fragment.RandomFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Fragment fragment;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    public FragmentTransaction transaction;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);





        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.requestLayout();

        // Bottom navigation listener
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.getMenu().clear();
        bottomNavigation.inflateMenu(R.menu.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.action_daily){
                    fragment = new DailyFragment();
                } else if (id == R.id.action_list) {
                    fragment = new ListFragment();
                } else if (id == R.id.action_random) {
                    fragment = new RandomFragment();
                }

                /*switch (id){
                    case R.id.action_daily:
                        fragment = new DailyFragment();
                        break;
                    case R.id.action_list:
                        fragment = new ListFragment();
                        break;
                    case R.id.action_random:
                        fragment = new RandomFragment();
                        break;
                }*/

                fragmentManager. popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });

        DatabaseReference questionAnswerRef = database.getReference("question-answer");

        // The Firebase Realtime Database synchronizes and stores a local copy of the data for active listeners
        questionAnswerRef.keepSynced(true);

        // Save device token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get the token
                    SettingsActivity.token = task.getResult();
                    Log.d(TAG, "FCM Token: " + SettingsActivity.token);

                    // Use the token as needed (e.g., send it to your server)
                });

//        String token = FirebaseInstanceId.getInstance().getToken();
        final DatabaseReference devicesRef = database.getReference("devices/token/" + SettingsActivity.token);
        Log.d(TAG, "FCM Token 2: " + SettingsActivity.token);
        devicesRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (!dataSnapshot.hasChild("language")) {
                        devicesRef.child("language").setValue(Lang_Indonesian? "id": "en");
                    }
                }
                else {
                    devicesRef.child("font_size").setValue("Small");
                    devicesRef.child("notification_enable").setValue(true);
                    devicesRef.child("notification_time").setValue("06:00");
                    devicesRef.child("language").setValue(Lang_Indonesian? "id": "en");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, new DailyFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.activity_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about){
            fragment = new AboutFragment();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            finish();
            startActivity(intent);
            return true;
        }

        /*switch (id){
            case R.id.nav_about:
                fragment = new AboutFragment();
                break;

            case R.id.nav_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                finish();
                startActivity(intent);
                return true;
        }*/

        DrawerLayout drawer = findViewById(R.id.activity_main);
        drawer.closeDrawer(GravityCompat.START);

        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();
        return true;
    }
}
