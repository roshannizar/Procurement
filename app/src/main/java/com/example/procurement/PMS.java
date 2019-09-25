package com.example.procurement;

import android.app.Application;

import com.example.procurement.activities.SignInActivity;

public class PMS extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SignInActivity.getCurrentUser();
    }
}
