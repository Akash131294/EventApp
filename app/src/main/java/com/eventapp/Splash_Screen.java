package com.eventapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash_Screen extends AppCompatActivity {

    Handler handler;
    Runnable runnable;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = this;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, Home.class);
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(runnable,5000);
    }

    @Override
    protected void onPause() {
        if (handler!=null && runnable!=null )
            handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handler.postDelayed(runnable,5000);
    }
}
