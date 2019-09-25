package com.example.procurement.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.procurement.R;
import com.example.procurement.activities.HomeActivity;
import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;

public class OrderViewFragment extends Fragment {
    private static final String TAG = "OrderViewFragment";

    private String orderKey;
    private Order order;
    private DocumentReference orderDBRef;
    private ImageView btnBack;
    private TextView txtOrderNameTitle, txtOrderIdView, txtOrderNameView, dtpDateView, txtDescriptionView, txtStatusView;

    public OrderViewFragment(String orderKey) {
        this.orderKey = orderKey;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_view, container, false);

        orderDBRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_ORDER)
                .document(orderKey);
        btnBack = view.findViewById(R.id.btnBack);
        txtOrderNameTitle = view.findViewById(R.id.txtOrderNameTitle);
        txtOrderIdView = view.findViewById(R.id.txtOrderIdView);
        txtOrderNameView = view.findViewById(R.id.txtOrderNameView);
        dtpDateView = view.findViewById(R.id.dtpDateView);
        txtDescriptionView = view.findViewById(R.id.txtDescription);
        txtStatusView = view.findViewById(R.id.txtStatusView);
        readStatusData();
        getBack();

        return view;
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
                txtOrderNameTitle.setText(order.getOrderID() + " - " + order.getName());
                txtOrderIdView.setText(order.getOrderID());
                txtOrderNameView.setText(order.getName());
                txtDescriptionView.setText(order.getDescription());
                dtpDateView.setText(order.getDate());
                txtStatusView.setText(order.getStatus());            }
        });
    }
}
