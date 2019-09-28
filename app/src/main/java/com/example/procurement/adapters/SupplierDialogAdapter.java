package com.example.procurement.adapters;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.models.Supplier;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static java.security.AccessController.getContext;

public class SupplierDialogAdapter extends RecyclerView.Adapter<SupplierDialogAdapter.ViewHolder>{

    private DatePickerDialog picker;
    private ArrayList<Supplier> iSupp;
    private Context c;
    private static Context test;

    public SupplierDialogAdapter(Context c, ArrayList<Supplier> iSupp) {
        this.c = c;
        this.iSupp = iSupp;
        test = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_supplier_dialog_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Supplier supplier = iSupp.get(position);

        holder.checkBox.setText(supplier.getSupplierName());
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        holder.txtExpectedDate.setText(day+"/"+(month+1)+"/"+year);

        holder.txtExpectedDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Calendar c = Calendar.getInstance();
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int month = c.get(Calendar.MONTH);
                        int year = c.get(Calendar.YEAR);
                        DatePickerDialog picker = new DatePickerDialog(test,
                                new DatePickerDialog.OnDateSetListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        holder.txtExpectedDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                }
        );

        holder.txtStatus.setText(supplier.getSupplierStatus());

        if(holder.txtOffer.getText().toString() != "" || holder.txtOffer.getText().toString()!= null){
            holder.checkBox.setClickable(true);
            holder.checkBox.setOnCheckedChangeListener(
                    new CheckBox.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                            if (compoundButton.isChecked()) {
                                String offer = holder.txtOffer.getText().toString();

                                CommonConstants.iSupplier.add(new Supplier(supplier.getSupplierName(), holder.txtExpectedDate.getText().toString(), offer, supplier.getSupplierStatus()));
                            } else {
                                CommonConstants.iSupplier.remove(position);
                                notifyDataSetChanged();
                            }
                        }
                    }
            );
        }
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
            txtStatus = view.findViewById(R.id.txtSupplierStatus);
            checkBox = view.findViewById(R.id.cbItemSupplier);

            //checkBox.setEnabled(false);
            checkBox.setClickable(false);
        }
    }
}
