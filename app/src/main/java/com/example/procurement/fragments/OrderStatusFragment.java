package com.example.procurement.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.procurement.activities.HomeActivity;
import com.example.procurement.adapters.OrderStatusAdapter;
import com.example.procurement.models.Order;
import com.example.procurement.status.ApprovedOrderStatus;
import com.example.procurement.status.DeclinedOrderStatus;
import com.example.procurement.status.DraftOrderStatus;
import com.example.procurement.status.HoldOrderStatus;
import com.example.procurement.status.OrderStatus;
import com.example.procurement.status.PendingOrderStatus;
import com.example.procurement.status.PlacedOrderStatus;
import com.example.procurement.utils.CommonConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;

public class OrderStatusFragment extends Fragment {

    private static final String TAG = "OrderStatusFragment";
    private ArrayList<Order> orders;
    private OrderStatusAdapter adapter;
    private OrderStatus approvedOrderStatus, declinedOrderStatus, placedOrderStatus, pendingOrderStatus, holdOrderStatus, draftOrderStatus;
    private RecyclerView recyclerView;
    private int checkedItem = -99;
    private CollectionReference orderDBRef;
    private ProgressBar progressBar;
    private Context mContext;
    public static int pendingStatus = 0, approvedStatus = 0, holdStatus = 0, placedStatus = 0, declinedStatus = 0, draftStatus = 0;

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
        draftStatus = 0;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_order_status, container, false);
        mContext = rootView.getContext();

        orderDBRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_ORDER);
        orders = new ArrayList<>();

        approvedOrderStatus = new ApprovedOrderStatus();
        declinedOrderStatus = new DeclinedOrderStatus();
        pendingOrderStatus = new PendingOrderStatus();
        placedOrderStatus = new PlacedOrderStatus();
        holdOrderStatus = new HoldOrderStatus();
        draftOrderStatus = new DraftOrderStatus();

        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.rvLoading);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //writeStatusData();
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

        String key = orderDBRef.document().getId();
        Order order = new Order("PO-02", "Praveen", "", CommonConstants.ORDER_STATUS_PENDING, "1-06-2019");
        order.setKey(key);
        orderDBRef.document(key)
                .set(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void readStatusData() {

        orderDBRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                }

                orders.clear();

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Order order = document.toObject(Order.class);
                    countStatus(order.getStatus());
                    orders.add(order);
                    CommonConstants.ORDER_ID = order.getOrderID();
                }

                if (orders != null) {
                    adapter = new OrderStatusAdapter(mContext, orders);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        inflater.inflate(R.menu.order_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search by Order Name");

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
                    getString(R.string.draft),
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
                            adapter = new OrderStatusAdapter(mContext, draftOrderStatus.meetOrderStatus(orders));
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
                        default:
                            adapter = new OrderStatusAdapter(mContext, orders);
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
            HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new CreatePurchaseOrderFragment(), null).commit();
            //HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new CreateOrderFragment(), null).commit();
//            Intent i = new Intent(this.getActivity(), RequisitionActivity.class);
//            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void countStatus(String status) {

        switch (status) {
            case CommonConstants.ORDER_STATUS_PENDING:
                pendingStatus++;
                break;
            case CommonConstants.ORDER_STATUS_HOLD:
                holdStatus++;
                break;
            case CommonConstants.ORDER_STATUS_APPROVED:
                approvedStatus++;
                break;
            case CommonConstants.ORDER_STATUS_PLACED:
                placedStatus++;
                break;
            case CommonConstants.ORDER_STATUS_DECLINED:
                declinedStatus++;
                break;
            case CommonConstants.ORDER_STATUS_DRAFT:
                draftStatus++;
                break;
        }
    }

}
