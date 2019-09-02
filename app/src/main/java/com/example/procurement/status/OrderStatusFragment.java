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

import com.example.procurement.R;
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
    private View rootView;

    public OrderStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_order_status, container, false);
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

    public int getCheckedItem() {
        return checkedItem;
    }

    public void setCheckedItem(int checkedItem) {
        this.checkedItem = checkedItem;
    }

    public void writeStatusData() {
        Order order1 = new Order("1", "praveen", CommonConstants.ORDER_STATUS_PENDING, "1-06-2019");
        Order order2 = new Order("2", "kumar", CommonConstants.ORDER_STATUS_APPROVED, "2-06-2019");
        Order order3 = new Order("3", "haresh", CommonConstants.ORDER_STATUS_PLACED, "3-06-2019");
        Order order4 = new Order("4", "dhanush", CommonConstants.ORDER_STATUS_DECLINED, "4-06-2019");
        Order order5 = new Order("5", "abishaan", CommonConstants.ORDER_STATUS_PENDING, "1-06-2019");
        Order order6 = new Order("6", "roshan", CommonConstants.ORDER_STATUS_APPROVED, "2-06-2019");
        Order order7 = new Order("7", "prashan", CommonConstants.ORDER_STATUS_PLACED, "3-06-2019");
        Order order8 = new Order("8", "keerthigan", CommonConstants.ORDER_STATUS_HOLD, "4-06-2019");

        myRef.push().setValue(order1);
        myRef.push().setValue(order2);
        myRef.push().setValue(order3);
        myRef.push().setValue(order4);
        myRef.push().setValue(order5);
        myRef.push().setValue(order6);
        myRef.push().setValue(order7);
        myRef.push().setValue(order8);
    }

    public void readStatusData() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                orders.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Order order = data.getValue(Order.class);
                    orders.add(order);
                    Log.d(TAG, "Value is: " + order.getOrderID());
                }

                if (orders != null) {
                    adapter = new OrderStatusAdapter(mContext, orders);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

//    private void clickItem(final int position) {
//        Intent intent = new Intent(OrderStatusViewActivity.this, OrderViewActivity.class);
//        intent.putExtra(CommonConstants.VIEW_STATUS_ORDER_EXTRA, adapter.getItem(position).getOrderID());
//        intent.putExtra(CommonConstants.VIEW_STATUS_ORDER_POSITION, position);
//        startActivityForResult(intent);
//    }

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
                adapter.getFilter().filter(newText);
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

        }

        return super.onOptionsItemSelected(item);
    }

}
