package com.example.procurement.status;

import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;
import java.util.List;

public class PlacedOrderStatus implements OrderStatus {

    @Override
    public List<Order> meetOrderStatus(List<Order> orders) {
        List<Order> placedOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStatus() == CommonConstants.ORDER_STATUS_PLACED) {
                placedOrders.add(order);
            }
        }
        return placedOrders;
    }
}
