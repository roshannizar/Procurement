package com.example.procurement.fragments;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.procurement.R;
import com.example.procurement.activities.RequisitionActivity;
import com.google.firestore.v1.Write;

public class RequisitionActivityFragment extends Fragment {

    private Button btnQuotation;
    private EditText txtRequisitionNo,txtPurpose,txtComments;
    private TextView txtDeliveryDate,txtTotalAmount;
    public static String REQUISITION_NO,PURPOSE,COMMENTS,DELIVERY_DATE,TOTAL_AMOUNT;

    public RequisitionActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_requisition, container, false);

        btnQuotation = v.findViewById(R.id.btnQuotation);
        txtRequisitionNo = v.findViewById(R.id.txtRequisitionNo);
        txtPurpose = v.findViewById(R.id.txtPurpose);
        txtComments = v.findViewById(R.id.txtComments);
        txtDeliveryDate = v.findViewById(R.id.txtDeliveryDate);
        txtTotalAmount = v.findViewById(R.id.txtTotalAmount);

        GoToQuotation();

        return v;
    }

    private void GoToQuotation() {
        btnQuotation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //REQUISITION_NO = txtRequisitionNo.getText().toString();
                        REQUISITION_NO = "REQ-1809";
                        PURPOSE = txtPurpose.getText().toString();
                        COMMENTS = txtComments.getText().toString();
                        DELIVERY_DATE = txtDeliveryDate.getText().toString();
                        TOTAL_AMOUNT = txtTotalAmount.getText().toString();

                        RequisitionActivity.fm2.beginTransaction().replace(R.id.fragment_container_requisition, new QuotationFragment(), null).commit();
                    }
                }
        );
    }
}
