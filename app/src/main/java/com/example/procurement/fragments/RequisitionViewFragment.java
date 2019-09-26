package com.example.procurement.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procurement.R;
import com.example.procurement.activities.RequisitionActivity;
import com.example.procurement.adapters.OrderStatusAdapter;
import com.example.procurement.adapters.RequisitionAdapter;
import com.example.procurement.models.Order;
import com.example.procurement.models.Requisition;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;


public class RequisitionViewFragment extends Fragment {

    private static final String TAG = "RequisitionViewFragment";
    private ArrayList<Requisition> iRequisition;
    private RecyclerView recyclerView;
    private CollectionReference requisitionRef;
    private RequisitionAdapter requisitionAdapter;
    private Context c;
    private ProgressBar progressBar;
    private TextView txtLoader,txtWait;
    private ImageView imgLoader;

    public RequisitionViewFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_requisition_view, container, false);
        setHasOptionsMenu(true);

        iRequisition = new ArrayList<>();

        progressBar = v.findViewById(R.id.progressBar3);
        txtLoader = v.findViewById(R.id.txtLoader2);
        txtWait = v.findViewById(R.id.txtWait2);
        imgLoader = v.findViewById(R.id.imgLoader2);
        recyclerView = v.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        requisitionRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_REQUISITION);

        readStatus();

        return v;
    }

    private void readStatus() {
        requisitionRef.addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if(e!= null) {
                            Log.v(TAG,"Listen Failed",e);
                        }

                        iRequisition.clear();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Requisition requisition = document.toObject(Requisition.class);
                            iRequisition.add(requisition);
                            CommonConstants.REQUISITION_ID = requisition.getRequisitionNo();
                        }

                        if (iRequisition != null) {
                            requisitionAdapter = new RequisitionAdapter(c, iRequisition);
                            progressBar.setVisibility(View.GONE);
                            imgLoader.setVisibility(View.INVISIBLE);
                            txtLoader.setVisibility(View.INVISIBLE);
                            txtWait.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(requisitionAdapter);
                        }

                        if(iRequisition.size()==0){
                            imgLoader.refreshDrawableState();
                            imgLoader.setImageResource(R.drawable.ic_safebox);
                            imgLoader.setVisibility(View.VISIBLE);
                            txtLoader.setVisibility(View.VISIBLE);
                            txtWait.setVisibility(View.VISIBLE);
                            txtLoader.setText("Requisition is empty!");
                            txtWait.setText("No point in waiting!");
                        }
                    }
                }
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        inflater.inflate(R.menu.menu_requisition, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if (!newText.equals("")) {
//                    adapter.getFilter().filter(newText);
//                }
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.filter_status) {
            Toast.makeText(this.getContext(),"Constructing still!",Toast.LENGTH_LONG).show();
//            // setup the alert builder
//            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//            builder.setTitle("Choose Status Filter Type");
//
//            // add a radio button list
//            String[] status = {
//                    getString(R.string.draft),
//                    getString(R.string.approved),
//                    getString(R.string.declined),
//                    getString(R.string.hold),
//                    getString(R.string.pending),
//                    getString(R.string.placed),
//
//            };
//
//            builder.setSingleChoiceItems(status, getCheckedItem(), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    switch (which) {
//                        case 0:
//                            adapter = new OrderStatusAdapter(mContext, draftOrderStatus.meetOrderStatus(orders));
//                            break;
//                        case 1:
//                            adapter = new OrderStatusAdapter(mContext, approvedOrderStatus.meetOrderStatus(orders));
//                            break;
//                        case 2:
//                            adapter = new OrderStatusAdapter(mContext, declinedOrderStatus.meetOrderStatus(orders));
//                            break;
//                        case 3:
//                            adapter = new OrderStatusAdapter(mContext, holdOrderStatus.meetOrderStatus(orders));
//                            break;
//                        case 4:
//                            adapter = new OrderStatusAdapter(mContext, pendingOrderStatus.meetOrderStatus(orders));
//                            break;
//                        case 5:
//                            adapter = new OrderStatusAdapter(mContext, placedOrderStatus.meetOrderStatus(orders));
//                            break;
//                        default:
//                            adapter = new OrderStatusAdapter(mContext, orders);
//                            break;
//                    }
//                    setCheckedItem(which);
//                    recyclerView.setAdapter(adapter);
//                }
//            });
//
//            // create and show the alert dialog
//            AlertDialog dialog = builder.create();
//            dialog.show();

        } else if (item.getItemId() == R.id.action_create_requisition) {
            Intent i = new Intent(this.getActivity(), RequisitionActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
