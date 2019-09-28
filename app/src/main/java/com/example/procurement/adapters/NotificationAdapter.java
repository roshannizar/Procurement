package com.example.procurement.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.activities.HomeActivity;
import com.example.procurement.fragments.OrderViewFragment;
import com.example.procurement.fragments.RequisitionViewFragment;
import com.example.procurement.models.Notification;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.firestore.CollectionReference;

import java.util.List;

import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private CollectionReference notificationDbRef;
    private List<Notification> notificationsList;

    public NotificationAdapter(List<Notification> notificationsList) {
        this.notificationsList = notificationsList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView notificationOrderID, notificationStatus;
        final CardView cvNotification;

        MyViewHolder(View view) {
            super(view);
            cvNotification = view.findViewById(R.id.cvNotification);
            notificationOrderID = view.findViewById(R.id.notificationOrderID);
            notificationStatus = view.findViewById(R.id.notificationStatus);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_notification_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Notification notification = notificationsList.get(position);

        notificationDbRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_NOTIFICATION);

        String status = notification.getStatus();
        holder.notificationOrderID.setText(notification.getID());
        holder.notificationStatus.setText("Status : " + status);

        if (notification.getOrderKey() != null) {
            holder.notificationOrderID.setText("Order ID : " + notification.getID());
        }

        if (notification.getRequisitionKey() != null) {
            holder.notificationOrderID.setText("Requisition ID : " + notification.getID());
        }

        int statusColor;

        switch (status) {
            case CommonConstants.ORDER_STATUS_APPROVED:
                statusColor = R.color.orderStatusAccepted;
                break;
            case CommonConstants.ORDER_STATUS_HOLD:
                statusColor = R.color.orderStatusHold;
                break;
            default:
                statusColor = R.color.orderStatusDenied;
                holder.cvNotification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notificationDbRef.document(notification.getNotificationKey()).delete();
                    }
                });
                break;
        }

        holder.cvNotification.setBackgroundResource(statusColor);

        holder.cvNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationDbRef.document(notification.getNotificationKey()).delete();
                if (notification.getOrderKey() != null) {
                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderViewFragment(notification.getOrderKey()), null).commit();
                }

                if (notification.getRequisitionKey() != null) {
                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new RequisitionViewFragment(), null).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

}
