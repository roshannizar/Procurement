package com.example.procurement.adapters;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.models.Inventory;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;

public class InventoryDialogAdapter extends RecyclerView.Adapter<InventoryDialogAdapter.ViewHolder> {

    private ArrayList<Inventory> listData;

    public InventoryDialogAdapter(ArrayList<Inventory> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_supplier_item_list, parent, false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Inventory inventoryData = listData.get(position);
        //final double sum = inventoryData.getUnitprice() * inventoryData.getQuantity();
        holder.checkBox.setText(inventoryData.getItemName());
        holder.txtQty.setText("0");
        holder.txtunitprice.setText(String.valueOf(inventoryData.getUnitprice()));

        holder.txtQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!holder.txtQty.getText().toString().equals("0")) {
                    holder.checkBox.setClickable(true);
                    holder.checkBox.setEnabled(true);
                    holder.checkBox.setOnCheckedChangeListener(
                            new CheckBox.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                    if (compoundButton.isChecked()) {
                                        int quantity = Integer.parseInt(holder.txtQty.getText().toString());
                                        CommonConstants.iInventory.add(new Inventory(String.valueOf(position), inventoryData.getItemName(), "", quantity, inventoryData.getUnitprice()));
                                    } else {
                                        CommonConstants.iInventory.remove(position);
                                        notifyDataSetChanged();
                                    }
                                }
                            }
                    );
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.txtIncrease.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(holder.txtQty.getText().toString().equals("0")) {
                            holder.txtQty.setText("1");
                        } else {
                            int value = Integer.parseInt(holder.txtQty.getText().toString());
                            int total = value +1;
                            holder.txtQty.setText(String.valueOf(total));
                        }
                    }
                }
        );

        holder.txtReduce.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(holder.txtQty.getText().toString().equals("0")) {
                            holder.txtQty.setText("0");
                        } else {
                            int value = Integer.parseInt(holder.txtQty.getText().toString());
                            int total = value -1;
                            holder.txtQty.setText(String.valueOf(total));
                        }
                    }
                }
        );
    }

    public int getItemCount() {

        if (listData != null) {
            return listData.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDescription,txtunitprice,txtReduce,txtIncrease;
        private EditText txtQty;
        private CheckBox checkBox;

        ViewHolder(View view) {
            super(view);

            txtDescription = view.findViewById(R.id.description);
            txtunitprice = view.findViewById(R.id.unitPrice);
            txtQty = view.findViewById(R.id.qty);
            txtReduce = view.findViewById(R.id.reduce);
            txtIncrease = view.findViewById(R.id.increase);
            checkBox = view.findViewById(R.id.cbItem);

            checkBox.setEnabled(false);
            checkBox.setClickable(false);
        }
    }
}
