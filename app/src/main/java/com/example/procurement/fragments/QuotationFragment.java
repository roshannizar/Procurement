package com.example.procurement.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procurement.R;
import com.example.procurement.activities.RequisitionActivity;
import com.example.procurement.models.Order;
import com.example.procurement.models.Requisition;
import com.example.procurement.utils.CommonConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;

public class QuotationFragment extends Fragment {

    private Button btnPlacedRequisition,btnBackRequisition;
    private EditText txtReason;
    private TextView txtProposalDate,txtProposedBy;
    private CollectionReference requisitionRef;

    public QuotationFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_quotation, container, false);

        btnPlacedRequisition = v.findViewById(R.id.btnPlaceRequisition2);
        btnBackRequisition = v.findViewById(R.id.btnBackRequisition);
        txtReason = v.findViewById(R.id.txtReason);
        txtProposalDate = v.findViewById(R.id.txtProposalDate);
        txtProposedBy = v.findViewById(R.id.txtProposedBy);
        requisitionRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_REQUISITION);
        PlaceOrder();
        GoToRequisition();

        return v;
    }

    private void WriteStatus() {
        String key = requisitionRef.document().getId();
        Requisition requisition = new Requisition(RequisitionActivityFragment.REQUISITION_NO, RequisitionActivityFragment.COMMENTS, RequisitionActivityFragment.PURPOSE, RequisitionActivityFragment.DELIVERY_DATE, RequisitionActivityFragment.TOTAL_AMOUNT,CommonConstants.ORDER_STATUS_PENDING,txtReason.getText().toString(),txtProposalDate.getText().toString(),txtProposedBy.getText().toString());
        requisition.setKey(key);
        requisitionRef.document(key)
                .set(requisition)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),"Requisition placed successfully!",Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Requisition placed successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Error while placing the document",Toast.LENGTH_LONG).show();
                        Log.w(TAG, "Error while placing the document", e);
                    }
                });
    }

    private void PlaceOrder() {
        btnPlacedRequisition.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WriteStatus();
                    }
                }
        );
    }

    private void GoToRequisition() {
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