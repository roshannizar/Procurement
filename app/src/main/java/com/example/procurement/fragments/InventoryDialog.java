package com.example.procurement.fragments;

import android.content.Context;
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
    private ArrayList<InventoryData> listData;
    private SeekBar skCount;
    private InventoryData d;
    private DialogAdapter dialogAdapter;
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
        dialogAdapter = new DialogAdapter(c,listData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        WriteDataValues();
        return v;
    }

    private void WriteDataValues() {

        for(int i=0;i<5;i++) {
            d = new InventoryData("Sand Heap","7","2");
            listData.add(d);
        }


        dialogAdapter = new DialogAdapter(c, listData);
        recyclerView.setAdapter(dialogAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
