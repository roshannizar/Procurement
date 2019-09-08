package com.example.procurement;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.time.Year;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateOrderFragment extends Fragment {

    private EditText txtCurrentDate;

    public CreateOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_order, container, false);
        txtCurrentDate = v.findViewById(R.id.txtCurrentDate);

        setDate();
        return v;
    }

    @SuppressLint("SetTextI18n")
    private void setDate() {
        int year,date,month;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            year = Year.now().getValue();
        } else {
            year = Calendar.getInstance().get(Calendar.YEAR);
        }

        date = Calendar.getInstance().get(Calendar.DATE);
        month = Calendar.getInstance().get(Calendar.MONTH);
        txtCurrentDate.setText(month+"-"+date+"-"+year);
    }
}
