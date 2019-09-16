package com.example.procurement;

import android.app.Application;

import com.example.procurement.utils.CommonConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PMS extends Application {
    public static DatabaseReference orderDatabaseRef, NotesDatabaseRef, NotificationDatabaseRef;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        orderDatabaseRef = database.getReference(CommonConstants.USER_DB_URL + CommonConstants.FIREBASE_ORDER_DB);


    }


}
