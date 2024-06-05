package com.example.knowitall;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.knowitall.ui.admin.HomePageAdmin;
import com.example.knowitall.ui.login.LoginActivity;

public class Splash extends AppCompatActivity {
    private TextView appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
       appName= findViewById(R.id.appname);
        Typeface  typeface = ResourcesCompat.getFont(this,R.font.bropella);
        appName.setTypeface(typeface);

        Animation animation= AnimationUtils.loadAnimation(this, R.anim.myanima);
        appName.setAnimation(animation);
        new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Splash.this, LoginActivity.class);
                startActivity(intent);
                Splash.this.finish();
            }
        }.start();


    }
}