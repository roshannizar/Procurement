package com.example.procurement.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.procurement.models.InventoryData;
import com.example.procurement.models.Supplier;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class SupplierDialogFragment extends Fragment {

    private TextView btnSave;
    private RecyclerView recyclerView;
    private ArrayList<Supplier> iSupplier;
    private Supplier d,d1,d2,d3,d4;
    private SupplierDialogAdapter supplierDialogAdapter;
    private Context c;

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

        BackToQuotation();
        WriteDataStatus();

        return v;
    }

    private void WriteDataStatus() {

        d = new Supplier("HuHu","12/12/2019","","Active");
        d1 = new Supplier("Pee","12/12/2019","","Active");
        d2 = new Supplier("Vola","12/12/2019","","Active");
        d3 = new Supplier("Santa","12/12/2019","","Active");
        d4 = new Supplier("Amigo","12/12/2019","","Active");

        iSupplier.add(d);
        iSupplier.add(d1);
        iSupplier.add(d2);
        iSupplier.add(d3);
        iSupplier.add(d4);

        supplierDialogAdapter = new SupplierDialogAdapter(c, iSupplier);
        recyclerView.setAdapter(supplierDialogAdapter);
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
