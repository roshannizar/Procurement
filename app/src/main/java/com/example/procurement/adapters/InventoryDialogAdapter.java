package com.example.procurement.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.models.Inventory;
import com.example.procurement.models.InventoryData;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;

public class InventoryDialogAdapter extends RecyclerView.Adapter<InventoryDialogAdapter.ViewHolder> {

    private ArrayList<Inventory> listData;
    private Context c;

    public InventoryDialogAdapter(Context c, ArrayList<Inventory> listData) {
        this.c = c;
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inventory_dialog_list, parent, false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Inventory inventoryData = listData.get(position);
        final double sum = inventoryData.getUnitprice() * inventoryData.getQuantity();
        holder.checkBox.setText(inventoryData.getItemName());
        holder.seekBar.setProgress(inventoryData.getQuantity());
        holder.textView.setText("Rs: "+inventoryData.getUnitprice());

        holder.checkBox.setOnCheckedChangeListener(
                new CheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(compoundButton.isChecked()) {
                            CommonConstants.iInventory.add(new Inventory(String.valueOf(position),inventoryData.getItemName(),"",inventoryData.getQuantity(),inventoryData.getUnitprice()));
                        } else {
                            CommonConstants.iInventory.remove(position);
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

        private SeekBar seekBar;
        private TextView textView;
        private CheckBox checkBox;

        ViewHolder(View view) {
            super(view);

            seekBar = view.findViewById(R.id.seekBar);
            textView = view.findViewById(R.id.txtRemaining);
            checkBox = view.findViewById(R.id.cbItem);
        }
    }
}
