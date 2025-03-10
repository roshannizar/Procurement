package com.example.procurement;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.procurement.activities.SignInActivity;
import com.example.procurement.models.Inventory;
import com.example.procurement.models.Notification;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;

public class PMS extends Application {

    CollectionReference notificationDbRef,  inventoryDBRef;

    @Override
    public void onCreate() {
        super.onCreate();
        SignInActivity.getCurrentUser();

        if(siteManagerDBRef!= null) {

            notificationDbRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_NOTIFICATION);
            inventoryDBRef = FirebaseFirestore.getInstance().collection(CommonConstants.COLLECTION_INVENTORIES);

            inventoryDBRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                    }

                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Inventory inventory = document.toObject(Inventory.class);

                        if (inventory.getQuantity() < 10) {
                            String key = notificationDbRef.document().getId();
                            Notification notification = new Notification();
                            notification.setID("Inventory Low: "+inventory.getQuantity());
                            notification.setStatus("Replenish Inventory: "+inventory.getItemName());
                            notification.setNotificationKey(key);
                            notificationDbRef.document(key).set(notification);
                        }
                    }

                }
            });
        }
    }
}
