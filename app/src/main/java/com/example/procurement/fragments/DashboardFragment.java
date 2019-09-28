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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.adapters.NotificationAdapter;
import com.example.procurement.models.Notification;
import com.example.procurement.models.Order;
import com.example.procurement.models.Requisition;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private int reqApproved, reqHold, reqPending, reqDeclined, orderApproved, orderDraft, orderPending, orderDeclined, orderPlaced, reqTotalAmount;
    private CollectionReference requisitionRef, orderDBRef;
    private ArrayList<Requisition> iRequisition;
    private ArrayList<Order> orders;

    private static final String TAG = "DashboardFragment";
    private ArrayList<Notification> notifications;
    private NotificationAdapter adapter;
    private RecyclerView recyclerView;
    private CollectionReference notificationDbRef;
    private Context mContext;
    private ProgressBar progressBar;
    private TextView txtUserName, txtNoti, txtMonthDate, txtApprovedCount, txtHoldCount, txtDeclinedCount, txtPendingCount, txtPending, txtDecline, txtDraft, txtTotalOrder, txtPlacedCount;
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

        txtApprovedCount = view.findViewById(R.id.txtApprovedCount);
        txtHoldCount = view.findViewById(R.id.txtHoldCount);
        txtDeclinedCount = view.findViewById(R.id.txtDeniedCount);
        txtPendingCount = view.findViewById(R.id.txtPendingCount);
        txtPending = view.findViewById(R.id.txtPending);
        txtDecline = view.findViewById(R.id.txtDecline);
        txtDraft = view.findViewById(R.id.txtDraftCount);
        txtTotalOrder = view.findViewById(R.id.txtTotalOrders);
        txtPlacedCount = view.findViewById(R.id.txtPlacedCounts);

        setDate();

        notificationDbRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_NOTIFICATION);
        requisitionRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_REQUISITION);
        orderDBRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_ORDER);

        iRequisition = new ArrayList<>();
        orders = new ArrayList<>();
        notifications = new ArrayList<>();

        recyclerView = view.findViewById(R.id.rvNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ReadData();
        ReadRequisitionStatusCount();
        ReadOrderStatusCount();

        return view;
    }

//    private void writeData() {
//        String key = notificationDbRef.document().getId();
//        Notification notification = new Notification("PO-01", "3L22ABg6ttMLkzKEeSIm", CommonConstants.ORDER_STATUS_APPROVED);
//        notification.setNotificationKey(key);
//        notificationDbRef.document(key).set(notification);
//    }

    @SuppressLint("SetTextI18n")
    private void SetCount() {
        txtPendingCount.setText(String.valueOf(reqPending));
        txtApprovedCount.setText(String.valueOf(reqApproved));
        txtHoldCount.setText(String.valueOf(reqHold));
        txtDeclinedCount.setText(String.valueOf(reqDeclined));
        //progressBar.setProgress(StatusCountUtil.reqTotalAmount);
        txtPlacedCount.setText(orderApproved + " Order(s) Approved");
        txtTotalOrder.setText(orderPlaced + " Order(s) Placed");
        txtPending.setText(orderPending + " Order(s) Pending");
        txtDecline.setText(orderDeclined + " Order(s) Declined");
        txtDraft.setText(orderDraft + " Orders(s) Drafted");
    }

    private void ReadData() {

        notificationDbRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                }

                notifications.clear();

                assert queryDocumentSnapshots != null;
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

                assert notifications != null;
                if (notifications.size() == 0) {
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

    private void ReadRequisitionStatusCount() {
        requisitionRef.addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.v(TAG, "Listen Failed", e);
                        }

                        iRequisition.clear();
                        int sum = 0;
                        assert queryDocumentSnapshots != null;
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Requisition requisition = document.toObject(Requisition.class);

                            //reqTotalAmount = sum+Integer.parseInt(requisition.getTotalAmount());

                            switch (requisition.getRequisitionStatus()) {
                                case CommonConstants.REQUISITION_STATUS_APPROVED:
                                    reqApproved++;
                                    System.out.println("Approved: " + reqApproved);
                                    break;
                                case CommonConstants.REQUISITION_STATUS_PENDING:
                                    reqPending++;
                                    System.out.println("Pending: " + reqPending);
                                    break;
                                case CommonConstants.REQUISITION_STATUS_DECLINED:
                                    reqDeclined++;
                                    break;
                                case CommonConstants.REQUISITION_STATUS_HOLD:
                                    reqHold++;
                                    break;
                                default:
                                    System.out.println("Nothing");
                            }
                        }

                        SetCount();

                    }
                }
        );
    }

    private void ReadOrderStatusCount() {
        orderDBRef.addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.v(TAG, "Listen Failed", e);
                        }

                        orders.clear();

                        assert queryDocumentSnapshots != null;
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Order ordersCount = document.toObject(Order.class);

                            switch (ordersCount.getOrderStatus()) {
                                case CommonConstants.ORDER_STATUS_APPROVED:
                                    orderApproved++;
                                    break;
                                case CommonConstants.ORDER_STATUS_PENDING:
                                    orderPending++;
                                    break;
                                case CommonConstants.ORDER_STATUS_DECLINED:
                                    orderDeclined++;
                                    break;
                                case CommonConstants.ORDER_STATUS_DRAFT:
                                    orderDraft++;
                                    break;
                                case CommonConstants.ORDER_STATUS_PLACED:
                                    orderPlaced++;
                                    break;
                                default:
                                    System.out.println("Nothing");
                            }
                        }

                        SetCount();

                    }
                }
        );
    }
}
