package com.example.procurement.adapters;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.activities.HomeActivity;
import com.example.procurement.fragments.CreateOrderFragment;
import com.example.procurement.fragments.RequisitionEditFragment;
import com.example.procurement.models.Requisition;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;
import java.util.List;

public class RequisitionAdapter extends RecyclerView.Adapter<RequisitionAdapter.ViewHolder> {

    private List<Requisition> iRequisition,iRequistionFilter;

    public RequisitionAdapter(List<Requisition> iRequisition) {
        this.iRequisition = iRequisition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_requisition_list,parent,false);

        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(iRequisition!= null) {

            final Requisition requisition = iRequisition.get(position);
            final String requisitionStatus;

            requisitionStatus = requisition.getRequisitionStatus();

            int statusBackground,reqStatusIcon;

            switch (requisitionStatus) {
                case CommonConstants.ORDER_STATUS_APPROVED:
                    statusBackground = R.drawable.badge_approved;
                    reqStatusIcon = R.color.orderStatusAccepted;
                    break;
                case CommonConstants.ORDER_STATUS_PENDING:
                    statusBackground = R.drawable.badge_pending;
                    reqStatusIcon = R.color.orderStatusPending;
                    break;
                case CommonConstants.ORDER_STATUS_HOLD:
                    statusBackground = R.drawable.badge_hold;
                    reqStatusIcon = R.color.orderStatusHold;
                    break;
                default:
                    reqStatusIcon = R.color.orderStatusDenied;
                    statusBackground = R.drawable.badge_denied;
            }

            reqStatusIcon = ContextCompat.getColor(holder.txtRequisitionStatusIcon.getContext(), reqStatusIcon);

            String statusReq = requisition.getRequisitionStatus();

            System.out.println(statusReq);

            holder.txtRequisitionName.setText(requisition.getRequisitionNo());
            holder.txtRequisitionStatus.setText(requisitionStatus);
            holder.txtwithInBudget.setText(requisition.getBudget());
            holder.txtdeliveryDate.setText(requisition.getDeliveryDate());
            holder.txtTotalAmount.setText(requisition.getTotalAmount());
            holder.txtRequisitionStatus.setBackgroundResource(statusBackground);
            holder.txtRequisitionStatusIcon.setColorFilter(reqStatusIcon,PorterDuff.Mode.MULTIPLY);

            if (statusReq.equals(CommonConstants.REQUISITION_STATUS_APPROVED)) {
                holder.txtPlaceOrder.setVisibility(View.VISIBLE);
                holder.txtPlaceOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new CreateOrderFragment(requisition.getKey()), null).commit();
                    }
                });
            }

            holder.txtView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new RequisitionEditFragment(requisition.getKey()),null).commit();
                        }
                    }
            );

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

        private TextView txtRequisitionName,txtRequisitionStatus,txtPlaceOrder,txtdeliveryDate,txtView,txtwithInBudget,txtTotalAmount;
        private ImageView txtRequisitionIcon,txtRequisitionStatusIcon;

        ViewHolder(View v) {
            super(v);

            txtRequisitionName = v.findViewById(R.id.requisitionName);
            txtRequisitionStatus = v.findViewById(R.id.requisitionStatus);
            txtTotalAmount = v.findViewById(R.id.totalAmount);
            txtdeliveryDate = v.findViewById(R.id.deliveryDate);
            txtwithInBudget = v.findViewById(R.id.withInBudget);
            txtPlaceOrder = v.findViewById(R.id.placeOrder);
            txtRequisitionIcon = v.findViewById(R.id.orderIcon);
            txtRequisitionStatusIcon = v.findViewById(R.id.reqStatusIcon);
            txtView = v.findViewById(R.id.txtView);

            txtPlaceOrder.setVisibility(View.INVISIBLE);
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
