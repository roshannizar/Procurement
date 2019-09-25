package com.example.procurement;

import android.app.Application;

import com.example.procurement.activities.SignInActivity;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.procurement.utils.CommonConstants.COLLECTION_SITE_MANGER;
import static com.example.procurement.utils.CommonConstants.DOCUMENT_EID;

public class PMS extends Application {
    public static DatabaseReference DatabaseRef;
    public static DocumentReference siteManagerDBRef;

    @Override
    public void onCreate() {
        super.onCreate();
        SignInActivity.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseRef = database.getReference(CommonConstants.USER_DB_URL);
        siteManagerDBRef = FirebaseFirestore.getInstance().collection(COLLECTION_SITE_MANGER).document(DOCUMENT_EID);
    }
}
