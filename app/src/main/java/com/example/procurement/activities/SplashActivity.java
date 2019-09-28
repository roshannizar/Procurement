package com.example.procurement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.procurement.R;
import com.example.procurement.utils.CommonConstants;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, SignInActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(i);
                finish();
            }
        }, CommonConstants.WELCOME_TIMEOUT);
    }
}
