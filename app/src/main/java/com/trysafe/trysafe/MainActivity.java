package com.trysafe.trysafe;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.trysafe.trysafe.Models.NotesModel;
import com.trysafe.trysafe.SqlLiteHelper.DBManager;

import java.util.List;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;


public class MainActivity extends AppCompatActivity {

    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new SearchFragment();
    final Fragment fragment3 = new NotesFragment();
    final Fragment fragment4 = new FeedsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;


    public static final String HOME = "Home";
    public static final String SEARCH = "Other";
    public static final String FEED = "Other";

    public static int currentFragment = -1;


    private ImageView mLogo;
    private ImageView mProfile;
    RelativeLayout toolBarLayout;


    FrameLayout frameLayout;

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("MyNotification", "MyNotification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfull";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        frameLayout = findViewById(R.id.fragment_container);

        bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        fm.beginTransaction().add(R.id.fragment_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment1, "1").commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //  Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.home:

                            fm.beginTransaction().hide(active).show(fragment1).commit();
                            active = fragment1;
                            return true;

                        case R.id.search:

                            fm.beginTransaction().hide(active).show(fragment2).commit();
                            active = fragment2;
                            return true;

                        case R.id.notes:

                            fm.beginTransaction().hide(active).show(fragment3).commit();
                            active = fragment3;
                            return true;

                        case R.id.feed:

                            fm.beginTransaction().hide(active).show(fragment4).commit();
                            active = fragment4;
                            return true;
                    }

                    return false;
                }
            };


    @Override
    public void onBackPressed() {
        if (bottomNav.getSelectedItemId() == R.id.home) {
            super.onBackPressed();
        } else {
            bottomNav.setSelectedItemId(R.id.home);
        }
    }

}
