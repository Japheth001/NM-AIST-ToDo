package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

//import android.support.v4.media.MediaBrowserCompat;


public class SplashActivity extends AppCompatActivity {
    ImageView smile;
    Animation smileBlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //smile=(ImageView)findViewById(R.id.smilePic);
       // smileBlink();

        getSupportActionBar().hide();

        Thread splashThread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(5000);
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //super.run();
            }
        };
        splashThread.start();
    }

    //private void smileBlink() {
       // smileBlink= AnimationUtils.loadAnimation(this,R.anim.blink);
       // smile.startAnimation(smileBlink);
    //}
}














