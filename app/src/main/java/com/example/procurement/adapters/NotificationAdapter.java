package com.example.procurement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.PMS;
import com.example.procurement.R;
import com.example.procurement.activities.HomeActivity;
import com.example.procurement.fragments.CreateOrderFragment;
import com.example.procurement.fragments.OrderViewFragment;
import com.example.procurement.models.Notification;
import com.example.procurement.utils.CommonConstants;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private DatabaseReference notificationDbRef;
    private Context context;
    private List<Notification> notificationsList;

    public NotificationAdapter(Context context, List<Notification> notificationsList) {
        this.context = context;
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
                .inflate(R.layout.layout_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Notification notification = notificationsList.get(position);
        notificationDbRef = PMS.DatabaseRef
                .child(CommonConstants.FIREBASE_NOTIFICATION_DB).getRef();

        holder.notificationOrderID.setText("Order ID : " +  notification.getOrderID());
        holder.notificationStatus.setText("Status : " + notification.getOrderStatus());

        int statusColor;

        switch (notification.getOrderStatus()) {
            case CommonConstants.ORDER_STATUS_APPROVED:
                statusColor = R.color.orderStatusAccepted;
                break;
            case CommonConstants.ORDER_STATUS_PENDING:
                statusColor = R.color.orderStatusPending;
                break;
            case CommonConstants.ORDER_STATUS_PLACED:
                statusColor = R.color.orderStatusPlaced;
                break;
            case CommonConstants.ORDER_STATUS_HOLD:
                statusColor = R.color.orderStatusHold;
                break;
            default:
                statusColor = R.color.orderStatusDenied;
        }

        holder.cvNotification.setBackgroundResource(statusColor);
        holder.cvNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationDbRef.child(notification.getNotificationKey()).removeValue();
                HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderViewFragment(notification.getOrderKey()), null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

}
