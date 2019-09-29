package com.example.procurement;

import com.example.procurement.filters.purchaseOrder.ApprovedOrderStatus;
import com.example.procurement.filters.purchaseOrder.DeclinedOrderStatus;
import com.example.procurement.filters.purchaseOrder.DraftOrderStatus;
import com.example.procurement.filters.purchaseOrder.OrderStatus;
import com.example.procurement.filters.purchaseOrder.PendingOrderStatus;
import com.example.procurement.filters.purchaseOrder.PlacedOrderStatus;
import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderStatusTest {

    //Positive test for PendingOrderStatus class
    @Test
    public void FilterPendingStatus() {
        OrderStatus pending = new PendingOrderStatus();
        List<Order> orderList = createList(CommonConstants.ORDER_STATUS_PENDING, CommonConstants.ORDER_STATUS_PENDING);
        List<Order> pendingList = pending.meetOrderStatus(orderList);
        int actual = pendingList.size();
        assertEquals(2, actual);
    }

    //Negative test for PendingOrderStatus class
    @Test
    public void FilterNotPendingStatus() {
        OrderStatus pending = new PendingOrderStatus();
        List<Order> orderList = createList(CommonConstants.ORDER_STATUS_PENDING, CommonConstants.ORDER_STATUS_APPROVED);
        List<Order> pendingList = pending.meetOrderStatus(orderList);
        int actual = pendingList.size();
        assertEquals(1, actual);
    }

    //Positive test for ApprovedOrderStatus class
    @Test
    public void FilterApprovedStatus() {
        OrderStatus approved = new ApprovedOrderStatus();
        List<Order> orderList = createList(CommonConstants.ORDER_STATUS_APPROVED, CommonConstants.ORDER_STATUS_APPROVED);
        List<Order> approvedList = approved.meetOrderStatus(orderList);
        int actual = approvedList.size();
        assertEquals(2, actual);
    }

    //Negative test for ApprovedOrderStatus class
    @Test
    public void FilterNotApprovedStatus() {
        OrderStatus approved = new ApprovedOrderStatus();
        List<Order> orderList = createList(CommonConstants.ORDER_STATUS_APPROVED, CommonConstants.ORDER_STATUS_PENDING);
        List<Order> approvedList = approved.meetOrderStatus(orderList);
        int actual = approvedList.size();
        assertEquals(1, actual);
    }

    //Positive test for PlacedOrderStatus class
    @Test
    public void FilterPlacedStatus() {
        OrderStatus placed = new PlacedOrderStatus();
        List<Order> orderList = createList(CommonConstants.ORDER_STATUS_PLACED, CommonConstants.ORDER_STATUS_PLACED);
        List<Order> placedList = placed.meetOrderStatus(orderList);
        int actual = placedList.size();
        assertEquals(2, actual);
    }

    //Negative test for PlacedOrderStatus class
    @Test
    public void FilterNotPlacedStatus() {
        OrderStatus placed = new PlacedOrderStatus();
        List<Order> orderList = createList(CommonConstants.ORDER_STATUS_PLACED, CommonConstants.ORDER_STATUS_PENDING);
        List<Order> placedList = placed.meetOrderStatus(orderList);
        int actual = placedList.size();
        assertEquals(1, actual);
    }

    //Positive test for DraftOrderStatus class
    @Test
    public void FilterDraftStatus() {
        OrderStatus drafted = new DraftOrderStatus();
        List<Order> orderList = createList(CommonConstants.ORDER_STATUS_DRAFT, CommonConstants.ORDER_STATUS_DRAFT);
        List<Order> draftedList = drafted.meetOrderStatus(orderList);
        int actual = draftedList.size();
        assertEquals(2, actual);
    }

    //Negative test for DraftOrderStatus class
    @Test
    public void FilterNotDraftedStatus() {
        OrderStatus drafted = new DraftOrderStatus();
        List<Order> orderList = createList(CommonConstants.ORDER_STATUS_DRAFT, CommonConstants.ORDER_STATUS_PENDING);
        List<Order> draftedList = drafted.meetOrderStatus(orderList);
        int actual = draftedList.size();
        assertEquals(1, actual);
    }

    //Positive test for DeclinedOrderStatus class
    @Test
    public void FilterDeclinedStatus() {
        OrderStatus declined = new DeclinedOrderStatus();
        List<Order> orderList = createList(CommonConstants.ORDER_STATUS_DECLINED, CommonConstants.ORDER_STATUS_DECLINED);
        List<Order> declinedList = declined.meetOrderStatus(orderList);
        int actual = declinedList.size();
        assertEquals(2, actual);
    }

    //Negative test for DeclinedOrderStatus class
    @Test
    public void FilterNotDeclinedStatus() {
        OrderStatus declined = new DeclinedOrderStatus();
        List<Order> orderList = createList(CommonConstants.ORDER_STATUS_DECLINED, CommonConstants.ORDER_STATUS_PENDING);
        List<Order> declinedList = declined.meetOrderStatus(orderList);
        int actual = declinedList.size();
        assertEquals(1, actual);
    }

    private List<Order> createList(String obj1Status, String obj2Status) {
        List<Order> ordersList = new ArrayList<>();

        //temp order object 1
        Order order1 = new Order();
        order1.setOrderStatus(obj1Status);

        //temp order object 2
        Order order2 = new Order();
        order2.setOrderStatus(obj2Status);

        ordersList.add(order1);
        ordersList.add(order2);

        return ordersList;
    }
}
