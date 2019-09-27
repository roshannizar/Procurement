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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;
import static com.example.procurement.utils.CommonConstants.COLLECTION_REQUISITION;
import static com.example.procurement.utils.CommonConstants.COLLECTION_REQUISITION_INVENTORY;
import static com.example.procurement.utils.CommonConstants.COLLECTION_REQUISITION_SUPPLIER;
import static com.example.procurement.utils.CommonConstants.COLLECTION_SITE_MANGER;
import static com.example.procurement.utils.CommonConstants.iInventory;
import static com.example.procurement.utils.CommonConstants.iSupplier;

public class QuotationFragment extends Fragment {

    private RecyclerView recyclerView;
    private Supplier s;
    private SupplierAdapter supplierAdapter;
    private Context context;
    private Button btnPlacedRequisition,btnBackRequisition;
    private EditText txtReason;
    private TextView txtProposalDate,txtProposedBy,btnAddSupplier;
    private CollectionReference requisitionRef,inventoryRef,supplierRef;
    private final Calendar c = Calendar.getInstance();
    private ProgressBar createProgressBar;
    private FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();
        requisitionRef = siteManagerDBRef.collection(COLLECTION_REQUISITION);

        recyclerView = v.findViewById(R.id.recyclerSupplier);
        context = v.getContext();
        supplierAdapter = new SupplierAdapter(context, CommonConstants.iSupplier);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        PlaceOrder();
        GoToRequisition();
        setProposalDate();
        WriteDataValues();
        AddSupplier();

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
        if (currentUser != null) {
            txtProposedBy.setText(currentUser.getEmail());
        }
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
                        //Toast.makeText(getContext(),"Requisition placed successfully!",Toast.LENGTH_LONG).show();
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

        //Add Inventory Bulk Insert
        inventoryRef = siteManagerDBRef.collection(COLLECTION_REQUISITION).document(key).collection(COLLECTION_REQUISITION_INVENTORY);
        supplierRef = siteManagerDBRef.collection(COLLECTION_REQUISITION).document(key).collection(COLLECTION_REQUISITION_SUPPLIER);

        for(int i=0;i<iInventory.size();i++) {

            Inventory inventoryList = iInventory.get(i);
            String inventoryKey = inventoryRef.document().getId();
            Inventory inventory = new Inventory(inventoryList.getItemNo(),inventoryList.getItemName(),"",inventoryList.getQuantity(),inventoryList.getUnitprice());
            requisition.setKey(inventoryKey);
            inventoryRef.document(inventoryKey)
                    .set(inventory)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Toast.makeText(getContext(),"Inventory placed successfully!",Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Inventory placed successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"Error while placing the inventory",Toast.LENGTH_LONG).show();
                            Log.w(TAG, "Error while placing the document", e);
                        }
                    });
        }

        //Add Supplier Bulk Insert
        for(int i =0;i<iSupplier.size();i++) {
            Supplier supplierList = iSupplier.get(i);
            String supplierKey = supplierRef.document().getId();
            Supplier supplier = new Supplier(supplierList.getSupplierName(),supplierList.getExpectedDate(),supplierList.getOffer(),supplierList.getSupplierStatus());
            requisition.setKey(supplierKey);
            supplierRef.document(supplierKey)
                    .set(supplier)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(),"Requisition placed successfully!",Toast.LENGTH_LONG).show();
                            createProgressBar.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "Supplier placed successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"Error while placing the inventory",Toast.LENGTH_LONG).show();
                            Log.w(TAG, "Error while placing the document", e);
                        }
                    });
        }

        CommonConstants.iInventory.clear();
        CommonConstants.iSupplier.clear();

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
