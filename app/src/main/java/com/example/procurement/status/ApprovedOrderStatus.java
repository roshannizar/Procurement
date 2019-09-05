package com.example.procurement.status;

import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;
import java.util.List;

public class ApprovedOrderStatus implements OrderStatus {

    @Override
    public List<Order> meetOrderStatus(List<Order> orders) {
        List<Order> approvedOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStatus() == CommonConstants.ORDER_STATUS_APPROVED) {
                approvedOrders.add(order);
            }
        }
        return approvedOrders;
    }
}
