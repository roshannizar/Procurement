package com.example.procurement.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.adapters.NotificationsAdapter;
import com.example.procurement.models.Notification;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private ArrayList<Notification> notifications;
    private NotificationsAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private Context mContext;

    private TextView txtUserName;
    private TextView txtMonthDate;
    private FirebaseAuth mAuth;

    public DashboardFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mContext = view.getContext();

        mAuth = FirebaseAuth.getInstance();
        txtUserName = view.findViewById(R.id.txtUserName);
        txtMonthDate = view.findViewById(R.id.txtMonthData);
        TextView txtApprovedCount = view.findViewById(R.id.txtApprovedCount);
        TextView txtHoldCount = view.findViewById(R.id.txtHoldCount);
        TextView txtPendingCount = view.findViewById(R.id.txtPendingCount);
        TextView txtTotalOrder = view.findViewById(R.id.txtTotalOrders);
        TextView txtPlacedCount = view.findViewById(R.id.txtPlacedCounts);
        txtPendingCount.setText(String.valueOf(OrderStatusFragment.pendingStatus));
        txtApprovedCount.setText(String.valueOf(OrderStatusFragment.approvedStatus));
        txtHoldCount.setText(String.valueOf(OrderStatusFragment.holdStatus));
        txtPlacedCount.setText(String.valueOf(OrderStatusFragment.placedStatus) + " Orders Placed");
        txtTotalOrder.setText(String.valueOf(OrderStatusFragment.approvedStatus + OrderStatusFragment.holdStatus + OrderStatusFragment.pendingStatus + OrderStatusFragment.declinedStatus + OrderStatusFragment.placedStatus) + " Orders Totally");
        setDate();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(CommonConstants.FIREBASE_NOTIFICATION_DB);

        notifications = new ArrayList<>();

        recyclerView = view.findViewById(R.id.rvNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        readStatusData();

        return view;
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

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            txtUserName.setText("Hello " + currentUser.getEmail() + "!");
            //txtUserName.setText(currentUser.getEmail());
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDate() {
        String year, date, month;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            year = String.valueOf(Year.now().getValue());
        } else {
            year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        }

        date = String.valueOf(Calendar.getInstance().get(Calendar.DATE));
        month = getMonthString(Calendar.getInstance().get(Calendar.MONTH));
        txtMonthDate.setText(date + "th " + month + " " + year);
    }

    private String getMonthString(int number) {
        String month;

        switch (number) {
            case 0:
                month = "January";
                break;
            case 1:
                month = "February";
                break;
            case 2:
                month = "March";
                break;
            case 3:
                month = "April";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "June";
                break;
            case 6:
                month = "July";
                break;
            case 7:
                month = "August";
                break;
            case 8:
                month = "September";
                break;
            case 9:
                month = "October";
                break;
            case 10:
                month = "November";
                break;
            default:
                month = "December";
                break;
        }
        return month;
    }

}
