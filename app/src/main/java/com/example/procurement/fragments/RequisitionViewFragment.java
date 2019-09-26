package com.example.procurement.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.procurement.R;
import com.example.procurement.activities.RequisitionActivity;


public class RequisitionViewFragment extends Fragment {

    public RequisitionViewFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_requisition_view, container, false);
        setHasOptionsMenu(true);

        return v;
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
