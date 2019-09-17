package com.example.procurement.fragments;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.procurement.PMS;
import com.example.procurement.R;
import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.database.DatabaseReference;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateOrderFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText txtCurrentDate, txtOrderName, txtDescription, dtpArrivalDate, txtOrderID;
    private Spinner spinnerStock;
    private Switch stockSwitch;
    private ArrayList<Order> orders;
    private Button btnPlaceOrder, btnGenerate;
    private TextView txtStatus;
    private boolean switchPlacement;
    private DatabaseReference orderCreateRef;
    private String txtSpinnerStock;

    public CreateOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_order, container, false);
        txtCurrentDate = v.findViewById(R.id.txtCurrentDate);
        spinnerStock = v.findViewById(R.id.spinnerstock);
        txtOrderName = v.findViewById(R.id.txtOrderName);
        stockSwitch = v.findViewById(R.id.stockSwitch);
        btnPlaceOrder = v.findViewById(R.id.btnPlaceOrder);
        txtDescription = v.findViewById(R.id.txtDescription);
        dtpArrivalDate = v.findViewById(R.id.dtpArrivalDate);
        txtStatus = v.findViewById(R.id.txtStatus);
        txtOrderID = v.findViewById(R.id.txtOrderID);
        btnGenerate = v.findViewById(R.id.btnGenerate);

        generate();
        txtOrderName.setVisibility(View.INVISIBLE);
        spinnerStock.setOnItemSelectedListener(this);

        orderCreateRef = PMS.DatabaseRef.child(CommonConstants.FIREBASE_ORDER_DB).getRef();
        orders = new ArrayList<>();

        init();

        return v;
    }

    private void init() {
        getHashOrderName();
        getGenerateID();
        placeOrder();
        setDate();
    }

    private void generate() {
        btnGenerate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setGenerateID(txtOrderID.getText().toString());
                    }
                }
        );
    }

    private void placeOrder() {
        btnPlaceOrder.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Code refactoring will be done later
                        if (!validationOrders()) {
                            if (switchPlacement) {
                                DatabaseReference reference = orderCreateRef.push();
                                String key = reference.getKey();
                                Order o = new Order(txtOrderID.getText().toString(), txtOrderName.getText().toString(), txtDescription.getText().toString(), CommonConstants.ORDER_STATUS_PENDING, dtpArrivalDate.getText().toString());
                                o.setKey(key);
                                orderCreateRef.child(key).setValue(o);
                                Toast.makeText(getActivity(), "Order has been placed successfully", Toast.LENGTH_LONG).show();
                                setGenerateID(txtOrderID.getText().toString());
                            } else {
                                DatabaseReference reference = orderCreateRef.push();
                                String key = reference.getKey();
                                Order o = new Order(txtOrderID.getText().toString(), txtSpinnerStock, txtDescription.getText().toString(), CommonConstants.ORDER_STATUS_PENDING, dtpArrivalDate.getText().toString());
                                o.setKey(key);
                                orderCreateRef.child(key).setValue(o);
                                Toast.makeText(getActivity(), "Order has been placed successfully", Toast.LENGTH_LONG).show();
                                setGenerateID(txtOrderID.getText().toString());
                            }
                        } else {
                            new AlertDialog.Builder(getActivity())
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

    private void getHashOrderName() {
        stockSwitch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (stockSwitch.isChecked()) {
                            txtOrderName.setVisibility(View.VISIBLE);
                            spinnerStock.setVisibility(View.INVISIBLE);
                            switchPlacement = true;
                        } else {
                            txtOrderName.setVisibility(View.INVISIBLE);
                            spinnerStock.setVisibility(View.VISIBLE);
                            switchPlacement = false;
                        }
                    }
                }
        );
    }

    @SuppressLint("SetTextI18n")
    private void setDate() {
        int year, date, month;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            year = Year.now().getValue();
        } else {
            year = Calendar.getInstance().get(Calendar.YEAR);
        }

        date = Calendar.getInstance().get(Calendar.DATE);
        month = Calendar.getInstance().get(Calendar.MONTH);
        txtCurrentDate.setText(month + "-" + date + "-" + year);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                txtSpinnerStock = "-- Please select a order";
                break;
            case 1:
                txtSpinnerStock = "Bricks";
                break;
            case 2:
                txtSpinnerStock = "Stones";
                break;
            case 3:
                txtSpinnerStock = "Cement";
                break;
            case 4:
                txtSpinnerStock = "Sand";
                break;
            case 5:
                txtSpinnerStock = "Paint";
                break;
            case 6:
                txtSpinnerStock = "Putty Wall";
                break;
            case 7:
                txtSpinnerStock = "Wires";
                break;
            case 8:
                txtSpinnerStock = "Cement Mixer";
                break;
            default:
                txtSpinnerStock = null;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        txtSpinnerStock = "-- Please select a order --";
    }

    private boolean validationOrders() {
        return !txtOrderID.getText().toString().equals("") && !txtOrderName.getText().toString().equals("") && txtSpinnerStock != null && !txtSpinnerStock.equals("") && !txtDescription.getText().toString().equals("") && !dtpArrivalDate.getText().toString().equals("");
    }

    private void getGenerateID() {
        Pattern p = Pattern.compile("\\d+");
        String generateNo = null;
        Matcher m = p.matcher(CommonConstants.ORDER_ID);

        while (m.find()) {
            generateNo = m.group();
        }

        int value = Integer.parseInt(generateNo) + 1;

        txtOrderID.setText("PO - " + value);
    }

    //Validation needed
    private void setGenerateID(String value) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(value);
        while (m.find()) {
            value = m.group();
        }

        int finalValue = Integer.parseInt(value) + 1;
        String temp = "PO - " + finalValue;

        txtOrderID.setText(temp);
        CommonConstants.ORDER_ID = temp;
    }
}
