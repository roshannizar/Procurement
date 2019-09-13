package com.example.procurement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.models.Notification;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {

    private Context context;
    private List<Notification> notificationsList;

    public NotificationsAdapter(Context context, List<Notification> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView notificationOrder, notificationStatus;

        MyViewHolder(View view) {
            super(view);
            notificationOrder = view.findViewById(R.id.notificationOrder);
            notificationStatus = view.findViewById(R.id.notificationStatus);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notification notification = notificationsList.get(position);
        String order = notification.getOrderID() + " " + notification.getOrderName();
        holder.notificationOrder.setText(order);
        holder.notificationStatus.setText(notification.getOrderName());
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

}
