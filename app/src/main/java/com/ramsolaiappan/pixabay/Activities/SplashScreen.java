package com.ramsolaiappan.pixabay.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.ramsolaiappan.pixabay.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Set theme stored in sharedpref
        String theme = getSharedPreferences("settings", MODE_PRIVATE).getString("theme","Light");
        AppCompatDelegate.setDefaultNightMode((theme.equals("Default")) ? AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM : (theme.equals("Light") ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES));


        ImageView iv = (ImageView)  findViewById(R.id.logoIV);

        iv.setAlpha(0f);
        iv.animate().alphaBy(1).setDuration(2000).withEndAction(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                intent.putExtra("query","");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}