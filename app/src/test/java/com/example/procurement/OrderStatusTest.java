package com.example.procurement;

import com.example.procurement.filters.purchaseOrder.ApprovedOrderStatus;
import com.example.procurement.filters.purchaseOrder.OrderStatus;
import com.example.procurement.models.Order;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderStatusTest {

    @Test
    public void FilterApprovedStatus() {
        List<Order> orders = new ArrayList<>();

        Order order = new Order();

        order.setOrderID("P001");
        order.setRequisitionID("REQ001");
        order.setCompany("Furgus");
        order.setVendor("HuHu");
        order.setDeliveryDate("12/12/2019");
        order.setOrderedDate("12/12/2019");
        order.setDescription("");
        order.setOrderStatus("Approved");
        order.setSubTotal(22000.0);

        Order order1 = new Order();

        order1.setOrderID("P001");
        order1.setRequisitionID("REQ001");
        order1.setCompany("Furgus");
        order1.setVendor("HuHu");
        order1.setDeliveryDate("12/12/2019");
        order1.setOrderedDate("12/12/2019");
        order1.setDescription("");
        order1.setOrderStatus("Approved");
        order1.setSubTotal(22000.0);

        OrderStatus approved = new ApprovedOrderStatus();

        orders.add(order1);
        orders.add(order);

        approved.meetOrderStatus(orders);

        int actual = approved.meetOrderStatus(orders).size();

        assertEquals(2, actual);
    }

    @Test
    public void FilterNotApprovedStatus() {
        List<Order> orders = new ArrayList<>();

        Order order = new Order();

        order.setOrderID("P001");
        order.setRequisitionID("REQ001");
        order.setCompany("Furgus");
        order.setVendor("HuHu");
        order.setDeliveryDate("12/12/2019");
        order.setOrderedDate("12/12/2019");
        order.setDescription("");
        order.setOrderStatus("Approved");
        order.setSubTotal(22000.0);

        Order order1 = new Order();

        order1.setOrderID("P001");
        order1.setRequisitionID("REQ001");
        order1.setCompany("Furgus");
        order1.setVendor("HuHu");
        order1.setDeliveryDate("12/12/2019");
        order1.setOrderedDate("12/12/2019");
        order1.setDescription("");
        order1.setOrderStatus("Declined");
        order1.setSubTotal(22000.0);

        OrderStatus approved = new ApprovedOrderStatus();

        orders.add(order1);
        orders.add(order);

        approved.meetOrderStatus(orders);

        int actual = approved.meetOrderStatus(orders).size();

        assertEquals(2, actual);
    }

}
