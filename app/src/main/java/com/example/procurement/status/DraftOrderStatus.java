package com.example.procurement.status;

import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;
import java.util.List;

public class DraftOrderStatus implements OrderStatus {

    @Override
    public List<Order> meetOrderStatus(List<Order> orders) {
        List<Order> draftOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStatus().equals(CommonConstants.ORDER_STATUS_DRAFT)) {
                draftOrders.add(order);
            }
        }
        return draftOrders;
    }
}
