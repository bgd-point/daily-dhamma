package red.point.dailydhamma;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import red.point.dailydhamma.fragment.AboutFragment;
import red.point.dailydhamma.fragment.DailyFragment;
import red.point.dailydhamma.fragment.ListFragment;
import red.point.dailydhamma.fragment.RandomFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MyActivity";
    private BottomNavigationView bottomNavigation;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.requestLayout();

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
                    case R.id.action_random:
                        fragment = new RandomFragment();
                        break;
                }

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
        String token = FirebaseInstanceId.getInstance().getToken();
        DatabaseReference devicesRef = database.getReference("devices/token/" + token);
        devicesRef.child("notification_enable").setValue(true);
        devicesRef.child("notification_time").setValue("06:00");

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, new DailyFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
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

        switch (id){
            case R.id.nav_about:
                fragment = new AboutFragment();
                break;

            case R.id.nav_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                finish();
                startActivity(intent);
                return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        drawer.closeDrawer(GravityCompat.START);

        fragmentManager. popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();
        return true;
    }
}
