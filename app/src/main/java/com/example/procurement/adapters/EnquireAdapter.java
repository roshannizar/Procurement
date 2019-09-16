package com.example.procurement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.models.Enquire;
import com.example.procurement.utils.CommonConstants;

import java.util.List;

public class EnquireAdapter extends RecyclerView.Adapter<EnquireAdapter.MyViewHolder> {

    private Context context;
    private List<Enquire> enquireList;

    public EnquireAdapter(Context context, List<Enquire> enquireList) {
        this.context = context;
        this.enquireList = enquireList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView noteIn, noteOut, timestampIn, timestampOut;
        final RelativeLayout relativeLayoutIn, relativeLayoutOut;

        MyViewHolder(View view) {
            super(view);
            relativeLayoutIn = view.findViewById(R.id.incoming);
            relativeLayoutOut = view.findViewById(R.id.outgoing);
            noteIn = view.findViewById(R.id.noteIn);
            noteOut = view.findViewById(R.id.noteOut);
            timestampIn = view.findViewById(R.id.timestampIn);
            timestampOut = view.findViewById(R.id.timestampOut);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_enquire_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Enquire enquire = enquireList.get(position);

        if (enquire.getEnquiryType().equals(CommonConstants.ENQUIRE_OUTGOING)) {
            holder.noteOut.setText(enquire.getEnquiry());
            holder.timestampOut.setText(enquire.getTimestamp());
         //   holder.relativeLayoutIn.setVisibility(View.GONE);

        } else if (enquire.getEnquiryType().equals(CommonConstants.ENQUIRE_INCOMING)) {
            holder.noteIn.setText(enquire.getEnquiry());
            holder.timestampIn.setText(enquire.getTimestamp());
          //  holder.relativeLayoutOut.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return enquireList.size();
    }

}
