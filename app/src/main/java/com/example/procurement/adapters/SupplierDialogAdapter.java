package com.example.procurement.adapters;

import android.content.Context;
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
import com.example.procurement.models.Supplier;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;

public class SupplierDialogAdapter extends RecyclerView.Adapter<SupplierDialogAdapter.ViewHolder>{

    private ArrayList<Supplier> iSupp;
    private Context c;

    public SupplierDialogAdapter(Context c, ArrayList<Supplier> iSupp) {
        this.c = c;
        this.iSupp = iSupp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_supplier_dialog_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SupplierDialogAdapter.ViewHolder holder, final int position) {
        final Supplier supplier = iSupp.get(position);

        holder.txtExpectedDate.setText(supplier.getExpectedDate());
        holder.txtStatus.setText(supplier.getSupplierStatus());
        holder.txtOffer.setText(supplier.getOffer());

        holder.checkBox.setOnCheckedChangeListener(
                new CheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(compoundButton.isChecked()) {
                            CommonConstants.iSupplier.add(new Supplier(supplier.getSupplierName(),supplier.getExpectedDate(),supplier.getOffer(),supplier.getSupplierStatus()));
                        } else {
                            CommonConstants.iSupplier.remove(position);
                        }
                    }
                }
        );
    }

    public int getItemCount() {

        if (iSupp != null) {
            return iSupp.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtExpectedDate,txtStatus;
        private CheckBox checkBox;
        private EditText txtOffer;

        ViewHolder(View view) {
            super(view);

            txtOffer = view.findViewById(R.id.txtOffer);
            txtExpectedDate = view.findViewById(R.id.dtpExpectedDate);
            txtStatus = view.findViewById(R.id.txtStatus);
            checkBox = view.findViewById(R.id.cbItem);
        }
    }
}
