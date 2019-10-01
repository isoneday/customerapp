package com.imastudio.customerapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.imastudio.customerapp.helper.SessionManager;

public class SplashScreenActivity extends AppCompatActivity {

    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        manager = new SessionManager(this);
        //untuk penundaan ke activity /action
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (manager.isLogin()==true){
                    startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SplashScreenActivity.this,LoginRegisterActivity.class));
                    finish();
                }
            }
        }, 3000);
    }
}
