package com.example.procurement;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.time.Year;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private TextView txtUserName,txtMonthDate;

    public DashboardFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        txtUserName = v.findViewById(R.id.txtUserName);
        txtMonthDate = v.findViewById(R.id.txtMonthData);
        setUserName();
        SigninActivity.SignOutUserFirebase();
        setDate();
        return v;
    }

    @SuppressLint("SetTextI18n")
    private void setUserName() {
        txtUserName.setText("Hello "+SigninActivity.name+"!");
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
        String month ="";

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
