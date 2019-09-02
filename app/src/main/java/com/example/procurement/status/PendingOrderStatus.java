package com.example.procurement.status;

import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderStatus implements OrderStatus {

    @Override
    public List<Order> meetOrderStatus(List<Order> orders) {
        List<Order> pendingOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStatus() == CommonConstants.ORDER_STATUS_PENDING) {
                pendingOrders.add(order);
            }
        }
        return pendingOrders;
    }
}