package com.example.procurement;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.procurement.activities.SignInActivity;
import com.example.procurement.adapters.RequisitionAdapter;
import com.example.procurement.models.Requisition;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;

public class PMS extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SignInActivity.getCurrentUser();
    }
}
