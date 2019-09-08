package com.example.procurement;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.procurement.status.OrderStatusFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.Year;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private TextView txtUserName;
    private TextView txtMonthDate;
    private FirebaseAuth mAuth;

    public DashboardFragment() {

    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mAuth = FirebaseAuth.getInstance();
        txtUserName = v.findViewById(R.id.txtUserName);
        txtMonthDate = v.findViewById(R.id.txtMonthData);
        TextView txtApprovedCount = v.findViewById(R.id.txtApprovedCount);
        TextView txtHoldCount = v.findViewById(R.id.txtHoldCount);
        TextView txtPendingCount = v.findViewById(R.id.txtPendingCount);
        TextView txtTotalOrder = v.findViewById(R.id.txtTotalOrders);
        TextView txtPlacedCount = v.findViewById(R.id.txtPlacedCounts);
        txtPendingCount.setText(String.valueOf(OrderStatusFragment.pendingStatus));
        txtApprovedCount.setText(String.valueOf(OrderStatusFragment.approvedStatus));
        txtHoldCount.setText(String.valueOf(OrderStatusFragment.holdStatus));
        txtPlacedCount.setText(String.valueOf(OrderStatusFragment.placedStatus)+" Orders Placed");
        txtTotalOrder.setText(String.valueOf(OrderStatusFragment.approvedStatus+OrderStatusFragment.holdStatus+OrderStatusFragment.pendingStatus+OrderStatusFragment.declinedStatus+OrderStatusFragment.placedStatus)+" Orders Totally");
        setDate();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!= null) {
            txtUserName.setText("Hello " + currentUser.getUid() + "!");
            //txtUserName.setText(currentUser.getEmail());
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDate() {
        String year,date,month;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             year = String.valueOf(Year.now().getValue());
        } else {
            year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        }

        date = String.valueOf(Calendar.getInstance().get(Calendar.DATE));
        month = getMonthString(Calendar.getInstance().get(Calendar.MONTH));
        txtMonthDate.setText(date+"th "+month+" "+year);
    }

    private String getMonthString(int number) {
        String month;

        switch (number) {
            case 0: month = "January";
                    break;
            case 1: month = "February";
                    break;
            case 2: month = "March";
                    break;
            case 3: month = "April";
                    break;
            case 4: month = "May";
                    break;
            case 5: month = "June";
                    break;
            case 6: month = "July";
                    break;
            case 7: month = "August";
                    break;
            case 8: month = "September";
                    break;
            case 9: month = "October";
                    break;
            case 10: month = "November";
                    break;
                    default: month = "December";
                    break;
        }
        return month;
    }

}
