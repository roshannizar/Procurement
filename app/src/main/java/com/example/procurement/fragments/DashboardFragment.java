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
import com.example.procurement.adapters.NotificationAdapter;
import com.example.procurement.models.Notification;
import com.example.procurement.models.Order;
import com.example.procurement.models.Requisition;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;


public class DashboardFragment extends Fragment {

    private int reqApproved = 0, reqHold = 0, reqPending = 0, reqDeclined = 0, orderApproved = 0, orderDraft = 0, orderPending = 0, orderDeclined = 0, orderPlaced = 0, reqTotalAmount;
    private ArrayList<Requisition> iRequisition;
    private ArrayList<Order> orders;

    private ArrayList<Notification> notificationList;
    private NotificationAdapter adapter;
    private RecyclerView recyclerView;
    private CollectionReference notificationDbRef, requisitionRef, orderDBRef, inventoryDBRef;

    private Context mContext;
    private ProgressBar progressBar;
    private TextView txtUserName, txtNoti, txtMonthDate, txtApproved, txtApprovedCount, txtHold, txtDeclined,
            txtDeclinedCount, txtPendingCount, txtPending, txtDraftCount, txtPlacedCount;
    private FirebaseAuth mAuth;
    private ImageView imgNoti;

    public DashboardFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mContext = view.getContext();

        mAuth = FirebaseAuth.getInstance();

        txtUserName = view.findViewById(R.id.txtUserName);
        txtMonthDate = view.findViewById(R.id.txtMonthData);
        progressBar = view.findViewById(R.id.progressBar2);
        txtNoti = view.findViewById(R.id.txtnoti);
        imgNoti = view.findViewById(R.id.imgnoti);

        txtApproved = view.findViewById(R.id.txtApproved);
        txtHold = view.findViewById(R.id.txtHold);
        txtDeclined = view.findViewById(R.id.txtDenied);
        txtPending = view.findViewById(R.id.txtPending);
        txtPendingCount = view.findViewById(R.id.txtPendingCount);
        txtDeclinedCount = view.findViewById(R.id.txtDeclineCount);
        txtDraftCount = view.findViewById(R.id.txtDraftCount);
        txtApprovedCount = view.findViewById(R.id.txtApproveCount);
        txtPlacedCount = view.findViewById(R.id.txtPlacedCounts);

        setDate();

        notificationDbRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_NOTIFICATION);
        requisitionRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_REQUISITION);
        orderDBRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_ORDER);
        inventoryDBRef = FirebaseFirestore.getInstance().collection(CommonConstants.COLLECTION_INVENTORIES);

        iRequisition = new ArrayList<>();
        orders = new ArrayList<>();
        notificationList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.rvNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ReadData();
        ReadRequisitionStatusCount();
        ReadOrderStatusCount();
        //writeData();
        return view;
    }

    private void writeData() {
        String key1 = notificationDbRef.document().getId();
        Notification notification1 = new Notification();
        notification1.setOrderKey("PKpB6EXFjb9JL11veL7x");
        notification1.setID("PO-1");
        notification1.setStatus(CommonConstants.ORDER_STATUS_APPROVED);
        notification1.setNotificationKey(key1);
        notificationDbRef.document(key1).set(notification1);

        String key2 = notificationDbRef.document().getId();
        Notification notification2 = new Notification();
        notification2.setRequisitionKey("V0mx632Hx2fWDk4svs7m");
        notification2.setID("REQ-1");
        notification2.setStatus(CommonConstants.ORDER_STATUS_APPROVED);
        notification2.setNotificationKey(key2);
        notificationDbRef.document(key2).set(notification2);
    }


    private void ReadData() {

        notificationDbRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                }

                notificationList.clear();

                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Notification notification = document.toObject(Notification.class);
                    notificationList.add(notification);
                }


                if (notificationList != null) {
                    Collections.reverse(notificationList);
                    adapter = new NotificationAdapter(notificationList);
                    progressBar.setVisibility(View.GONE);
                    txtNoti.setVisibility(View.INVISIBLE);
                    imgNoti.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(adapter);
                }

                assert notificationList != null;
                if (notificationList.size() == 0) {
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
                        //int sum = 0;
                        assert queryDocumentSnapshots != null;
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Requisition requisition = document.toObject(Requisition.class);

                           // reqTotalAmount = sum + Integer.parseInt(requisition.getTotalAmount());

                            switch (requisition.getRequisitionStatus()) {
                                case CommonConstants.REQUISITION_STATUS_APPROVED:
                                    reqApproved++;
                                    txtApproved.setText(String.valueOf(reqApproved));
                                    break;
                                case CommonConstants.REQUISITION_STATUS_PENDING:
                                    reqPending++;
                                    txtPending.setText(String.valueOf(reqPending));
                                    break;
                                case CommonConstants.REQUISITION_STATUS_DECLINED:
                                    reqDeclined++;
                                    txtDeclined.setText(String.valueOf(reqDeclined));
                                    break;
                                case CommonConstants.REQUISITION_STATUS_HOLD:
                                    reqHold++;
                                    txtHold.setText(String.valueOf(reqHold));
                                    break;
                                default:
                                    System.out.println("Nothing");
                            }
                        }
                    }
                }
        );
    }

    private void ReadOrderStatusCount() {
        orderDBRef.addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
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
                                    txtApprovedCount.setText(orderApproved + " Order(s) Approved");
                                    break;
                                case CommonConstants.ORDER_STATUS_PENDING:
                                    orderPending++;
                                    txtPendingCount.setText(orderPending + " Order(s) Pending");
                                    break;
                                case CommonConstants.ORDER_STATUS_DECLINED:
                                    orderDeclined++;
                                    txtDeclinedCount.setText(orderDeclined + " Order(s) Declined");
                                    break;
                                case CommonConstants.ORDER_STATUS_DRAFT:
                                    orderDraft++;
                                    txtDraftCount.setText(orderDraft + " Orders(s) Drafted");
                                    break;
                                case CommonConstants.ORDER_STATUS_PLACED:
                                    orderPlaced++;
                                    txtPlacedCount.setText(orderPlaced + " Order(s) Placed");
                                    break;
                                default:
                                    System.out.println("Nothing");
                            }
                        }
                    }
                }
        );
    }
}
