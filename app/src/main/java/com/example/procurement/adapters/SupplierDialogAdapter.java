package com.example.procurement.adapters;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

public class SupplierDialogAdapter extends RecyclerView.Adapter<SupplierDialogAdapter.ViewHolder>{

    private ArrayList<Supplier> iSupp;

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public SupplierDialogAdapter(Context context, ArrayList<Supplier> iSupp) {
        this.iSupp = iSupp;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_supplier_dialog_list, parent, false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Supplier supplier = iSupp.get(position);

        holder.checkBox.setText(supplier.getSupplierName());
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        holder.txtExpectedDate.setText(day+"/"+(month+1)+"/"+year);

        holder.txtOffer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!holder.txtOffer.getText().toString().equals("")){
                    holder.checkBox.setClickable(true);
                    holder.checkBox.setEnabled(true);
                    holder.checkBox.setOnCheckedChangeListener(
                            new CheckBox.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                    if (compoundButton.isChecked()) {
                                        String offer = holder.txtOffer.getText().toString();

                                        CommonConstants.iSupplier.add(new Supplier(supplier.getSupplierName(), holder.txtExpectedDate.getText().toString(), offer, supplier.getStatus()));
                                    } else {
                                        CommonConstants.iSupplier.remove(position);
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

        holder.txtExpectedDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Calendar c = Calendar.getInstance();
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int month = c.get(Calendar.MONTH);
                        int year = c.get(Calendar.YEAR);
                        DatePickerDialog picker = new DatePickerDialog(mContext,
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

        holder.txtStatus.setText(supplier.getStatus());

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

            checkBox.setEnabled(false);
            checkBox.setClickable(false);
        }
    }
}
