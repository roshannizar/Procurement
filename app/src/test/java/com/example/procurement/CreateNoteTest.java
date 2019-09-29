package com.example.procurement;

import android.app.Application;

import com.example.procurement.fragments.NoteFragment;
import com.example.procurement.models.Order;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.junit.Before;
import org.junit.Test;

public class CreateNoteTest extends Application {
//    private CollectionReference testDBRef;
//    private FirebaseFirestore firestore;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        FirebaseApp.initializeApp(this);
//    }
//
//    @Before
//    public void setup() {
//        firestore = FirebaseFirestore.getInstance();
//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().build();
//        firestore.setFirestoreSettings(settings);
//    }
//
//    @Test
//    public void createNoteTest() {
//        testDBRef = firestore.collection("test");
//
//        final String key = testDBRef.document().getId();
//        final Order order = new Order();
//        order.setOrderKey(key);
//
//        testDBRef.document(key).set(order);
//
//        NoteFragment noteFragment = new NoteFragment(order.getOrderKey());
//        noteFragment.createNote("INV-1", "100 brick arrived");
//    }

}
