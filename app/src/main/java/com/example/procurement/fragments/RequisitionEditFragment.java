package com.example.procurement.fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.procurement.R;
import com.example.procurement.activities.HomeActivity;
import com.example.procurement.activities.RequisitionActivity;
import com.example.procurement.adapters.InventoryAdapter;
import com.example.procurement.adapters.RequisitionAdapter;
import com.example.procurement.adapters.SupplierAdapter;
import com.example.procurement.models.Inventory;
import com.example.procurement.models.Requisition;
import com.example.procurement.models.Supplier;
import com.example.procurement.utils.CommonConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;
import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;
import static com.example.procurement.utils.CommonConstants.COLLECTION_SUPPLIERS;
import static com.example.procurement.utils.CommonConstants.REQUISITION_ID;


public class RequisitionEditFragment extends Fragment {

    private RecyclerView recyclerView,recyclerViewSupplier;
    private Context c;
    private RadioGroup radioGroup;
    private InventoryAdapter inventoryAdapter;
    private SupplierAdapter supplierAdapter;
    private Requisition requisition;
    private DatePickerDialog picker;
    private EditText txtRequisitionNo,txtPurpose,txtComments,txtReason;
    private TextView txtDeliveryDate,txtTotalAmount,btnAddItems,txtStatus,txtProposalDate,txtProposedBy,btnAddSupplier;
    private String RADIO="";
    private Button btnUpdateRequisition,btnCancelRequisition;
    private DocumentReference requisitionRef;
    private CollectionReference  supplierDBRef, inventoryDBRef, requisitionUpdateRef;
    private String requisitionKey;
    private Spinner spRecommendedSupplier;
    private ProgressBar createProgressBar;

    public RequisitionEditFragment(String requisitionKey) {
        this.requisitionKey = requisitionKey;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_requisition_edit, container, false);

        requisitionRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_REQUISITION).document(requisitionKey);
        inventoryDBRef = requisitionRef.collection(CommonConstants.COLLECTION_INVENTORIES);
        supplierDBRef = requisitionRef.collection(CommonConstants.COLLECTION_SUPPLIERS);
        requisitionUpdateRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_REQUISITION);

        txtRequisitionNo = v.findViewById(R.id.txtRequisitionIdEdit);
        txtPurpose = v.findViewById(R.id.txtPurposeEdit);
        txtComments = v.findViewById(R.id.txtCommentsEdit);
        txtDeliveryDate = v.findViewById(R.id.txtDeliveryDateEdit);
        txtTotalAmount = v.findViewById(R.id.txtTotalAmountEdit);
        radioGroup = v.findViewById(R.id.budgetRadioEdit);
        btnAddItems = v.findViewById(R.id.btnAddItemsEdit);
        txtStatus = v.findViewById(R.id.txtStatusEdit);
        recyclerView = v.findViewById(R.id.recyclerItemsEdit);

        recyclerViewSupplier = v.findViewById(R.id.recyclerSupplierEdit);
        btnUpdateRequisition = v.findViewById(R.id.btnPlaceRequisitionEdit);
        btnCancelRequisition = v.findViewById(R.id.btnBackRequisitionEdit);
        txtReason = v.findViewById(R.id.txtReasonEdit);
        txtProposalDate = v.findViewById(R.id.txtProposalDateEdit);
        txtProposedBy = v.findViewById(R.id.txtProposedByEdit);
        createProgressBar = v.findViewById(R.id.createProgressBarEdit);
        createProgressBar.setVisibility(View.INVISIBLE);
        btnAddSupplier = v.findViewById(R.id.btnAddSupplierEdit);
        spRecommendedSupplier = v.findViewById(R.id.spRecommendedSupplierEdit);

        c= v.getContext();

        inventoryAdapter = new InventoryAdapter(CommonConstants.iInventory,"Requisition");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        supplierAdapter = new SupplierAdapter(CommonConstants.iSupplier);
        recyclerViewSupplier.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        recyclerViewSupplier.setItemAnimator(new DefaultItemAnimator());

        ReadRequisitionData();
        PopUpItems();
        ShowDialog();
        CheckRadio();
        setTotalAmountBadge();
        WriteDataValues();
        WriteSupplierDataValues();
        RecommendedSupplier();
        onUpdateData();
        return v;
    }

    private void RecommendedSupplier() {

        if(CommonConstants.iSupplier != null) {
            ArrayList<String> arraylist = new ArrayList<>();

            arraylist.add("-- Please Choose a supplier --");
            for (int i = 0; i < CommonConstants.iSupplier.size(); i++) {

                Supplier supplier = CommonConstants.iSupplier.get(i);

                arraylist.add(supplier.getSupplierName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, arraylist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRecommendedSupplier.setAdapter(adapter);
        }
    }

    private void ReadRequisitionData() {
        requisitionRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                requisition = new Requisition();
                requisition = documentSnapshot.toObject(Requisition.class);

                if (requisition != null) {

                    txtRequisitionNo.setText(requisition.getRequisitionNo());
                    txtDeliveryDate.setText(requisition.getDeliveryDate());
                    txtPurpose.setText(requisition.getPurpose());
                    txtStatus.setText(requisition.getRequisitionStatus());
                    txtTotalAmount.setText(requisition.getTotalAmount());
                    txtComments.setText(requisition.getComment());
                    txtReason.setText(requisition.getReason());
                    txtProposalDate.setText(requisition.getProposalDate());
                    txtProposedBy.setText(requisition.getProposedBy());
                    RADIO = requisition.getBudget();

                    if(RADIO.equals("Yes")) {
                        radioGroup.check(R.id.radioYes);
                    } else {
                        radioGroup.check(R.id.radioNo);
                    }
                }
            }
        });

        inventoryDBRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                }

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Inventory inventory = document.toObject(Inventory.class);
                        CommonConstants.iInventory.add(inventory);
                    }


                    if (CommonConstants.iInventory != null) {
                        inventoryAdapter = new InventoryAdapter(CommonConstants.iInventory, "Requisition");
                        recyclerView.setAdapter(inventoryAdapter);
                    }

                }
            }
        });

        supplierDBRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                }

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Supplier supplier = document.toObject(Supplier.class);
                        CommonConstants.iSupplier.add(supplier);
                    }


                    if (CommonConstants.iSupplier != null) {
                        supplierAdapter = new SupplierAdapter(CommonConstants.iSupplier);
                        recyclerViewSupplier.setAdapter(supplierAdapter);
                    }

                }
            }
        });
    }

    private void onUpdateData() {

        btnUpdateRequisition.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Validation()) {

                            Requisition requisition = new Requisition(txtRequisitionNo.getText().toString(), txtComments.getText().toString(), txtPurpose.getText().toString(), txtDeliveryDate.getText().toString(), txtTotalAmount.getText().toString(), txtStatus.getText().toString(), txtReason.getText().toString(), txtProposalDate.getText().toString(), txtProposedBy.getText().toString(), RADIO);
                            requisition.setKey(requisitionKey);
                            requisitionUpdateRef.document(requisitionKey)
                                    .set(requisition)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                                                    .setTitle("ALERT")
                                                    .setMessage("Requisition Updated Successfully!")
                                                    .setCancelable(false)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                                    }).show();
                                            //HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderViewFragment(order.getOrderKey()), null).commit();
                                            Log.d(Constraints.TAG, "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(Constraints.TAG, "Error writing document", e);
                                        }
                                    });

                        } else {
                            new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                                    .setTitle("ALERT")
                                    .setMessage("Fill the required fields!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).show();
                        }
                    }
                }
        );
    }

    private void PopUpItems() {
        btnAddItems.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HomeActivity.fm.beginTransaction().replace(R.id.fragment_container,new InventoryDialog(),null).commit();
                    }
                }
        );
    }

    @SuppressLint("SetTextI18n")
    private void setTotalAmountBadge() {

        if(CommonConstants.iInventory!= null) {
            double value = 0.0;
            boolean checkEmptyUnitPrice = false;

            for (int i = 0; i < CommonConstants.iInventory.size(); i++) {
                Inventory inventory = CommonConstants.iInventory.get(i);
                value = value + inventory.getUnitprice() * inventory.getQuantity();

                System.out.println(inventory.getUnitprice());

                if (inventory.getUnitprice() == 0) {
                    checkEmptyUnitPrice = true;
                }
            }

            txtTotalAmount.setText("Rs: " + value);

            if (value > 100000.0 || checkEmptyUnitPrice) {
                txtStatus.setText(R.string.hold);
                txtStatus.setBackgroundResource(R.drawable.badge_hold);
            } else {
                txtStatus.setText(R.string.pending);
                txtStatus.setBackgroundResource(R.drawable.badge_pending);
            }
        }
    }

    private void CheckRadio() {
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        if(checkedId == R.id.radioYes) {
                            RADIO = "Yes";
                        } else {
                            RADIO = "No";
                        }
                    }
                }
        );
    }

    private void WriteDataValues() {
        inventoryAdapter = new InventoryAdapter(CommonConstants.iInventory,"Requisition");
        inventoryAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(inventoryAdapter);
    }


    private void WriteSupplierDataValues() {
        supplierAdapter = new SupplierAdapter(CommonConstants.iSupplier);
        recyclerViewSupplier.setAdapter(supplierAdapter);
        supplierAdapter.notifyDataSetChanged();
    }


    private void ShowDialog() {
        txtDeliveryDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Calendar c = Calendar.getInstance();
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int month = c.get(Calendar.MONTH);
                        int year = c.get(Calendar.YEAR);
                        picker = new DatePickerDialog(Objects.requireNonNull(getContext()),
                                new DatePickerDialog.OnDateSetListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        txtDeliveryDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                }
        );
    }

    private boolean Validation() {

        boolean value = true;

        if(txtRequisitionNo.getText().toString().equals("")) {
            txtRequisitionNo.setBackgroundResource(R.drawable.text_box_empty);
            value = false;
        } else {
            if(txtPurpose.getText().toString().equals("")) {
                txtRequisitionNo.setBackgroundResource(R.drawable.text_box);
                txtPurpose.setBackgroundResource(R.drawable.text_box_empty);
                value = false;

            } else {
                if(txtComments.getText().toString().equals("")) {
                    txtPurpose.setBackgroundResource(R.drawable.text_box);
                    txtComments.setBackgroundResource(R.drawable.text_box_empty);
                    value = false;
                } else if(CommonConstants.iInventory.size() == 0) {
                    txtComments.setBackgroundResource(R.drawable.text_box);
                    recyclerView.setBackgroundResource(R.drawable.text_box_empty);
                    value = false;
                } else {

                    if(RADIO.equals("")) {
                        recyclerView.setBackgroundResource(R.drawable.text_box);
                        radioGroup.setBackgroundResource(R.drawable.text_box_empty);
                        value = false;
                    }
                }
            }
        }

        return value;
    }
}
