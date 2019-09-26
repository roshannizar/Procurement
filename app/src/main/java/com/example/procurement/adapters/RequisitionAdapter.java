package com.example.procurement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_requisition_list,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    public int getItemCount() {
        if(iRequisition!= null) {
            return iRequisition.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View v) {
            super(v);
        }
    }
}
