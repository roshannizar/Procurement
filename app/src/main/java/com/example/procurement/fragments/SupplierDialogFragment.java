package com.example.procurement.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.procurement.R;
import com.example.procurement.activities.RequisitionActivity;
import com.example.procurement.adapters.InventoryDialogAdapter;
import com.example.procurement.adapters.SupplierAdapter;
import com.example.procurement.adapters.SupplierDialogAdapter;
import com.example.procurement.models.Inventory;
import com.example.procurement.models.InventoryData;
import com.example.procurement.models.Supplier;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class SupplierDialogFragment extends Fragment {

    private TextView btnSave;
    private RecyclerView recyclerView;
    private ArrayList<Supplier> iSupplier;
    private Supplier d,d1,d2,d3,d4;
    private SupplierDialogAdapter supplierDialogAdapter;
    private Context c;
    private CollectionReference supplierRef;

    public SupplierDialogFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_supplier_dialog, container, false);

        recyclerView = v.findViewById(R.id.checkBoxRecycleSupplier);
        iSupplier = new ArrayList<>();
        c = v.getContext();
        supplierDialogAdapter = new SupplierDialogAdapter(c, iSupplier);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        btnSave = v.findViewById(R.id.btnSave2);
        supplierRef = FirebaseFirestore.getInstance().collection(CommonConstants.COLLECTION_SUPPLIERS);

        BackToQuotation();
        WriteDataStatus();

        return v;
    }

    private void WriteDataStatus() {

        supplierRef.orderBy("supplierId").addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.v(TAG, "Listen Failed", e);
                        }

                        iSupplier.clear();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Supplier supplier = document.toObject(Supplier.class);
                            iSupplier.add(supplier);
                        }
                        Collections.reverse(iSupplier);

                        if (iSupplier != null) {
                            supplierDialogAdapter = new SupplierDialogAdapter(c, iSupplier);
//                            progressBar.setVisibility(View.GONE);
//                            imgLoader.setVisibility(View.INVISIBLE);
//                            txtLoader.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(supplierDialogAdapter);

//                            if (listData.size() == 0) {
//                                progressBar.setVisibility(View.GONE);
//                                imgLoader.refreshDrawableState();
//                                imgLoader.setImageResource(R.drawable.ic_white_box);
//                                imgLoader.setVisibility(View.VISIBLE);
//                                txtLoader.setVisibility(View.VISIBLE);
//                                txtLoader.setText("Inventory is empty!");
//                            }
                        }
                    }
                }
        );
    }

    private void BackToQuotation() {
        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequisitionActivity.fm2.beginTransaction().replace(R.id.fragment_container_requisition, new QuotationFragment(),null).commit();
                    }
                }
        );
    }
}
