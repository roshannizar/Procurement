package com.example.procurement.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procurement.R;
import com.example.procurement.activities.RequisitionActivity;
import com.example.procurement.adapters.InventoryAdapter;
import com.example.procurement.adapters.SupplierAdapter;
import com.example.procurement.models.Inventory;
import com.example.procurement.models.Order;
import com.example.procurement.models.Requisition;
import com.example.procurement.models.Supplier;
import com.example.procurement.utils.CommonConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.Calendar;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;

public class QuotationFragment extends Fragment {

    private RecyclerView recyclerView;
    private Supplier s;
    private SupplierAdapter supplierAdapter;
    private Context context;
    private Button btnPlacedRequisition,btnBackRequisition;
    private EditText txtReason;
    private TextView txtProposalDate,txtProposedBy,btnAddSupplier;
    private CollectionReference requisitionRef;
    private final Calendar c = Calendar.getInstance();
    private ProgressBar createProgressBar;

    public QuotationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quotation, container, false);

        btnPlacedRequisition = v.findViewById(R.id.btnPlaceRequisition2);
        btnBackRequisition = v.findViewById(R.id.btnBackRequisition);
        txtReason = v.findViewById(R.id.txtReason);
        txtProposalDate = v.findViewById(R.id.txtProposalDate);
        txtProposedBy = v.findViewById(R.id.txtProposedBy);
        createProgressBar = v.findViewById(R.id.createProgressBar);
        createProgressBar.setVisibility(View.INVISIBLE);
        btnAddSupplier = v.findViewById(R.id.btnAddSupplier);

        requisitionRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_REQUISITION);

        recyclerView = v.findViewById(R.id.recyclerSupplier);
        context = v.getContext();
        supplierAdapter = new SupplierAdapter(context, CommonConstants.iSupplier);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        PlaceOrder();
        GoToRequisition();
        setProposalDate();
        WriteDataValues();
        AddSupplier();

        return v;
    }


    private void AddSupplier() {
        btnAddSupplier.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequisitionActivity.fm2.beginTransaction().replace(R.id.fragment_container_requisition,new SupplierDialogFragment(),null).commit();
                    }
                }
        );
    }

    private void WriteDataValues() {
        supplierAdapter = new SupplierAdapter(context, CommonConstants.iSupplier);
        recyclerView.setAdapter(supplierAdapter);
        supplierAdapter.notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    private void setProposalDate() {
        txtProposalDate.setText(c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR));
    }

    private void WriteStatus() {
        createProgressBar.setVisibility(View.VISIBLE);
        String key = requisitionRef.document().getId();
        Requisition requisition = new Requisition(RequisitionActivityFragment.REQUISITION_NO, RequisitionActivityFragment.COMMENTS, RequisitionActivityFragment.PURPOSE, RequisitionActivityFragment.DELIVERY_DATE, RequisitionActivityFragment.TOTAL_AMOUNT,CommonConstants.ORDER_STATUS_PENDING,txtReason.getText().toString(),txtProposalDate.getText().toString(),txtProposedBy.getText().toString(),RequisitionActivityFragment.RADIO);
        requisition.setKey(key);
        requisitionRef.document(key)
                .set(requisition)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),"Requisition placed successfully!",Toast.LENGTH_LONG).show();
                        createProgressBar.setVisibility(View.INVISIBLE);
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
