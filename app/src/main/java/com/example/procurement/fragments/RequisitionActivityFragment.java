package com.example.procurement.fragments;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.procurement.R;
import com.example.procurement.activities.RequisitionActivity;
import com.example.procurement.adapters.InventoryAdapter;
import com.example.procurement.models.Inventory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.procurement.utils.CommonConstants.REQUISITION_ID;

public class RequisitionActivityFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Inventory> iInventory;
    private Inventory i;
    private Context c;
    private InventoryAdapter inventoryAdapter;
    private DatePickerDialog picker;
    private Button btnQuotation;
    private EditText txtRequisitionNo,txtPurpose,txtComments;
    private TextView txtDeliveryDate,txtTotalAmount,btnGenerate;
    static String REQUISITION_NO,PURPOSE,COMMENTS,DELIVERY_DATE,TOTAL_AMOUNT;

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
        btnGenerate = v.findViewById(R.id.btnGenerateRequisition);

        recyclerView = v.findViewById(R.id.recyclerItems);
        iInventory = new ArrayList<>();
        c= v.getContext();
        inventoryAdapter = new InventoryAdapter(c,iInventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        GoToQuotation();
        ShowDialog();
        GetGenerateID();
        GenerateID();
        WriteDataValues();
        return v;
    }

    private void WriteDataValues() {

        for(int j=1;j<=5;j++) {
            i = new Inventory(String.valueOf(j),"Sand Heap","",7,2);
            iInventory.add(i);
        }


        inventoryAdapter = new InventoryAdapter(c, iInventory);
        recyclerView.setAdapter(inventoryAdapter);
    }

    private void GenerateID() {
        btnGenerate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setGenerateID(txtRequisitionNo.getText().toString());
                    }
                }
        );
    }

    private void GetGenerateID() {
        Pattern p = Pattern.compile("\\d+");
        String generateNo = null;
        if (REQUISITION_ID != null) {
            Matcher m = p.matcher(REQUISITION_ID);

            while (m.find()) {
                generateNo = m.group();
            }
            int value = Integer.parseInt(generateNo) + 1;
            txtRequisitionNo.setText("REQ-" + value);
        }
    }

    //Validation needed
    private void setGenerateID(String value) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(value);
        while (m.find()) {
            value = m.group();
        }

        int finalValue = Integer.parseInt(value) + 1;
        String temp = "REQ-" + finalValue;

        txtRequisitionNo.setText(temp);
        REQUISITION_ID = temp;
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
