package com.example.procurement;

import android.app.Application;

import com.example.procurement.activities.SignInActivity;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PMS extends Application {
    public static DatabaseReference DatabaseRef;

    @Override
    public void onCreate() {
        super.onCreate();
        SignInActivity.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseRef = database.getReference(CommonConstants.USER_DB_URL);
    }
}
