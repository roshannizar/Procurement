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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.activities.HomeActivity;
import com.example.procurement.adapters.InventoryAdapter;
import com.example.procurement.adapters.OrderStatusAdapter;
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
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;
import static com.example.procurement.activities.SignInActivity.SignOutUserFirebase;
import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;
import static com.example.procurement.utils.CommonConstants.GENERATE_ORDER_FRAGMENT_TAG;
import static com.example.procurement.utils.CommonConstants.ORDER_ID;

public class CreateOrderFragment extends Fragment {

    private Spinner spCompany, spVendor;
    private TextView txtOrderId, txtRequisitionId, txtDeliveryDate,
            txtDescription, txtStatusView, txtSubTotal, txtTax, txtTotal, txtCurrentDate;
    private CardView cvTotal;
    private RecyclerView productItem;
    private Button btnGenerate;
    private ImageView btnBack;
    private String requisitionKey;
    private DocumentReference requisitionRef;
    private CollectionReference orderDBRef, supplierDBRef, sitesDBRef,inventoryRef;
    private Requisition requisition;
    private Inventory inventory;
    private DatePickerDialog picker;
    private InventoryAdapter adapter;
    private ArrayList<String> companyList, vendorList;
    private Context mContext;
    private final String selectCompany = "Select Company";
    private final String selectVendor = "Select Vendor";
    private ArrayList<Inventory> inventoryList;


    public CreateOrderFragment(String requisitionKey) {
        this.requisitionKey = requisitionKey;
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
        View rootView = inflater.inflate(R.layout.fragment_create_purchase_order, container, false);

        requisitionRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_REQUISITION).document(requisitionKey);
        orderDBRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_ORDER);
        supplierDBRef = requisitionRef.collection(CommonConstants.COLLECTION_REQUISITION_SUPPLIER);
        sitesDBRef = FirebaseFirestore.getInstance().collection(CommonConstants.COLLECTION_SITES);
        inventoryRef = requisitionRef.collection(CommonConstants.COLLECTION_REQUISITION_INVENTORY);

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


        cvTotal = rootView.findViewById(R.id.cvTotal);

        productItem = rootView.findViewById(R.id.rvItemView);
        btnGenerate = rootView.findViewById(R.id.btnGenerate);

        adapter = new InventoryAdapter(mContext,CommonConstants.iInventory,"Order");
        productItem.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
            order.setSubTotal(2500.00);
            order.setOrderKey(key);
            orderDBRef.document(key)
                    .set(order)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderStatusFragment(), null).commit();
                            Log.d(GENERATE_ORDER_FRAGMENT_TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(GENERATE_ORDER_FRAGMENT_TAG, "Error writing document", e);
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
                    Log.w(GENERATE_ORDER_FRAGMENT_TAG, "Listen failed.", e);
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
                    Log.w(GENERATE_ORDER_FRAGMENT_TAG, "Listen failed.", e);
                }

                vendorList.clear();
                vendorList.add(selectVendor);

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Supplier supplier = document.toObject(Supplier.class);
                        vendorList.add(supplier.getSupplierName());
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

        inventoryRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                }

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Inventory inventory = document.toObject(Inventory.class);
                        inventoryList.add(inventory);
                        System.out.println(inventory.getItemName());
                    }


                    if (inventoryList != null) {
                        adapter= new InventoryAdapter(mContext, inventoryList,"Order");
//                        progressBar.setVisibility(View.GONE);
//                        imgLoader.setVisibility(View.INVISIBLE);
//                        txtLoader.setVisibility(View.INVISIBLE);
//                        txtWait.setVisibility(View.INVISIBLE);
                        productItem.setAdapter(adapter);

//                        if (orders.size() == 0) {
//                            imgLoader.refreshDrawableState();
//                            imgLoader.setImageResource(R.drawable.ic_safebox);
//                            imgLoader.setVisibility(View.VISIBLE);
//                            txtLoader.setVisibility(View.VISIBLE);
//                            txtWait.setVisibility(View.VISIBLE);
//                            txtLoader.setText("Purchase Order is empty!");
//                            txtWait.setText("No point in waiting!");
//                        }
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
                    Log.w(GENERATE_ORDER_FRAGMENT_TAG, "Listen failed.", e);
                }

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Order order = document.toObject(Order.class);
                        ORDER_ID = order.getOrderID();
                    }

                    Pattern p = Pattern.compile("\\d+");
                    String generateNo = null;
                    if (ORDER_ID != null) {
                        Matcher m = p.matcher(ORDER_ID);

                        while (m.find()) {
                            generateNo = m.group();
                        }
                        int value = Integer.parseInt(generateNo) + 1;
                        String temp = "PO-" + value;
                        txtOrderId.setText(temp);
                        ORDER_ID = "";
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
                    order.setSubTotal(2500.00);
                    order.setOrderKey(key);
                    orderDBRef.document(key)
                            .set(order)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    alert.show();
                                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderViewFragment(key), null).commit();
                                    Log.d(GENERATE_ORDER_FRAGMENT_TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(GENERATE_ORDER_FRAGMENT_TAG, "Error writing document", e);
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

        boolean value = true;
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
        } else if (inventoryList.size() == 0) {
            txtDescription.setBackgroundResource(R.drawable.text_box);
            productItem.setBackgroundResource(R.drawable.text_box_empty);
            value = true;
        } else {
            productItem.setBackgroundResource(R.drawable.text_box);
            value = true;
        }

        return value;
    }

}
