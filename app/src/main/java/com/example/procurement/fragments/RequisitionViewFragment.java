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
import com.example.procurement.activities.RequisitionActivity;
import com.example.procurement.adapters.RequisitionAdapter;
import com.example.procurement.filters.requisition.ApprovedRequisitionStatus;
import com.example.procurement.filters.requisition.DeclineRequisitionStatus;
import com.example.procurement.filters.requisition.HoldRequisitionStatus;
import com.example.procurement.filters.requisition.PendingRequisitionStatus;
import com.example.procurement.filters.requisition.RequisitionStatus;
import com.example.procurement.models.Requisition;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;


public class RequisitionViewFragment extends Fragment {

    private static final String TAG = "RequisitionViewFragment";
    private ArrayList<Requisition> iRequisition;
    private RecyclerView recyclerView;
    private CollectionReference requisitionRef;
    private RequisitionAdapter requisitionAdapter;
    private Context c;
    private int checkedItem = -99;
    private ProgressBar progressBar;
    private TextView txtLoader, txtWait;
    private ImageView imgLoader;
    private RequisitionStatus approvedStatus,declinedStatus,holdStatus,pendingStatus;
    private int approvedCount,delinedCount,holdCount,pendingCount;

    public RequisitionViewFragment() {

    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate((bundle));
        approvedCount=0;
        delinedCount=0;
        holdCount=0;
        pendingCount=0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_requisition_view, container, false);
        setHasOptionsMenu(true);

        iRequisition = new ArrayList<>();
        c=v.getContext();
        progressBar = v.findViewById(R.id.progressBar3);
        txtLoader = v.findViewById(R.id.txtLoader2);
        txtWait = v.findViewById(R.id.txtWait2);
        imgLoader = v.findViewById(R.id.imgLoader2);
        recyclerView = v.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        approvedStatus = new ApprovedRequisitionStatus();
        declinedStatus= new DeclineRequisitionStatus();
        holdStatus = new HoldRequisitionStatus();
        pendingStatus = new PendingRequisitionStatus();

        requisitionRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_REQUISITION);

        readStatus();

        return v;
    }

    private void readStatus() {
        requisitionRef.orderBy("requisitionNo").addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.v(TAG, "Listen Failed", e);
                        }

                        iRequisition.clear();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Requisition requisition = document.toObject(Requisition.class);
                            iRequisition.add(requisition);
                            CommonConstants.REQUISITION_ID = requisition.getRequisitionNo();
                        }
                        Collections.reverse(iRequisition);

                        if (iRequisition != null) {
                            requisitionAdapter = new RequisitionAdapter(c, iRequisition);
                            progressBar.setVisibility(View.GONE);
                            imgLoader.setVisibility(View.INVISIBLE);
                            txtLoader.setVisibility(View.INVISIBLE);
                            txtWait.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(requisitionAdapter);

                            if (iRequisition.size() == 0) {
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
                }
        );
    }

    private int getCheckedItem() {
        return checkedItem;
    }

    private void setCheckedItem(int checkedItem) {
        this.checkedItem = checkedItem;
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
                if (!newText.equals("")) {
                    requisitionAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.filter_status) {

            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle("Choose Status Filter Type");

            // add a radio button list
            String[] status = {
                    getString(R.string.approved),
                    getString(R.string.declined),
                    getString(R.string.hold),
                    getString(R.string.pending),

            };

            builder.setSingleChoiceItems(status, getCheckedItem(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            requisitionAdapter = new RequisitionAdapter(c, approvedStatus.meetRequisitionStatus(iRequisition));
                            break;
                        case 1:
                            requisitionAdapter = new RequisitionAdapter(c, declinedStatus.meetRequisitionStatus(iRequisition));
                            break;
                        case 2:
                            requisitionAdapter = new RequisitionAdapter(c, holdStatus.meetRequisitionStatus(iRequisition));
                            break;
                        case 3:
                            requisitionAdapter = new RequisitionAdapter(c, pendingStatus.meetRequisitionStatus(iRequisition));
                            break;
                        default:
                            requisitionAdapter = new RequisitionAdapter(c, iRequisition);
                            break;
                    }
                    setCheckedItem(which);
                    recyclerView.setAdapter(requisitionAdapter);
                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (item.getItemId() == R.id.action_create_requisition) {
            Intent i = new Intent(this.getActivity(), RequisitionActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
