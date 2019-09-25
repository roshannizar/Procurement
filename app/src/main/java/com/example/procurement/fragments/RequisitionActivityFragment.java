package com.example.procurement.fragments;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.procurement.R;
import com.example.procurement.activities.RequisitionActivity;

public class RequisitionActivityFragment extends Fragment {

    private Button btnQuotation;


    public RequisitionActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_requisition, container, false);

        btnQuotation = v.findViewById(R.id.btnQuotation);

        GoToQuotation();

        return v;
    }

    private void GoToQuotation() {
        btnQuotation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequisitionActivity.fm2.beginTransaction().replace(R.id.fragment_container_requisition, new QuotationFragment(), null).commit();
                    }
                }
        );
    }
}
