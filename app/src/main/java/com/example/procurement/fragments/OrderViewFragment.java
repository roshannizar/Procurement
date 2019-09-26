package com.example.procurement.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.procurement.utils.CommonConstants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;

public class OrderViewFragment extends Fragment {
    private static final String TAG = "OrderViewFragment";

    private String orderKey;
    private Order order;
    private DocumentReference orderDBRef;
    private ImageView btnBack;
    private RecyclerView productItemView;
    private EditText etCompany, etVendor;
    private TextView txtOrderId, txtRequisitionId, txtOrderName, txtDeliveryDate,
            txtDescription, txtStatusView, txtSubTotal, txtTax, txtTotal, txtOrderedDate;
    private Button btnUpdate;
    private ArrayList<Inventory> iInventory;
    private InventoryAdapter inventoryAdapter;
    private Inventory i;


    public OrderViewFragment(String orderKey) {
        this.orderKey = orderKey;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_purchase_order_view, container, false);

        orderDBRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_ORDER)
                .document(orderKey);

        btnBack = rootView.findViewById(R.id.btnBack);
        txtOrderId = rootView.findViewById(R.id.txtOrderId);
        txtRequisitionId = rootView.findViewById(R.id.txtRequisitionId);
        txtOrderName = rootView.findViewById(R.id.txtOrderName);
        txtOrderedDate = rootView.findViewById(R.id.txtOrderedDate);
        txtDeliveryDate = rootView.findViewById(R.id.txtDeliveryDate);
        txtDescription = rootView.findViewById(R.id.txtDescription);
        txtStatusView = rootView.findViewById(R.id.txtStatusView);
        txtSubTotal = rootView.findViewById(R.id.txtSubTotal);
        txtTax = rootView.findViewById(R.id.txtTax);
        txtTotal = rootView.findViewById(R.id.txtTotal);
        etCompany = rootView.findViewById(R.id.etCompany);
        etVendor = rootView.findViewById(R.id.etVendor);
        btnUpdate = rootView.findViewById(R.id.btnUpdate);

        iInventory = new ArrayList<>();
        inventoryAdapter = new InventoryAdapter(getActivity(),iInventory);

        productItemView = rootView.findViewById(R.id.rvItemView);
        productItemView.setLayoutManager(new LinearLayoutManager(getActivity()));
        productItemView.setItemAnimator(new DefaultItemAnimator());

        DeleteOrder();
        readStatusData();
        WriteDataValues();
        getBack();

        return rootView;
    }

    private void WriteDataValues() {

        for(int j=1;j<=5;j++) {
            i = new Inventory(String.valueOf(j),"Sand Heap","",7,2);
            iInventory.add(i);
        }


        inventoryAdapter = new InventoryAdapter(getActivity(), iInventory);
        productItemView.setAdapter(inventoryAdapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_edit:
                HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new CreatePurchaseOrderFragment(), null).commit();
                break;
            case R.id.menu_delete:
                Toast.makeText(getActivity(), "Order Delete !!!", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getBack() {
        btnBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderStatusFragment(), null).commit();
                    }
                }
        );
    }

    private void readStatusData() {
        orderDBRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                order = new Order();
                order = documentSnapshot.toObject(Order.class);

                if (order != null) {

                    String orderStatus = order.getOrderStatus();

                    etVendor.setText(order.getVendor());
                    etCompany.setText(order.getCompany());
                    txtOrderId.setText(order.getOrderID());
                    txtRequisitionId.setText(order.getRequisitionID());
                    txtOrderName.setText(order.getOrderName());
                    txtOrderedDate.setText(order.getOrderedDate());
                    txtDeliveryDate.setText(order.getDeliveryDate());
                    txtDescription.setText(order.getDescription());
                    txtStatusView.setText(orderStatus);

                    switch (orderStatus) {
                        case CommonConstants.ORDER_STATUS_APPROVED:
                            txtStatusView.setBackgroundResource(R.drawable.badge_approved);
                            break;
                        case CommonConstants.ORDER_STATUS_PENDING:
                            txtStatusView.setBackgroundResource(R.drawable.badge_pending);
                            break;
                        case CommonConstants.ORDER_STATUS_PLACED:
                            txtStatusView.setBackgroundResource(R.drawable.badge_placed);
                            break;
                        case CommonConstants.ORDER_STATUS_HOLD:
                            txtStatusView.setBackgroundResource(R.drawable.badge_hold);
                            EditOrder();
                            break;
                        case CommonConstants.ORDER_STATUS_DRAFT:
                            txtStatusView.setBackgroundResource(R.drawable.badge_draft);
                            EditOrder();
                            break;
                        default:
                            txtStatusView.setBackgroundResource(R.drawable.badge_denied);
                    }

                    double subTotal = order.getSubTotal();
                    double tax = subTotal * 0.10;
                    double total = subTotal + tax;
                    txtSubTotal.setText(String.valueOf(subTotal));
                    txtTax.setText(String.valueOf(tax));
                    txtTotal.setText(String.valueOf(total));
                }
            }
        });
    }

    private void EditOrder() {
        btnUpdate.setText("Edit");
        btnUpdate.setBackgroundResource(R.drawable.badge_approved);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void DeleteOrder() {
        btnUpdate.setText("Delete");
        btnUpdate.setBackgroundResource(R.drawable.badge_denied);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
