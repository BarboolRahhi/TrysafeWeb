package com.trysafe.trysafe;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        final  FirebaseUser currentUser = firebaseAuth.getCurrentUser();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser == null){
                    startActivity(new Intent(splashActivity.this,RegisterActivity.class));
                    finish();
                }else {

                    startActivity(new Intent(splashActivity.this,MainActivity.class));
                    finish();
                }
            }
        },1500);



    }
}
