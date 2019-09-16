package com.example.procurement.status;

import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;
import java.util.List;

public class PlacedOrderStatus implements OrderStatus {

    @Override
    public List<Order> meetOrderStatus(List<Order> orders) {
        List<Order> placedOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStatus().equals(CommonConstants.ORDER_STATUS_PLACED)) {
                placedOrders.add(order);
            }
        }
        return placedOrders;
    }
}
