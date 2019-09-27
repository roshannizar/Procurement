package com.example.procurement.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.adapters.NoteAdapter;
import com.example.procurement.adapters.NotificationAdapter;
import com.example.procurement.models.Notification;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;


public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private ArrayList<Notification> notifications;
    private NotificationAdapter adapter;
    private RecyclerView recyclerView;
    private CollectionReference notificationDbRef;
    private Context mContext;
    private ProgressBar progressBar;
    private TextView txtUserName,txtNoti;
    private TextView txtMonthDate;
    private FirebaseAuth mAuth;
    private ImageView imgNoti;

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
        progressBar = view.findViewById(R.id.progressBar2);
        txtNoti = view.findViewById(R.id.txtnoti);
        imgNoti = view.findViewById(R.id.imgnoti);
        TextView txtApprovedCount = view.findViewById(R.id.txtApprovedCount);
        TextView txtHoldCount = view.findViewById(R.id.txtHoldCount);
        TextView txtPendingCount = view.findViewById(R.id.txtPendingCount);
        TextView txtTotalOrder = view.findViewById(R.id.txtTotalOrders);
        TextView txtPlacedCount = view.findViewById(R.id.txtPlacedCounts);
        txtPendingCount.setText(String.valueOf(OrderStatusFragment.pendingStatus));
        txtApprovedCount.setText(String.valueOf(OrderStatusFragment.approvedStatus));
        txtHoldCount.setText(String.valueOf(OrderStatusFragment.holdStatus));
        txtPlacedCount.setText(OrderStatusFragment.placedStatus + " Order(s) Placed");
        txtTotalOrder.setText((OrderStatusFragment.approvedStatus + OrderStatusFragment.holdStatus + OrderStatusFragment.pendingStatus + OrderStatusFragment.declinedStatus + OrderStatusFragment.placedStatus) + " Order(s) Totally");
        setDate();

        notificationDbRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_NOTIFICATION);

        notifications = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rvNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        readData();
        // writeData();
        return view;
    }

//    private void writeData() {
//        DatabaseReference reference = notificationDbRef.push();
//        String key = reference.getKey();
//
//        if (key != null) {
//            Notification notification = new Notification("PO-01", "-LovkdDnlrX7ikscLBUl", CommonConstants.ORDER_STATUS_PLACED);
//            notification.setNotificationKey(key);
//            notificationDbRef.child(key).setValue(notification);
//        }
//    }

    private void readData() {

        notificationDbRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                }

                notifications.clear();

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Notification notification = document.toObject(Notification.class);
                    notifications.add(notification);
                }


                if (notifications != null) {
                    Collections.reverse(notifications);
                    adapter = new NotificationAdapter(mContext, notifications);
                    progressBar.setVisibility(View.GONE);
                    txtNoti.setVisibility(View.INVISIBLE);
                    imgNoti.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(adapter);
                }

                if(notifications.size() == 0) {
                    imgNoti.setImageResource(R.drawable.ic_email);
                    imgNoti.setVisibility(View.VISIBLE);
                    txtNoti.setVisibility(View.VISIBLE);
                    txtNoti.setText("Wohoo! No Notifications");
                }
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
            txtUserName.setText("Hi " + currentUser.getEmail() + "!");
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
