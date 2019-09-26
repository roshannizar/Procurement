package com.example.procurement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.models.Requisition;

import java.util.ArrayList;

public class RequisitionAdapter extends RecyclerView.Adapter<RequisitionAdapter.ViewHolder> {

    private ArrayList<Requisition> iRequisition;
    private Context c;

    public RequisitionAdapter(Context c, ArrayList<Requisition> iRequisition) {
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
        Requisition requisition = iRequisition.get(position);

        holder.txtRequisitionName.setText(requisition.getRequisitionNo());
        holder.txtRequisitionStatus.setText(requisition.getRequisitionStatus());
        holder.txtwithInBudget.setText(requisition.getBudget());
        holder.txtdeliveryDate.setText(requisition.getDeliveryDate());
        holder.txtTotalAmount.setText("Rs: "+requisition.getTotalAmount());
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
}
