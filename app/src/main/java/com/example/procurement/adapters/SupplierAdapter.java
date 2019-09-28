package com.example.procurement.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.models.Supplier;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.ViewHolder> {

    private ArrayList<Supplier> iSupplier;

    public SupplierAdapter(ArrayList<Supplier> iSupplier) {
        this.iSupplier = iSupplier;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_requisition_supplier_list,parent,false);

        return new SupplierAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SupplierAdapter.ViewHolder holder, final int position) {
        Supplier s = iSupplier.get(position);

        holder.txtSppNo.setText(s.getSupplierId());
        holder.txtSupplierName.setText(s.getSupplierName());
        holder.txtOffer.setText("Offer: "+s.getOffer());
        holder.txtDelivery.setText("Date: "+s.getExpectedDate());

        holder.btnClose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonConstants.iSupplier.remove(position);
                        notifyDataSetChanged();
                    }
                }
        );

    }

    public int getItemCount() {

        if(iSupplier != null) {
            return iSupplier.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtSppNo,txtSupplierName,txtDelivery,txtOffer;
        private ImageView btnClose;

        ViewHolder(View v) {
            super(v);

            txtSppNo = v.findViewById(R.id.txtSuppNo);
            txtSupplierName = v.findViewById(R.id.txtSupplierName);
            txtDelivery = v.findViewById(R.id.txtDelivery);
            txtOffer = v.findViewById(R.id.txtOffer);
            btnClose = v.findViewById(R.id.btnSupplierClose);
        }
    }
}
