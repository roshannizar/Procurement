package com.example.procurement.status;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.CreateOrderFragment;
import com.example.procurement.HomeActivity;
import com.example.procurement.R;
import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class OrderStatusFragment extends Fragment {

    private static final String TAG = "OrderStatusViewActivity";

    private ArrayList<Order> orders;
    private OrderStatusAdapter adapter;
    private OrderStatus approvedOrderStatus, declinedOrderStatus, placedOrderStatus, pendingOrderStatus, holdOrderStatus;
    private RecyclerView recyclerView;
    private int checkedItem = 0;
    private DatabaseReference myRef;
    private ProgressBar progressBar;
    private Context mContext;
    public static int pendingStatus = 0, approvedStatus = 0, holdStatus = 0, placedStatus = 0, declinedStatus = 0;

    public OrderStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        pendingStatus = 0;
        approvedStatus = 0;
        holdStatus = 0;
        placedStatus = 0;
        declinedStatus = 0;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_order_status, container, false);
        mContext = rootView.getContext();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Orders");

        orders = new ArrayList<>();

        approvedOrderStatus = new ApprovedOrderStatus();
        declinedOrderStatus = new DeclinedOrderStatus();
        pendingOrderStatus = new PendingOrderStatus();
        placedOrderStatus = new PlacedOrderStatus();
        holdOrderStatus = new HoldOrderStatus();

        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.rvOrderMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        writeStatusData();
        readStatusData();

        return rootView;
    }

    private int getCheckedItem() {
        return checkedItem;
    }

    private void setCheckedItem(int checkedItem) {
        this.checkedItem = checkedItem;
    }

    private void writeStatusData() {
        Order order1 = new Order("1", "Praveen", "", CommonConstants.ORDER_STATUS_PENDING, "1-06-2019");
        Order order2 = new Order("2", "Kumar", "", CommonConstants.ORDER_STATUS_APPROVED, "2-06-2019");
        Order order3 = new Order("3", "Haresh", "", CommonConstants.ORDER_STATUS_PLACED, "3-06-2019");
        Order order4 = new Order("4", "Dhanush", "", CommonConstants.ORDER_STATUS_DECLINED, "4-06-2019");
        Order order5 = new Order("5", "Abishaan", "", CommonConstants.ORDER_STATUS_PENDING, "1-06-2019");
        Order order6 = new Order("6", "Roshan", "", CommonConstants.ORDER_STATUS_APPROVED, "2-06-2019");
        Order order7 = new Order("7", "Prashan", "", CommonConstants.ORDER_STATUS_PLACED, "3-06-2019");
        Order order8 = new Order("8", "Keerthigan", "", CommonConstants.ORDER_STATUS_HOLD, "4-06-2019");

        myRef.push().setValue(order1);
        myRef.push().setValue(order2);
        myRef.push().setValue(order3);
        myRef.push().setValue(order4);
        myRef.push().setValue(order5);
        myRef.push().setValue(order6);
        myRef.push().setValue(order7);
        myRef.push().setValue(order8);
    }

    private void readStatusData() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                orders.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Order order = data.getValue(Order.class);

                    if (order != null) {
                        countStatus(order.getStatusText());
                        orders.add(order);
                    }
                }

                adapter = new OrderStatusAdapter(mContext, orders);
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        inflater.inflate(R.menu.order_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("")) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.filter_status) {
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Choose Status Filter Type");

            // add a radio button list
            String[] status = {
                    getString(R.string.default_status),
                    getString(R.string.approved),
                    getString(R.string.declined),
                    getString(R.string.hold),
                    getString(R.string.pending),
                    getString(R.string.placed),

            };

            builder.setSingleChoiceItems(status, getCheckedItem(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            adapter = new OrderStatusAdapter(mContext, orders);
                            break;
                        case 1:
                            adapter = new OrderStatusAdapter(mContext, approvedOrderStatus.meetOrderStatus(orders));
                            break;
                        case 2:
                            adapter = new OrderStatusAdapter(mContext, declinedOrderStatus.meetOrderStatus(orders));
                            break;
                        case 3:
                            adapter = new OrderStatusAdapter(mContext, holdOrderStatus.meetOrderStatus(orders));
                            break;
                        case 4:
                            adapter = new OrderStatusAdapter(mContext, pendingOrderStatus.meetOrderStatus(orders));
                            break;
                        case 5:
                            adapter = new OrderStatusAdapter(mContext, placedOrderStatus.meetOrderStatus(orders));
                            break;
                    }
                    setCheckedItem(which);
                    recyclerView.setAdapter(adapter);
                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (item.getItemId() == R.id.action_create) {
            HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new CreateOrderFragment(), null).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    private void countStatus(String status) {

        if (status.equals("Pending")) {
            pendingStatus++;
        } else if (status.equals("Hold")) {
            holdStatus++;
        } else if (status.equals("Approved")) {
            approvedStatus++;
        } else if (status.equals("Placed")) {
            placedStatus++;
        } else if (status.equals("Declined")) {
            declinedStatus++;
        }
    }

}
