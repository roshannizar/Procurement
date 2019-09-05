package com.example.procurement.status;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;
import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.OrdersViewHolder> implements Filterable {

    private ItemClickListener mClickListener;
    private final LayoutInflater mInflater;
    private List<Order> mOrders; // copy of orders
    private List<Order> mOrdersOriginal;

    // pass data into constructor
    OrderStatusAdapter(Context context, List<Order> orders) {
        this.mInflater = LayoutInflater.from(context);
        this.mOrders = orders;
        this.mOrdersOriginal = orders;
    }

    // inflate the row layout from xml when needed
    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_order_status_item, parent, false);
        return new OrdersViewHolder(view);
    }

    // bind the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        // check if any data is available
        if (mOrders != null) {
            Order order = mOrders.get(position);
            String orderName, orderStatus, orderDate;
            orderName = order.getName();
            orderStatus = order.getStatusText();

            int statusColor, statusIcon;

            switch (order.getStatus()) {
                case CommonConstants.ORDER_STATUS_APPROVED:
                    orderDate = order.getDate();
                    statusColor = R.color.orderStatusAccepted;
                    statusIcon = R.drawable.order_ic_status_accepted;
                    break;
                case CommonConstants.ORDER_STATUS_PENDING:
                    orderDate = "--/--/--";
                    statusColor = R.color.orderStatusPending;
                    statusIcon = R.drawable.order_ic_status_pending;
                    break;
                case CommonConstants.ORDER_STATUS_PLACED:
                    orderDate = order.getDate();
                    statusColor = R.color.orderStatusPlaced;
                    statusIcon = R.drawable.order_ic_status_accepted;
                    break;
                case CommonConstants.ORDER_STATUS_HOLD:
                    orderDate = order.getDate();
                    statusColor = R.color.orderStatusHold;
                    statusIcon = R.drawable.order_ic_status_pending;
                    break;
                default:
                    orderDate = "";
                    statusColor = R.color.orderStatusDenied;
                    statusIcon = R.drawable.order_ic_status_denied;
            }

            statusColor = ContextCompat.getColor(holder.itemView.getContext(), statusColor);

            holder.name.setText(orderName);
            holder.date.setText(orderDate);
            holder.status.setText(orderStatus);
            holder.status.setTextColor(statusColor);
            holder.statusIcon.setImageResource(statusIcon);
            holder.statusIcon.setColorFilter(statusColor, PorterDuff.Mode.MULTIPLY);
            holder.orderIcon.setColorFilter(statusColor, PorterDuff.Mode.MULTIPLY);
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
    class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Hold views
        final TextView name;
        final TextView status;
        final TextView date;
        final ImageView orderIcon;
        final ImageView statusIcon;

        OrdersViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.orderName);
            status = view.findViewById(R.id.orderStatus);
            date = view.findViewById(R.id.orderDate);
            orderIcon = view.findViewById(R.id.orderIcon);
            statusIcon = view.findViewById(R.id.orderStatusIcon);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    Order getItem(int position) {
        return mOrders.get(position);
    }

    interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void resetSearch(List<Order> orders) {
        this.mOrders = orders;
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
