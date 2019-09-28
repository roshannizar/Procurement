package com.example.procurement.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.activities.HomeActivity;
import com.example.procurement.fragments.CreateOrderFragment;
import com.example.procurement.models.Order;
import com.example.procurement.models.Requisition;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;
import java.util.List;

public class RequisitionAdapter extends RecyclerView.Adapter<RequisitionAdapter.ViewHolder> {

    private List<Requisition> iRequisition,iRequistionFilter;
    private Context c;

    public RequisitionAdapter(Context c, List<Requisition> iRequisition) {
        this.c = c;
        this.iRequisition = iRequisition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_requisition_list,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(iRequisition!= null) {

            final Requisition requisition = iRequisition.get(position);
            final String requisitionStatus;

            requisitionStatus = requisition.getRequisitionStatus();

            int statusBackground;

            switch (requisitionStatus) {
                case CommonConstants.ORDER_STATUS_APPROVED:
                    statusBackground = R.drawable.badge_approved;
                    break;
                case CommonConstants.ORDER_STATUS_PENDING:
                    statusBackground = R.drawable.badge_pending;
                    break;
                case CommonConstants.ORDER_STATUS_HOLD:
                    statusBackground = R.drawable.badge_hold;
                    break;
                default:
                    statusBackground = R.drawable.badge_denied;
            }

            holder.txtRequisitionName.setText(requisition.getRequisitionNo());
            holder.txtRequisitionStatus.setText(requisitionStatus);
            holder.txtwithInBudget.setText(requisition.getBudget());
            holder.txtdeliveryDate.setText(requisition.getDeliveryDate());
            holder.txtTotalAmount.setText("Rs: " + requisition.getTotalAmount());
            holder.txtRequisitionStatus.setBackgroundResource(statusBackground);

            if (requisition.getRequisitionStatus().equals(R.string.approved)) {
                holder.txtPlaceOrder.setVisibility(View.VISIBLE);
                holder.txtPlaceOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new CreateOrderFragment(requisition.getKey()), null).commit();
                    }
                });
            } else {
                holder.txtPlaceOrder.setVisibility(View.INVISIBLE);
            }

        }
    }

    public int getItemCount() {
        if(iRequisition!= null) {
            return iRequisition.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtRequisitionName, txtRequisitionDescription,txtRequisitionStatus,txtPlaceOrder,txtEnquire,txtdeliveryDate,txtwithInBudget,txtTotalAmount;
        private ImageView txtRequisitionIcon,txtRequisitionStatusIcon;

        ViewHolder(View v) {
            super(v);

            txtRequisitionName = v.findViewById(R.id.requisitionName);
            txtRequisitionStatus = v.findViewById(R.id.requisitionStatus);
            txtTotalAmount = v.findViewById(R.id.totalAmount);
            txtdeliveryDate = v.findViewById(R.id.deliveryDate);
            txtwithInBudget = v.findViewById(R.id.withInBudget);
            txtPlaceOrder = v.findViewById(R.id.placeOrder);
            txtEnquire = v.findViewById(R.id.enquire);
            txtRequisitionIcon = v.findViewById(R.id.orderIcon);
            txtRequisitionStatusIcon = v.findViewById(R.id.orderStatusIcon);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults rReturn = new FilterResults();
                final ArrayList<Requisition> results = new ArrayList<>();
                if (iRequistionFilter == null)
                    iRequistionFilter = new ArrayList<>(iRequisition);
                if (constraint != null && constraint.length() > 0) {
                    if (iRequistionFilter != null && iRequistionFilter.size() > 0) {
                        for (final Requisition requisition: iRequistionFilter) {
                            if (requisition.getRequisitionNo() != null) {
                                if (requisition.getRequisitionNo().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                    results.add(requisition);
                                }
                            }
                        }
                    }
                    rReturn.values = results;
                    rReturn.count = results.size();
                } else {
                    rReturn.values = iRequistionFilter;
                    rReturn.count = iRequistionFilter.size();
                }
                return rReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(final CharSequence constraint,
                                          FilterResults results) {
                iRequisition = new ArrayList<>((ArrayList<Requisition>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
