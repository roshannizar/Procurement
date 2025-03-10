package com.example.procurement.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.procurement.models.Requisition;
import com.example.procurement.models.Site;
import com.example.procurement.models.Supplier;
import com.example.procurement.utils.CommonConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;
import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;
import static com.example.procurement.utils.CommonConstants.ORDER_ID;

public class CreateOrderFragment extends Fragment {

    private Spinner spCompany, spVendor;
    private RecyclerView productItem;
    private Button btnGenerate;
    private ImageView btnBack;
    private String requisitionKey;
    private DocumentReference requisitionRef;
    private CollectionReference orderDBRef, supplierDBRef, sitesDBRef, inventoryDBRef, productDBRef, supplierProductDBRef;
    private Requisition requisition;
    private DatePickerDialog picker;
    private InventoryAdapter adapter;
    private ArrayList<String> companyList, vendorList;
    private Context mContext;
    private final String selectCompany = "Select Company";
    private final String selectVendor = "Select Vendor";
    private ArrayList<Inventory> inventoryList;
    private ArrayList<Supplier> suppliersList;
    private TextView txtOrderId, txtRequisitionId, txtDeliveryDate,
            txtDescription, txtStatusView, txtSubTotal, txtTax, txtTotal, txtCurrentDate;

    public CreateOrderFragment(String requisitionKey) {
        this.requisitionKey = requisitionKey;
        companyList = new ArrayList<>();
        vendorList = new ArrayList<>();
        inventoryList = new ArrayList<>();
        suppliersList = new ArrayList<>();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_purchase_order, container, false);

        requisitionRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_REQUISITION).document(requisitionKey);
        orderDBRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_ORDER);
        supplierDBRef = requisitionRef.collection(CommonConstants.COLLECTION_SUPPLIERS);
        sitesDBRef = FirebaseFirestore.getInstance().collection(CommonConstants.COLLECTION_SITES);
        inventoryDBRef = requisitionRef.collection(CommonConstants.COLLECTION_INVENTORIES);

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
        btnGenerate = rootView.findViewById(R.id.btnGenerate);

        adapter = new InventoryAdapter(inventoryList, "Order");
        productItem.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        productItem.setItemAnimator(new DefaultItemAnimator());


        getBack();
        getGenerateID();
        ShowDialog();
        generateOrder();
        readData();
        setDate();
        setDescriptionDialog();
        setSpinnerData();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.create_order_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_draft) {
            String key = orderDBRef.document().getId();
            Order order = new Order();
            order.setOrderID(txtOrderId.getText().toString());
            order.setRequisitionID(txtRequisitionId.getText().toString());
            order.setCompany(spCompany.getSelectedItem().toString());
            order.setVendor(spVendor.getSelectedItem().toString());
            order.setDeliveryDate(txtDeliveryDate.getText().toString());
            order.setOrderedDate(txtCurrentDate.getText().toString());
            order.setDescription(txtDescription.getText().toString());
            order.setOrderStatus(getString(R.string.draft));
            order.setSubTotal(Double.parseDouble(txtSubTotal.getText().toString()));
            order.setOrderKey(key);

            productDBRef = orderDBRef.document(key).collection(CommonConstants.COLLECTION_INVENTORIES);
            for (Inventory inventory : inventoryList) {
                productDBRef.document().set(inventory);
            }

            supplierProductDBRef = orderDBRef.document(key).collection(CommonConstants.COLLECTION_SUPPLIERS);
            for (Supplier supplier : suppliersList) {
                supplierProductDBRef.document().set(supplier);
            }

            orderDBRef.document(key)
                    .set(order)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderStatusFragment(), null).commit();
                            Log.d(TAG, CommonConstants.SUCCESS_MSG);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, CommonConstants.ERROR_MSG, e);
                        }
                    });

        }

        return super.onOptionsItemSelected(item);
    }

    private void setSpinnerData() {


        sitesDBRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, CommonConstants.ERROR_MSG, e);
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
                    Log.w(TAG, CommonConstants.ERROR_MSG, e);
                }

                vendorList.clear();
                vendorList.add(selectVendor);

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Supplier supplier = document.toObject(Supplier.class);
                        if (supplier.getStatus().equals("Active")) {
                            suppliersList.add(supplier);
                            vendorList.add(supplier.getSupplierName());
                        }
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, vendorList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spVendor.setAdapter(adapter);
            }
        });

    }

    private void getBack() {
        btnBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new RequisitionViewFragment(), null).commit();
                    }
                }
        );
    }

    private void readData() {
        requisitionRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                requisition = new Requisition();
                requisition = documentSnapshot.toObject(Requisition.class);

                if (requisition != null) {

                    txtRequisitionId.setText(requisition.getRequisitionNo());
                    txtDeliveryDate.setText(requisition.getDeliveryDate());

                    DecimalFormat df = new DecimalFormat("#.##");
                    double subTotal = Double.parseDouble(requisition.getTotalAmount());
                    double tax = subTotal * 0.10d;
                    double total = subTotal + tax;
                    txtSubTotal.setText(String.valueOf(subTotal));
                    txtTax.setText((df.format(tax)));
                    txtTotal.setText(String.valueOf(total));
                }
            }
        });

        inventoryDBRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, CommonConstants.ERROR_MSG, e);
                }

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Inventory inventory = document.toObject(Inventory.class);
                        inventoryList.add(inventory);
                    }


                    if (inventoryList != null) {
                        adapter = new InventoryAdapter(inventoryList, "Order");
                        productItem.setAdapter(adapter);
                    }

                }
            }
        });
    }

    private void getGenerateID() {

        orderDBRef.orderBy("orderID").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, CommonConstants.ERROR_MSG, e);
                }

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Order order = document.toObject(Order.class);
                        ORDER_ID = order.getOrderID();
                    }

                    Pattern p = Pattern.compile("\\d+");
                    String generateNo = null;
                    try {
                        if (ORDER_ID != null) {
                            Matcher m = p.matcher(ORDER_ID);

                            while (m.find()) {
                                generateNo = m.group();
                            }
                            if (generateNo != null) {
                                int value = Integer.parseInt(generateNo) + 1;
                                String temp = "PO-" + value;
                                txtOrderId.setText(temp);
                                ORDER_ID = "";
                            } else {
                                ORDER_ID = "PO-1";
                                txtOrderId.setText(ORDER_ID);
                            }
                        }
                    } catch (NumberFormatException e1) {
                        Log.w(TAG, CommonConstants.ERROR_MSG, e1);
                    }
                }
            }
        });
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
        txtCurrentDate.setText(date + "/" + month + "/" + year);
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

    private void generateOrder() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Are you sure you want to exit?")
                .setTitle("ALERT")
                .setMessage("Successfully Generated the Purchase Order !")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        final AlertDialog alert = builder.create();

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation()) {

                    final String key = orderDBRef.document().getId();
                    final Order order = new Order();
                    order.setOrderID(txtOrderId.getText().toString());
                    order.setRequisitionID(txtRequisitionId.getText().toString());
                    order.setCompany(spCompany.getSelectedItem().toString());
                    order.setVendor(spVendor.getSelectedItem().toString());
                    order.setDeliveryDate(txtDeliveryDate.getText().toString());
                    order.setOrderedDate(txtCurrentDate.getText().toString());
                    order.setDescription(txtDescription.getText().toString());
                    order.setOrderStatus(txtStatusView.getText().toString());
                    order.setSubTotal(Double.parseDouble(txtSubTotal.getText().toString()));
                    order.setOrderKey(key);

                    productDBRef = orderDBRef.document(key).collection(CommonConstants.COLLECTION_INVENTORIES);
                    for (Inventory inventory : inventoryList) {
                        productDBRef.document().set(inventory);
                    }

                    supplierProductDBRef = orderDBRef.document(key).collection(CommonConstants.COLLECTION_SUPPLIERS);
                    for (Supplier supplier : suppliersList) {
                        supplierProductDBRef.document().set(supplier);
                    }

                    orderDBRef.document(key)
                            .set(order)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    alert.show();
                                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderViewFragment(key), null).commit();
                                    Log.d(TAG, CommonConstants.SUCCESS_MSG);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, CommonConstants.ERROR_MSG, e);
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
