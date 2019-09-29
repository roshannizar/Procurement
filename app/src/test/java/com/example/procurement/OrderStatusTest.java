package com.example.procurement;

import com.example.procurement.filters.purchaseOrder.ApprovedOrderStatus;
import com.example.procurement.filters.purchaseOrder.DraftOrderStatus;
import com.example.procurement.filters.purchaseOrder.OrderStatus;
import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderStatusTest {

    @Test
    public void FilterApprovedStatus() {
        List<Order> orders = new ArrayList<>();

        //temp order object 1
        Order order1 = new Order();
        order1.setOrderID("P001");
        order1.setRequisitionID("REQ001");
        order1.setCompany("Furgus");
        order1.setVendor("HuHu");
        order1.setDeliveryDate("12/12/2019");
        order1.setOrderedDate("12/12/2019");
        order1.setDescription("");
        order1.setOrderStatus(CommonConstants.ORDER_STATUS_APPROVED);
        order1.setSubTotal(22000.0);

        //temp order object 2
        Order order2 = new Order();
        order1.setOrderID("P001");
        order1.setRequisitionID("REQ001");
        order1.setCompany("Furgus");
        order1.setVendor("HuHu");
        order1.setDeliveryDate("12/12/2019");
        order1.setOrderedDate("12/12/2019");
        order1.setDescription("");
        order1.setOrderStatus(CommonConstants.ORDER_STATUS_APPROVED);
        order1.setSubTotal(22000.0);

        OrderStatus approved = new ApprovedOrderStatus();

        orders.add(order1);
        orders.add(order2);

        List<Order> orderList = approved.meetOrderStatus(orders);
        int actual = orderList.size();
        assertEquals(2, actual);

    }

    @Test
    public void FilterNotApprovedStatus() {
        List<Order> orders = new ArrayList<>();

        //temp order object 1
        Order order1 = new Order();
        order1.setOrderID("P001");
        order1.setRequisitionID("REQ001");
        order1.setCompany("Furgus");
        order1.setVendor("HuHu");
        order1.setDeliveryDate("12/12/2019");
        order1.setOrderedDate("12/12/2019");
        order1.setDescription("");
        order1.setOrderStatus(CommonConstants.ORDER_STATUS_APPROVED);
        order1.setSubTotal(22000.0);

        //temp order object 2
        Order order2 = new Order();
        order1.setOrderID("P001");
        order1.setRequisitionID("REQ001");
        order1.setCompany("Furgus");
        order1.setVendor("HuHu");
        order1.setDeliveryDate("12/12/2019");
        order1.setOrderedDate("12/12/2019");
        order1.setDescription("");
        order1.setOrderStatus(CommonConstants.ORDER_STATUS_PENDING);
        order1.setSubTotal(22000.0);

        OrderStatus approved = new ApprovedOrderStatus();

        orders.add(order1);
        orders.add(order2);

        List<Order> orderList = approved.meetOrderStatus(orders);
        int actual = orderList.size();
        assertEquals(2, actual);
    }

    @Test
    public void FilterDraftStatus() {
        List<Order> orders = new ArrayList<>();

        //temp order object 1
        Order order1 = new Order();
        order1.setOrderID("P001");
        order1.setRequisitionID("REQ001");
        order1.setCompany("Furgus");
        order1.setVendor("HuHu");
        order1.setDeliveryDate("12/12/2019");
        order1.setOrderedDate("12/12/2019");
        order1.setDescription("");
        order1.setOrderStatus(CommonConstants.ORDER_STATUS_DRAFT);
        order1.setSubTotal(22000.0);

        //temp order object 2
        Order order2 = new Order();
        order1.setOrderID("P001");
        order1.setRequisitionID("REQ001");
        order1.setCompany("Furgus");
        order1.setVendor("HuHu");
        order1.setDeliveryDate("12/12/2019");
        order1.setOrderedDate("12/12/2019");
        order1.setDescription("");
        order1.setOrderStatus(CommonConstants.ORDER_STATUS_DRAFT);
        order1.setSubTotal(22000.0);

        OrderStatus drafted = new DraftOrderStatus();

        orders.add(order1);
        orders.add(order2);

        List<Order> orderList = drafted.meetOrderStatus(orders);
        int actual = orderList.size();
        assertEquals(2, actual);
    }

    @Test
    public void FilterNotDraftedStatus() {
        List<Order> orders = new ArrayList<>();

        //temp order object 1
        Order order1 = new Order();
        order1.setOrderID("P001");
        order1.setRequisitionID("REQ001");
        order1.setCompany("Furgus");
        order1.setVendor("HuHu");
        order1.setDeliveryDate("12/12/2019");
        order1.setOrderedDate("12/12/2019");
        order1.setDescription("");
        order1.setOrderStatus(CommonConstants.ORDER_STATUS_DRAFT);
        order1.setSubTotal(22000.0);

        //temp order object 2
        Order order2 = new Order();
        order1.setOrderID("P001");
        order1.setRequisitionID("REQ001");
        order1.setCompany("Furgus");
        order1.setVendor("HuHu");
        order1.setDeliveryDate("12/12/2019");
        order1.setOrderedDate("12/12/2019");
        order1.setDescription("");
        order1.setOrderStatus(CommonConstants.ORDER_STATUS_PENDING);
        order1.setSubTotal(22000.0);

        OrderStatus drafted = new DraftOrderStatus();

        orders.add(order1);
        orders.add(order2);

        List<Order> orderList = drafted.meetOrderStatus(orders);
        int actual = orderList.size();
        assertEquals(1, actual);
    }

}
