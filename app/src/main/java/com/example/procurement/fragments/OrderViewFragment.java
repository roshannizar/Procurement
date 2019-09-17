package com.example.procurement.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.procurement.PMS;
import com.example.procurement.R;
import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.example.procurement.utils.CommonConstants.ORDER_VIEW_FRAGMENT_TAG;


public class OrderViewFragment extends Fragment {

    private String orderKey;
    private Order order;
    private DatabaseReference orderDatabaseRef;
    private TextView textView;

    public OrderViewFragment(String orderKey) {
        this.orderKey = orderKey;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_view, container, false);

        orderDatabaseRef = PMS.DatabaseRef.child(CommonConstants.FIREBASE_ORDER_DB).child(orderKey).getRef();

        textView = view.findViewById(R.id.orderView);
        readStatusData();
        return view;
    }

    private void readStatusData() {

        orderDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                order = new Order();
                order = dataSnapshot.getValue(Order.class);
                textView.setText(order.getOrderID() + "\n " + order.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(ORDER_VIEW_FRAGMENT_TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
