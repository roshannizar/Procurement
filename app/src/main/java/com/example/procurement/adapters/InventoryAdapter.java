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
import com.example.procurement.models.Inventory;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    private ArrayList<Inventory> iList;
    private String classType;

    public InventoryAdapter(ArrayList<Inventory> iList,String classType) {
        this.iList = iList;
        this.classType = classType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_requisition_inventory_list,parent,false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if(iList!=null) {

            Inventory i = iList.get(position);

            holder.txtNo.setText(i.getItemNo());
            holder.txtItemName.setText(i.getItemName());
            holder.txtQuantity.setText("Quantity: " + i.getQuantity());
            holder.txtUnitPrice.setText("Unit Price: " + i.getUnitprice());
            holder.txtTotalAmount.setText("Total Amount: " + (i.getQuantity() * i.getUnitprice()));

            if(classType.equals("Order")) {
                holder.btnClose.setVisibility(View.INVISIBLE);
            } else {
                holder.btnClose.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CommonConstants.iInventory.remove(position);
                                notifyDataSetChanged();
                            }
                        }
                );
            }
        }
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
