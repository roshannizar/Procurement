package com.example.procurement.fragments;


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

import com.example.procurement.R;
import com.example.procurement.activities.HomeActivity;
import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;

public class OrderViewFragment extends Fragment {
    private static final String TAG = "OrderViewFragment";

    private String orderKey;
    private Order order;
    private DocumentReference orderDBRef;
    private ImageView btnBack;
    private EditText etCompany, etVendor;
    private TextView txtOrderId, txtRequisitionId, txtOrderName, txtDeliveryDate,
            txtDescription, txtStatusView, txtSubTotal, txtTax, txtTotal, txtOrderedDate;
    private Button btnUpdate;

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
        btnUpdate.setText("Delete");
        btnUpdate.setBackgroundResource(R.drawable.badge_denied);

        readStatusData();
        getBack();

        return rootView;
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

                    String orderID = getString(R.string.orderID) + order.getOrderID();
                    String requisitionID = getString(R.string.requisitionID) + order.getRequisitionID();
                    String vendor = order.getVendor();
                    String company = order.getCompany();
                    String orderName = getString(R.string.orderName) + order.getOrderName();
                    String orderStatus = order.getOrderStatus();
                    String deliveryDate = getString(R.string.deliveryDate) + order.getDeliveryDate();
                    String orderedDate = getString(R.string.orderedDate) + order.getOrderedDate();
                    String description = getString(R.string.description) + order.getDescription();

                    etVendor.setText(vendor);
                    etCompany.setText(company);
                    txtOrderId.setText(orderID);
                    txtRequisitionId.setText(requisitionID);
                    txtOrderName.setText(orderName);
                    txtOrderedDate.setText(orderedDate);
                    txtDeliveryDate.setText(deliveryDate);
                    txtDescription.setText(description);
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
                            break;
                        case CommonConstants.ORDER_STATUS_DRAFT:
                            txtStatusView.setBackgroundResource(R.drawable.badge_draft);
                            btnUpdate.setText("Edit");
                            btnUpdate.setBackgroundResource(R.drawable.badge_approved);
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
}
