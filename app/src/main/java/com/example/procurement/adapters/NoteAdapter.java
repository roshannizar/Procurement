package com.example.procurement.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.models.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    private List<Note> noteList;

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView note, timestamp , invoiceNo;

        MyViewHolder(View view) {
            super(view);
            note = view.findViewById(R.id.note);
            invoiceNo = view.findViewById(R.id.invoiceNo);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_note_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.note.setText(note.getNote());
        holder.invoiceNo.setText(note.getInvoiceNo());
        holder.timestamp.setText(note.getTimestamp());

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

}
