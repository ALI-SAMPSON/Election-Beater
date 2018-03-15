package com.example.icode.voteme.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.icode.voteme.R;

public class SplashScreenActivity extends AppCompatActivity {

    private final int SPLASH_SCREEN_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //creates an instance of an actionbar
        ActionBar actionBar = getSupportActionBar();
        //test to see if there is an actionbar
        if(actionBar != null){
            actionBar.hide();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Creates and start the intent of the next activity
                Intent startIntent = new Intent(SplashScreenActivity.this,SliderView.class);
                startActivity(startIntent);
                SplashScreenActivity.this.finish(); //this prevents the app from going back to the splash screen
            }
        }, SPLASH_SCREEN_DISPLAY_LENGTH);
    }
}
