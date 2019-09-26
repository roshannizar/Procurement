package com.example.procurement.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.activities.RequisitionActivity;
import com.example.procurement.adapters.InventoryDialogAdapter;
import com.example.procurement.models.InventoryData;

import java.util.ArrayList;

public class InventoryDialog extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private ArrayList<InventoryData> listData;
    private TextView btnSave;
    private SeekBar skCount;
    private InventoryData d,d1,d2,d3,d4;
    private InventoryDialogAdapter inventoryDialogAdapter;
    private Context c;

    public InventoryDialog() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.layout_inventory_dialog, container, false);

        recyclerView = v.findViewById(R.id.checkBoxRecycle);
        skCount = v.findViewById(R.id.seekBar);
        c = v.getContext();
        listData = new ArrayList<>();
        inventoryDialogAdapter = new InventoryDialogAdapter(c,listData);
        btnSave = v.findViewById(R.id.btnSave);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        WriteDataValues();
        DialogChooser();
        return v;
    }

    private void DialogChooser() {
        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequisitionActivity.fm2.beginTransaction().replace(R.id.fragment_container_requisition,new RequisitionActivityFragment(),null).commit();
                    }
                }
        );
    }

    private void WriteDataValues() {

        d = new InventoryData("Sand Heap","7","22.0");
        d1 = new InventoryData("Bricks","10","45.0");
        d2 = new InventoryData("Cements","7","8.0");
        d3 = new InventoryData("Wall Putty","12","140.0");
        d4 = new InventoryData("Paint","8","0");

        listData.add(d);
        listData.add(d1);
        listData.add(d2);
        listData.add(d3);
        listData.add(d4);

        inventoryDialogAdapter = new InventoryDialogAdapter(c, listData);
        recyclerView.setAdapter(inventoryDialogAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
