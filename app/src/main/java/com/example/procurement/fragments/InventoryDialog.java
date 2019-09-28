package com.example.procurement.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.activities.RequisitionActivity;
import com.example.procurement.adapters.InventoryAdapter;
import com.example.procurement.adapters.InventoryDialogAdapter;
import com.example.procurement.adapters.RequisitionAdapter;
import com.example.procurement.models.Inventory;
import com.example.procurement.models.InventoryData;
import com.example.procurement.models.Requisition;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class InventoryDialog extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private ArrayList<Inventory> listData;
    private TextView btnSave,txtLoader;
    private SeekBar skCount;
    private InventoryData d,d1,d2,d3,d4;
    private InventoryDialogAdapter inventoryDialogAdapter;
    private Context c;
    private ImageView imgLoader;
    private ProgressBar progressBar;
    private CollectionReference inventoryRef;

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
        inventoryRef = FirebaseFirestore.getInstance().collection(CommonConstants.COLLECTION_INVENTORY);
        imgLoader = v.findViewById(R.id.imgInvLoader);
        txtLoader = v.findViewById(R.id.txtInvLoader);
        progressBar = v.findViewById(R.id.progressBar6);

        readInventoryData();
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

    private void readInventoryData() {
        inventoryRef.orderBy("itemNo").addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.v(TAG, "Listen Failed", e);
                        }

                        listData.clear();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Inventory inventory = document.toObject(Inventory.class);
                            listData.add(inventory);
                        }
                        Collections.reverse(listData);

                        if (listData != null) {
                            inventoryDialogAdapter = new InventoryDialogAdapter(c, listData);
                            progressBar.setVisibility(View.GONE);
                            imgLoader.setVisibility(View.INVISIBLE);
                            txtLoader.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(inventoryDialogAdapter);

                            if (listData.size() == 0) {
                                progressBar.setVisibility(View.GONE);
                                imgLoader.refreshDrawableState();
                                imgLoader.setImageResource(R.drawable.ic_white_box);
                                imgLoader.setVisibility(View.VISIBLE);
                                txtLoader.setVisibility(View.VISIBLE);
                                txtLoader.setText("Inventory is empty!");
                            }
                        }

                    }
                }
        );
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
