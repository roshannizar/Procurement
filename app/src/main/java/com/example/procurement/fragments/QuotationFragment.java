package com.example.procurement.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.procurement.R;
import com.example.procurement.activities.RequisitionActivity;

public class QuotationFragment extends Fragment {

    private Button btnPlacedRequisition,btnBackRequisition;

    public QuotationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_quotation, container, false);

        btnPlacedRequisition = v.findViewById(R.id.btnPlaceRequisition2);
        btnBackRequisition = v.findViewById(R.id.btnBackRequisition);

        PlaceOrder();
        GoToRequisition();

        return v;
    }

    private void PlaceOrder() {
        btnPlacedRequisition.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"Created Successfully!",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void GoToRequisition() {
        btnBackRequisition.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequisitionActivity.fm2.beginTransaction().replace(R.id.fragment_container_requisition, new RequisitionActivityFragment(), null).commit();
                    }
                }
        );
    }

}
