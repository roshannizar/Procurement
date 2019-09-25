package com.example.procurement.fragments;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.procurement.R;
import com.example.procurement.activities.HomeActivity;
import com.example.procurement.models.Note;
import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;

import java.text.DateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.procurement.PMS.siteManagerDBRef;
import static com.example.procurement.utils.CommonConstants.ORDER_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateOrderFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText txtCurrentDate, txtOrderName, txtDescription, dtpArrivalDate, txtOrderID;
    private Switch stockSwitch;
    private ArrayList<Order> orders;
    private Button btnPlaceOrder, btnGenerate;
    private TextView txtStatus;
    private boolean switchPlacement;
    private CollectionReference orderDBRef;
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
        txtOrderName = v.findViewById(R.id.txtOrderName);
        stockSwitch = v.findViewById(R.id.stockSwitch);
        btnPlaceOrder = v.findViewById(R.id.btnPlaceOrder);
        txtDescription = v.findViewById(R.id.txtDescription);
        dtpArrivalDate = v.findViewById(R.id.dtpArrivalDate);
        txtStatus = v.findViewById(R.id.txtStatus);
        txtOrderID = v.findViewById(R.id.txtOrderID);
        btnGenerate = v.findViewById(R.id.btnGenerate);

        orderDBRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_ORDER);
        orders = new ArrayList<>();

        generate();
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

                                String key = orderDBRef.document().getId();
                                Order order = new Order(txtOrderID.getText().toString(), txtOrderName.getText().toString(), txtDescription.getText().toString(), CommonConstants.ORDER_STATUS_PENDING, dtpArrivalDate.getText().toString());
                                order.setKey(key);
                                orderDBRef.document(key)
                                        .set(order)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(), "Order has been placed successfully", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), "Ordering Failed", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                setGenerateID(txtOrderID.getText().toString());
                            } else {

                                String key = orderDBRef.document().getId();
                                Order order = new Order(txtOrderID.getText().toString(), txtSpinnerStock, txtDescription.getText().toString(), CommonConstants.ORDER_STATUS_PENDING, dtpArrivalDate.getText().toString());
                                order.setKey(key);
                                orderDBRef.document(key)
                                        .set(order)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(), "Order has been placed successfully", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), "Ordering Failed", Toast.LENGTH_LONG).show();
                                            }
                                        });

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

                            HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new InventoryDialog(), null).commit();
                        } else {
                            txtOrderName.setVisibility(View.VISIBLE);
                            switchPlacement = true;
                            //showBubbleDialog(false,null,-1);
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
        if (ORDER_ID != null) {
            Matcher m = p.matcher(ORDER_ID);

            while (m.find()) {
                generateNo = m.group();
            }
            int value = Integer.parseInt(generateNo) + 1;
            txtOrderID.setText("PO - " + value);
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
        String temp = "PO - " + finalValue;

        txtOrderID.setText(temp);
        ORDER_ID = temp;
    }

    private void showBubbleDialog(final boolean shouldUpdate, final Note note, final int position) {
        InventoryDialog idialog = new InventoryDialog();
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View view = layoutInflaterAndroid.inflate(R.layout.layout_inventory_dialog, null);

        android.app.AlertDialog.Builder alertDialogBuilderUserInput = new android.app.AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(view);


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? CommonConstants.UPDATE_STRING
                        : CommonConstants.SAVE_STRING, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton(CommonConstants.CANCEL_STRING,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final android.app.AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
