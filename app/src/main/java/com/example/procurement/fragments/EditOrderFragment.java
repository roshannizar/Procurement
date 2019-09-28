package com.example.procurement.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.activities.HomeActivity;
import com.example.procurement.adapters.InventoryAdapter;
import com.example.procurement.models.Inventory;
import com.example.procurement.models.Order;
import com.example.procurement.models.Site;
import com.example.procurement.models.Supplier;
import com.example.procurement.utils.CommonConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;

public class EditOrderFragment extends Fragment {

    private Spinner spCompany, spVendor;
    private TextView txtOrderId, txtRequisitionId, txtDeliveryDate,
            txtDescription, txtStatusView, txtSubTotal, txtTax, txtTotal, txtCurrentDate;
    private RecyclerView productItem;
    private Button btnGenerate;
    private ImageView btnBack;
    private Order order;
    private CollectionReference orderDBRef, supplierDBRef, sitesDBRef, inventoryDBRef;
    private DatePickerDialog picker;
    private ArrayList<String> companyList, vendorList;
    private Context mContext;
    private final String selectCompany = "Select Company";
    private final String selectVendor = "Select Vendor";
    private ArrayList<Inventory> inventoryList;
    private InventoryAdapter adapter;


    public EditOrderFragment(Order order) {
        this.order = order;
        companyList = new ArrayList<>();
        vendorList = new ArrayList<>();
        inventoryList = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_purchase_order, container, false);

        orderDBRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_ORDER);
        supplierDBRef = orderDBRef.document(order.getOrderKey()).collection(CommonConstants.COLLECTION_SUPPLIERS);
        sitesDBRef = FirebaseFirestore.getInstance().collection(CommonConstants.COLLECTION_SITES);
        inventoryDBRef = orderDBRef.document(order.getOrderKey()).collection(CommonConstants.COLLECTION_INVENTORIES);

        mContext = getContext();

        btnBack = rootView.findViewById(R.id.btnBack);
        spCompany = rootView.findViewById(R.id.spCompany);
        spVendor = rootView.findViewById(R.id.spVendor);

        txtOrderId = rootView.findViewById(R.id.txtOrderId);
        txtRequisitionId = rootView.findViewById(R.id.txtRequisitionId);
        txtCurrentDate = rootView.findViewById(R.id.txtOrderedDate);
        txtDeliveryDate = rootView.findViewById(R.id.txtDeliveryDate);
        txtDescription = rootView.findViewById(R.id.txtDescription);
        txtStatusView = rootView.findViewById(R.id.txtStatusView);
        txtSubTotal = rootView.findViewById(R.id.txtSubTotal);
        txtTax = rootView.findViewById(R.id.txtTax);
        txtTotal = rootView.findViewById(R.id.txtTotal);
        productItem = rootView.findViewById(R.id.rvItemView);

        adapter = new InventoryAdapter(inventoryList, "Order");
        productItem.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        productItem.setItemAnimator(new DefaultItemAnimator());

        btnGenerate = rootView.findViewById(R.id.btnGenerate);
        getBack();
        updateOrder();
        ShowDialog();
        readData();
        setDate();
        setDescriptionDialog();
        setSpinnerData();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void setSpinnerData() {

        sitesDBRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                }

                companyList.clear();
                companyList.add(selectCompany);

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Site site = document.toObject(Site.class);
                        companyList.add(site.getSiteName());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, companyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCompany.setAdapter(adapter);
            }
        });

        supplierDBRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e);
                }

                vendorList.clear();
                vendorList.add(selectVendor);

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Supplier supplier = document.toObject(Supplier.class);

                        if (supplier.getStatus().equals("Active")) {
                            vendorList.add(supplier.getSupplierName());
                        }
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, vendorList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spVendor.setAdapter(adapter);
            }
        });

        inventoryDBRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e);
                }

                inventoryList.clear();

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Inventory inventory = document.toObject(Inventory.class);
                        inventoryList.add(inventory);
                    }
                }

                if (inventoryList != null) {
                    adapter = new InventoryAdapter(inventoryList, "Order");
                    productItem.setAdapter(adapter);
                }
            }
        });
    }

    private void getBack() {
        btnBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderViewFragment(order.getOrderKey()), null).commit();
                    }
                }
        );
    }

    private void readData() {
        txtOrderId.setText(order.getOrderID());
        txtRequisitionId.setText(order.getRequisitionID());
        txtDeliveryDate.setText(order.getDeliveryDate());
        txtDescription.setText(order.getDescription());
        txtStatusView.setText(order.getOrderStatus());
        txtRequisitionId.setText(order.getRequisitionID());
        txtDeliveryDate.setText(order.getDeliveryDate());

        DecimalFormat df = new DecimalFormat("#.##");
        double subTotal = order.getSubTotal();
        double tax = subTotal * 0.10d;
        double total = subTotal + tax;
        txtSubTotal.setText(String.valueOf(subTotal));
        txtTax.setText((df.format(tax)));
        txtTotal.setText(String.valueOf(total));

        switch (order.getOrderStatus()) {
            case CommonConstants.ORDER_STATUS_PENDING:
                txtStatusView.setBackgroundResource(R.drawable.badge_pending);
                break;
            case CommonConstants.ORDER_STATUS_HOLD:
                txtStatusView.setBackgroundResource(R.drawable.badge_hold);
                break;
            case CommonConstants.ORDER_STATUS_DRAFT:
                txtStatusView.setBackgroundResource(R.drawable.badge_draft);
                break;
        }
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
        txtCurrentDate.setText(date + "-" + month + "-" + year);
    }

    private void setDescriptionDialog() {

        txtDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mContext);
                View dialogView = layoutInflaterAndroid.inflate(R.layout.layout_add_description, null);

                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(mContext);
                alertDialogBuilderUserInput.setView(dialogView);

                final EditText inputText = dialogView.findViewById(R.id.txtDescription);
                inputText.setHint(R.string.hint_enter_description);

                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton(CommonConstants.SAVE_STRING, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                            }
                        })
                        .setNegativeButton(CommonConstants.CANCEL_STRING,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
                alertDialog.show();

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtDescription.setText(inputText.getText().toString());
                        alertDialog.dismiss();
                    }
                });
            }
        });


    }

    private void updateOrder() {
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation()) {

                    order.setCompany(spCompany.getSelectedItem().toString());
                    order.setVendor(spVendor.getSelectedItem().toString());
                    order.setDeliveryDate(txtDeliveryDate.getText().toString());
                    order.setDescription(txtDescription.getText().toString());
                    order.setOrderStatus(getString(R.string.pending));
                    order.setSubTotal(Double.parseDouble(txtSubTotal.getText().toString()));

                    orderDBRef.document(order.getOrderKey())
                            .set(order)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                                            .setTitle("ALERT")
                                            .setMessage("Successfully Generated the Purchase Order !")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            }).show();
                                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderViewFragment(order.getOrderKey()), null).commit();
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
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
        });
    }

    private boolean Validation() {

        boolean value;
        if (spVendor.getSelectedItem().toString().equals(selectVendor)) {
            spVendor.setBackgroundResource(R.drawable.text_box_empty);
            value = false;
        } else if (spCompany.getSelectedItem().toString().equals(selectCompany)) {
            spVendor.setBackgroundResource(R.drawable.text_box);
            spCompany.setBackgroundResource(R.drawable.text_box_empty);
            value = false;
        } else if (txtDescription.getText().toString().equals("")) {
            spCompany.setBackgroundResource(R.drawable.text_box);
            txtDescription.setBackgroundResource(R.drawable.text_box_empty);
            value = false;
        } else {
            txtDescription.setBackgroundResource(R.drawable.text_box);
            value = true;
        }

        return value;
    }
}
