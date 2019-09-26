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
import com.example.procurement.models.Inventory;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    private ArrayList<Inventory> iList;
    private Context c;

    public InventoryAdapter(Context c, ArrayList<Inventory> iList) {
        this.c = c;
        this.iList = iList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_requisition_inventory_list,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Inventory i = iList.get(position);

        holder.txtNo.setText(i.getItemNo());
        holder.txtItemName.setText(i.getItemName());
        holder.txtQuantity.setText("Quantity: "+String.valueOf(i.getQuantity()));
        holder.txtUnitPrice.setText("Unit Price: "+String.valueOf(i.getUnitprice()));
        holder.txtTotalAmount.setText("Total Amount: "+String.valueOf(i.getQuantity()*i.getUnitprice()));

        holder.btnClose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       CommonConstants.iInventory.remove(position);
                    }
                }
        );
    }

    public int getItemCount() {

        if(iList != null) {
            return iList.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNo,txtItemName,txtQuantity,txtTotalAmount,txtUnitPrice;
        private ImageView btnClose;

        ViewHolder(View v) {
            super(v);

            txtNo = v.findViewById(R.id.txtNo);
            txtItemName = v.findViewById(R.id.txtItemName);
            txtQuantity = v.findViewById(R.id.txtQuantity);
            txtTotalAmount = v.findViewById(R.id.txtTotalAmount);
            txtUnitPrice = v.findViewById(R.id.txtUnitPrice);
            btnClose = v.findViewById(R.id.btnClose);
        }
    }
}
