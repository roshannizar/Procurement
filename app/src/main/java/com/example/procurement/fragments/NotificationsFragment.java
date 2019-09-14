package com.example.procurement.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.adapters.NotificationsAdapter;
import com.example.procurement.models.Notification;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class NotificationsFragment extends Fragment {
    private static final String TAG = "NotificationsFragment";

    private ArrayList<Notification> notifications;
    private NotificationsAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private DatabaseReference myRef;
    private Context mContext;
    private SimpleDateFormat format;

    public NotificationsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        mContext = rootView.getContext();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(CommonConstants.FIREBASE_NOTIFICATION_DB);
        format = new SimpleDateFormat("dd-MM-yyyy");

        notifications = new ArrayList<>();

        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.rvNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //writeStatusData();
        readStatusData();

        return rootView;
    }

    private void readStatusData() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                notifications.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Notification notification = data.getValue(Notification.class);

                    if (notification != null) {
                        notifications.add(notification);

//                        try {
//                            Date orderDate = format.parse(notification.getOrderDate());
//                            Date currentDate = format.parse(new Date().toString());
//                            if (currentDate.equals(orderDate)) {
//                                notifications.add(notification);
//                            }
//                        } catch (ParseException error) {
//                            Log.w(TAG, "Failed to read value.", error.getCause());
//                        }

                    }
                }

                if (notifications != null) {
                    Collections.reverse(notifications);
                    adapter = new NotificationsAdapter(mContext, notifications);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void writeStatusData() {
        Notification createNotification = new Notification("1df", "Praveen", CommonConstants.ORDER_STATUS_PENDING, "21-09-2019");
        myRef.push().setValue(createNotification);
    }
}