package com.example.procurement;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateOrderFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText txtCurrentDate,txtOrderName,txtDescription,dtpArrivalDate,txtOrderID;
    private Spinner spinnerStock;
    private Switch stockSwitch;
    private ArrayList<Order> orders;
    private Button btnPlaceOrder;
    private TextView txtStatus;
    private boolean switchPlacement;
    private DatabaseReference myRef;
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

        txtOrderName.setVisibility(View.INVISIBLE);
        spinnerStock.setOnItemSelectedListener(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(CommonConstants.FIREBASE_DATABASE_NAME);
        orders = new ArrayList<>();

        init();

        return v;
    }

    private void init() {
        getHashOrderName();
        placeOrder();
        setDate();
    }

    private void placeOrder() {
        btnPlaceOrder.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (validationOrders()) {
                            if (switchPlacement) {
                                Order o = new Order("PO" + txtOrderID.getText().toString(), txtOrderName.getText().toString(), txtDescription.getText().toString(), CommonConstants.ORDER_STATUS_PENDING, dtpArrivalDate.getText().toString());
                                myRef.push().setValue(o);
                                Toast.makeText(getActivity(), "Order has been placed successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Order o = new Order("PO" + txtOrderID.getText().toString(), txtSpinnerStock, txtDescription.getText().toString(), CommonConstants.ORDER_STATUS_PENDING, dtpArrivalDate.getText().toString());
                                myRef.push().setValue(o);
                                Toast.makeText(getActivity(), "Order has been placed successfully", Toast.LENGTH_LONG).show();
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
                        if(stockSwitch.isChecked()) {
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
        int year,date,month;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            year = Year.now().getValue();
        } else {
            year = Calendar.getInstance().get(Calendar.YEAR);
        }

        date = Calendar.getInstance().get(Calendar.DATE);
        month = Calendar.getInstance().get(Calendar.MONTH);
        txtCurrentDate.setText(month+"-"+date+"-"+year);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0: txtSpinnerStock = "-- Please select a order";
                    break;
            case 1: txtSpinnerStock = "Bricks";
                    break;
            case 2: txtSpinnerStock = "Stones";
                    break;
            case 3: txtSpinnerStock = "Cement";
                    break;
            case 4: txtSpinnerStock = "Sand";
                    break;
            case 5: txtSpinnerStock = "Paint";
                    break;
            case 6: txtSpinnerStock = "Putty Wall";
                    break;
            case 7: txtSpinnerStock = "Wires";
                    break;
            case 8: txtSpinnerStock = "Cement Mixer";
                    break;
                    default: txtSpinnerStock = null;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        txtSpinnerStock = "-- Please select a order --";
    }

    private boolean validationOrders() {
        return !txtOrderID.getText().toString().equals("") && !txtOrderName.getText().toString().equals("") && txtSpinnerStock != null && !txtSpinnerStock.equals("") && !txtDescription.getText().toString().equals("") && !dtpArrivalDate.getText().toString().equals("");
    }

    private void generateID() {

    }
}
