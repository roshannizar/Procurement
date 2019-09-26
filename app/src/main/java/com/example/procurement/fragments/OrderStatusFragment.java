package com.example.procurement.fragments;


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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.adapters.OrderStatusAdapter;
import com.example.procurement.filterPattern.ApprovedOrderStatus;
import com.example.procurement.filterPattern.DeclinedOrderStatus;
import com.example.procurement.filterPattern.DraftOrderStatus;
import com.example.procurement.filterPattern.HoldOrderStatus;
import com.example.procurement.filterPattern.OrderStatus;
import com.example.procurement.filterPattern.PendingOrderStatus;
import com.example.procurement.filterPattern.PlacedOrderStatus;
import com.example.procurement.models.Order;
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
    private ImageView imgLoader;
    private TextView txtLoader, txtWait;
    static int pendingStatus = 0, approvedStatus = 0, holdStatus = 0, placedStatus = 0, declinedStatus = 0, draftStatus = 0;

    public OrderStatusFragment() {
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
        txtWait = rootView.findViewById(R.id.txtWait);
        txtLoader = rootView.findViewById(R.id.txtLoader);
        imgLoader = rootView.findViewById(R.id.imgLoader);

        // writeStatusData();
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
        Order order = new Order();
        order.setOrderID("PO.1");
        order.setRequisitionID("RQ.1");
        order.setOrderName("Bricks");
        order.setCompany("VIP");
        order.setVendor("Praveen");
        order.setDeliveryDate("21-10-2019");
        order.setOrderedDate("15-09-2019");
        order.setDescription("Lorem LoremLoremLoremLoremLoremLoremLoremLorem ");
        order.setOrderStatus("Pending");
        order.setSubTotal(2500.00);
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

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Order order = document.toObject(Order.class);
                        countStatus(order.getOrderStatus());
                        orders.add(order);
                        CommonConstants.ORDER_ID = order.getOrderID();
                    }

                    if (orders != null) {
                        adapter = new OrderStatusAdapter(mContext, orders);
                        progressBar.setVisibility(View.GONE);
                        imgLoader.setVisibility(View.INVISIBLE);
                        txtLoader.setVisibility(View.INVISIBLE);
                        txtWait.setVisibility(View.INVISIBLE);
                        recyclerView.setAdapter(adapter);

                        if (orders.size() == 0) {
                            imgLoader.refreshDrawableState();
                            imgLoader.setImageResource(R.drawable.ic_safebox);
                            imgLoader.setVisibility(View.VISIBLE);
                            txtLoader.setVisibility(View.VISIBLE);
                            txtWait.setVisibility(View.VISIBLE);
                            txtLoader.setText("Purchase Order is empty!");
                            txtWait.setText("No point in waiting!");
                        }
                    }

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
                if (!newText.equals("") && !newText.isEmpty()) {
                    try {
                        adapter.getFilter().filter(newText);
                    } catch (NullPointerException e) {
                        Log.w(TAG, "Caught NullPointerException");
                    }
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
