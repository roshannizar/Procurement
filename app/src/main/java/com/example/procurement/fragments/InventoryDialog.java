package com.example.procurement.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.adapters.DialogAdapter;
import com.example.procurement.models.InventoryData;

import java.util.ArrayList;
import java.util.List;

public class InventoryDialog extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private List<InventoryData> listData;
    private ProgressBar pbInventoryLevel;
    private SeekBar skCount;
    private DialogAdapter dialogAdapter;

    public InventoryDialog() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.layout_inventory_dialog, container, false);

        recyclerView = v.findViewById(R.id.checkBoxRecycle);
        pbInventoryLevel = v.findViewById(R.id.pbStockLevel);
        skCount = v.findViewById(R.id.seekBar);

        listData = new ArrayList<>();
        dialogAdapter = new DialogAdapter(getActivity(),listData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        WriteDataValues();
        return v;
    }

    private void WriteDataValues() {

        InventoryData d = new InventoryData("Bricks","5","10");
        listData.add(d);

        dialogAdapter = new DialogAdapter(getActivity(), listData);
        recyclerView.setAdapter(dialogAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
