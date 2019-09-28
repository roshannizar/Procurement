package com.example.procurement.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.models.Enquire;

import java.util.List;

public class EnquireAdapter extends RecyclerView.Adapter<EnquireAdapter.MyViewHolder> {

    private List<Enquire> enquireList;

    public EnquireAdapter(List<Enquire> enquireList) {
        this.enquireList = enquireList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView noteIn, noteOut, timestampIn, timestampOut,enquiryID;
        final RelativeLayout relativeLayoutIn, relativeLayoutOut;

        MyViewHolder(View view) {
            super(view);
            relativeLayoutIn = view.findViewById(R.id.incoming);
            relativeLayoutOut = view.findViewById(R.id.outgoing);
            noteIn = view.findViewById(R.id.noteIn);
            noteOut = view.findViewById(R.id.noteOut);
            timestampIn = view.findViewById(R.id.timestampIn);
            timestampOut = view.findViewById(R.id.timestampOut);
            enquiryID =view.findViewById(R.id.enquiryID);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_enquire_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Enquire enquire = enquireList.get(position);

        String eid = "Enquiry " + (position + 1) + " : ";
        holder.enquiryID.setText(eid);
        holder.noteOut.setText(enquire.getEnquiry());
        holder.timestampOut.setText(enquire.getTimestamp());
        holder.relativeLayoutIn.setVisibility(View.GONE);

        if (enquire.isReplied()) {
            holder.noteIn.setText(enquire.getReply());
            holder.timestampIn.setVisibility(View.GONE);
            holder.relativeLayoutIn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return enquireList.size();
    }

}
