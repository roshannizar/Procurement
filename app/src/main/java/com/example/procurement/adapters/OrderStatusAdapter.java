package com.example.procurement.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.activities.HomeActivity;
import com.example.procurement.fragments.EnquireFragment;
import com.example.procurement.fragments.NoteFragment;
import com.example.procurement.fragments.OrderStatusFragment;
import com.example.procurement.fragments.OrderViewFragment;
import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;
import java.util.List;

import static com.example.procurement.R.color;
import static com.example.procurement.R.drawable;
import static com.example.procurement.R.id;
import static com.example.procurement.R.layout;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.OrdersViewHolder> implements Filterable {

    private final LayoutInflater mInflater;
    private List<Order> mOrders; // copy of orders
    private List<Order> mOrdersOriginal;
    private Context mContext;

    // pass data into constructor
    public OrderStatusAdapter(Context context, List<Order> orders) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mOrders = orders;
        this.mOrdersOriginal = orders;
    }

    // inflate the row layout from xml when needed
    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(layout.layout_order_status_item, parent, false);
        return new OrdersViewHolder(view);
    }

    // bind the data to the TextView in each row
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final OrdersViewHolder holder, int position) {
        // check if any data is available
        if (mOrders != null) {
            final Order order = mOrders.get(position);
            final String orderName, orderStatus, orderKey, orderDescription, orderId;
            orderName = order.getName();
            orderKey = order.getKey();
            orderId = order.getOrderID();
            orderDescription = order.getDescription();
            orderStatus = order.getStatus();

            int statusColor, statusIcon, statusBackground;

            switch (order.getStatus()) {
                case CommonConstants.ORDER_STATUS_APPROVED:
                    statusColor = color.orderStatusAccepted;
                    statusBackground = drawable.badge_approved;
                    statusIcon = drawable.order_ic_status_accepted;
                    break;
                case CommonConstants.ORDER_STATUS_PENDING:
                    statusColor = color.orderStatusPending;
                    statusBackground = drawable.badge_pending;
                    statusIcon = drawable.order_ic_status_pending;
                    break;
                case CommonConstants.ORDER_STATUS_PLACED:
                    statusColor = color.orderStatusPlaced;
                    statusBackground = drawable.badge_placed;
                    statusIcon = drawable.order_ic_status_accepted;
                    break;
                case CommonConstants.ORDER_STATUS_HOLD:
                    statusColor = color.orderStatusHold;
                    statusBackground = drawable.badge_hold;
                    statusIcon = drawable.order_ic_status_pending;
                    break;
                default:
                    statusColor = color.orderStatusDenied;
                    statusBackground = drawable.badge_denied;
                    statusIcon = drawable.order_ic_status_denied;
            }

            statusColor = ContextCompat.getColor(holder.itemView.getContext(), statusColor);

            holder.name.setText(orderId + " - " + orderName);
            holder.status.setText(orderStatus);
            holder.description.setText(orderDescription);
            holder.status.setBackgroundResource(statusBackground);
            holder.statusIcon.setImageResource(statusIcon);
            holder.statusIcon.setColorFilter(statusColor, PorterDuff.Mode.MULTIPLY);
            holder.orderIcon.setColorFilter(statusColor, PorterDuff.Mode.MULTIPLY);

            if (orderStatus.equals(CommonConstants.ORDER_STATUS_PLACED)) {
                holder.note.setVisibility(View.VISIBLE);
                holder.note.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new NoteFragment(orderKey), null).commit();
                    }
                });

                holder.enquireOrSend.setText("ENQUIRE");
                holder.enquireOrSend.setVisibility(View.VISIBLE);
                holder.enquireOrSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new EnquireFragment(orderKey), null).commit();
                    }
                });
            }

            if (orderStatus.equals(CommonConstants.ORDER_STATUS_PENDING)) {
                holder.enquireOrSend.setText("SEND");
                holder.enquireOrSend.setVisibility(View.VISIBLE);
                holder.enquireOrSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext,"Mail Sent !!!",Toast.LENGTH_LONG).show();
                    }
                });
            }

//            if (orderStatus.equals(CommonConstants.ORDER_STATUS_APPROVED)) {
//                holder.enquireOrSend.setText("RE-SEND");
//                holder.enquireOrSend.setVisibility(View.VISIBLE);
//                holder.enquireOrSend.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(mContext,"Mail Sent !!!",Toast.LENGTH_LONG).show();
//                    }
//                });
//            }


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderViewFragment(orderKey), null).commit();
                }
            });
        }
    }

    // gets total number of rows
    @Override
    public int getItemCount() {
        // getItemCount() is called many times, and when it is first called,
        // mOrders has not been updated (means initially, it's null, and we can't return null).
        if (mOrders != null) {
            return mOrders.size();
        } else {
            return 0;
        }
    }

    // Stores and recycles views as they are scrolled off screen
    class OrdersViewHolder extends RecyclerView.ViewHolder {
        // Hold views
        final TextView name, status, enquireOrSend, note;
        final ImageView orderIcon;
        final ImageView statusIcon;
        final TextView description;
        CardView cardView;

        OrdersViewHolder(View view) {
            super(view);
            name = view.findViewById(id.orderName);
            status = view.findViewById(id.orderStatus);
            orderIcon = view.findViewById(id.orderIcon);
            statusIcon = view.findViewById(id.orderStatusIcon);
            description = view.findViewById(id.orderDescription);
            note = view.findViewById(id.note);
            enquireOrSend = view.findViewById(id.enquireOrSend);
            cardView = view.findViewById(id.cardOrder);

            note.setVisibility(View.INVISIBLE);
            enquireOrSend.setVisibility(View.INVISIBLE);
        }

    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Order> results = new ArrayList<>();
                if (mOrdersOriginal == null)
                    mOrdersOriginal = new ArrayList<>(mOrders);
                if (constraint != null && constraint.length() > 0) {
                    if (mOrdersOriginal != null && mOrdersOriginal.size() > 0) {
                        for (final Order cd : mOrdersOriginal) {
                            if (cd.getName().toLowerCase()
                                    .contains(constraint.toString().toLowerCase()))
                                results.add(cd);
                        }
                    }
                    oReturn.values = results;
                    oReturn.count = results.size();
                } else {
                    oReturn.values = mOrdersOriginal;
                    oReturn.count = mOrdersOriginal.size();
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(final CharSequence constraint,
                                          FilterResults results) {
                mOrders = new ArrayList<>((ArrayList<Order>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
