package com.example.procurement.fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.procurement.R;
import com.example.procurement.activities.RequisitionActivity;
import com.example.procurement.adapters.InventoryAdapter;
import com.example.procurement.models.Inventory;
import com.example.procurement.utils.CommonConstants;

import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.procurement.utils.CommonConstants.REQUISITION_ID;

public class RequisitionActivityFragment extends Fragment {

    private RecyclerView recyclerView;
    private Inventory i;
    private Context c;
    private RadioGroup radioGroup;
    private InventoryAdapter inventoryAdapter;
    private DatePickerDialog picker;
    private Button btnQuotation;
    private EditText txtRequisitionNo,txtPurpose,txtComments;
    private TextView txtDeliveryDate,txtTotalAmount,btnGenerate,btnAddItems,txtStatus;
    static String REQUISITION_NO= REQUISITION_ID,PURPOSE,COMMENTS="",DELIVERY_DATE="",TOTAL_AMOUNT="",RADIO="",REQUISITION_STATUS="";

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
        radioGroup = v.findViewById(R.id.budgetRadio);
        btnAddItems = v.findViewById(R.id.btnAddItems);
        txtStatus = v.findViewById(R.id.txtStatus);
        recyclerView = v.findViewById(R.id.recyclerItems);

        c= v.getContext();
        inventoryAdapter = new InventoryAdapter(c,CommonConstants.iInventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        GoToQuotation();
        ShowDialog();
        GetGenerateID();
        GenerateID();
        WriteDataValues();
        CheckRadio();
        setTotalAmountBadge();
        //CheckValueInConstant();
        PopUpItems();

        return v;
    }

    private void PopUpItems() {
        btnAddItems.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequisitionActivity.fm2.beginTransaction().replace(R.id.fragment_container_requisition,new InventoryDialog(),null).commit();
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
        inventoryAdapter = new InventoryAdapter(c, CommonConstants.iInventory);
        inventoryAdapter.notifyDataSetChanged();
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

    @SuppressLint("SetTextI18n")
    private void GetGenerateID() {
        Pattern p = Pattern.compile("\\d+");
        String generateNo = null;
        if (REQUISITION_ID != null) {
            Matcher m = p.matcher(REQUISITION_ID);

            while (m.find()) {
                generateNo = m.group();
            }
            assert generateNo != null;
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

    private void CheckValueInConstant() {
        if(REQUISITION_NO.equals("REQ-00")) {
            setGenerateID(txtRequisitionNo.getText().toString());
        } else if(PURPOSE.equals("")) {
            txtRequisitionNo.setText(REQUISITION_NO);
            txtPurpose.setText("");
        } else if(COMMENTS.equals("")) {
            txtRequisitionNo.setText(REQUISITION_NO);
            txtPurpose.setText(PURPOSE);
            txtComments.setText("");
        } else if(DELIVERY_DATE.equals("")) {
            txtRequisitionNo.setText(REQUISITION_NO);
            txtPurpose.setText(PURPOSE);
            txtComments.setText(COMMENTS);
            txtDeliveryDate.setText("");
        } else if(TOTAL_AMOUNT.equals("")) {
            txtRequisitionNo.setText(REQUISITION_NO);
            txtPurpose.setText(PURPOSE);
            txtComments.setText(COMMENTS);
            txtDeliveryDate.setText(DELIVERY_DATE);
            txtTotalAmount.setText("");
        } else if(RADIO.equals("")){
            txtRequisitionNo.setText(REQUISITION_NO);
            txtPurpose.setText(PURPOSE);
            txtComments.setText(COMMENTS);
            txtDeliveryDate.setText(DELIVERY_DATE);
            txtTotalAmount.setText(TOTAL_AMOUNT);
        } else {
            txtRequisitionNo.setText(REQUISITION_NO);
            txtPurpose.setText(PURPOSE);
            txtComments.setText(COMMENTS);
            txtDeliveryDate.setText(DELIVERY_DATE);
            txtTotalAmount.setText(TOTAL_AMOUNT);

            if(RADIO.equals("Yes")) {
                radioGroup.check(R.id.radioYes);
            } else if(RADIO.equals("No")) {
                radioGroup.check(R.id.radioNo);
            }
        }
    }

    private void GoToQuotation() {
        btnQuotation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(Validation()) {

                            REQUISITION_NO = txtRequisitionNo.getText().toString();
                            PURPOSE = txtPurpose.getText().toString();
                            COMMENTS = txtComments.getText().toString();
                            DELIVERY_DATE = txtDeliveryDate.getText().toString();
                            TOTAL_AMOUNT = txtTotalAmount.getText().toString();
                            REQUISITION_STATUS = txtStatus.getText().toString();

                            RequisitionActivity.fm2.beginTransaction().replace(R.id.fragment_container_requisition, new QuotationFragment(), null).commit();
                        } else {
                            new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                                    .setTitle("ALERT")
                                    .setMessage("Fill the required fields!")
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Whatever...
                                        }
                                    }).show();
                        }
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
