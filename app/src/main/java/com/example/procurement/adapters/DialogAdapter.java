package com.example.procurement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.fragments.InventoryDialog;
import com.example.procurement.models.InventoryData;

import org.w3c.dom.Text;

import java.util.List;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolder> {

    private List<InventoryData> listData;
    private Context c;

    public DialogAdapter(Context c, List<InventoryData> listData) {
        this.c = c;
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dialog_box_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InventoryData inventoryData = listData.get(position);

        holder.checkBox.setText(inventoryData.getIName());
        holder.progressBar.setProgress(Integer.parseInt(inventoryData.getProgressLevel()));
        holder.textView.setText(inventoryData.getCount());
    }

    public int getItemCount() {

        if (listData != null) {
            return 0;
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SeekBar seekBar;
        private TextView textView;
        private CheckBox checkBox;
        private ProgressBar progressBar;

        ViewHolder(View view) {
            super(view);

            seekBar = view.findViewById(R.id.seekBar);
            textView = view.findViewById(R.id.txtRemaining);
            checkBox = view.findViewById(R.id.cbItem);
            progressBar = view.findViewById(R.id.pbStockLevel);
        }
    }
}
