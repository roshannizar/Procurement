package com.example.procurement.filters.purchaseOrder;

import com.example.procurement.models.Order;

import java.util.List;

public interface OrderStatus {
    List<Order> meetOrderStatus(List<Order> orders);
}