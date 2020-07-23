package com.mrc.reports.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GenericTransitionOptions;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrc.reports.BaseActivity;
import com.mrc.reports.R;

public class Splash_ui extends BaseActivity {

    ImageView mLogo;
//    TextView mTitle;

    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_ui);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLogo = findViewById(R.id.logo);
//        mTitle = findViewById(R.id.title);

//        mTitle.setTypeface(Raleway_Medium);

        //Displaying Logo
        Glide.with(this)
                .load(R.mipmap.logo_icon)
                .transition(GenericTransitionOptions.<Drawable>with(R.anim.blink_anim))
                .into(mLogo);


        new Handler().postDelayed(new Runnable(){

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                //This method will be executed once the timer is over
                //Start your app main activity
                //check if LanguagePreference has anything if YES then we use the saved value to select the Language

                //TODO:Uncomment This
                startActivity(new Intent(Splash_ui.this, SignIn_ui.class));

                //Close the Activity
                finish();

            }
        }, SPLASH_TIME_OUT);


    }

}
