package com.example.procurement;

import com.example.procurement.filters.requisition.ApprovedRequisitionStatus;
import com.example.procurement.filters.requisition.HoldRequisitionStatus;
import com.example.procurement.filters.requisition.PendingRequisitionStatus;
import com.example.procurement.filters.requisition.RequisitionStatus;
import com.example.procurement.models.Requisition;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RequisitionStatusTest {

    @Test
    public void FilterApprovedStatus() {

        RequisitionStatus approved = new ApprovedRequisitionStatus();
        List<Requisition> requisitions = new ArrayList<>();

        Requisition requisition = new Requisition("Req-001","Extension","Future Use","12/12/2019","12,500","Approved","Discounts","12/12/2019","Roshan Nizar","Yes");
        Requisition requisition1 = new Requisition("Req-002","New Building","Not Enough space","13/12/2019","125,000","Approved","Discounts","12/12/2019","Roshan Nizar","Yes");

        requisitions.add(requisition);
        requisitions.add(requisition1);

        approved.meetRequisitionStatus(requisitions);

        int actual = approved.meetRequisitionStatus(requisitions).size();

        assertEquals(2, actual);
    }

    @Test
    public void FilterNotApprovedStatus() {

        RequisitionStatus approved = new ApprovedRequisitionStatus();
        List<Requisition> requisitions = new ArrayList<>();

        Requisition requisition = new Requisition("Req-001","Extension","Future Use","12/12/2019","12,500","Approved","Discounts","12/12/2019","Roshan Nizar","Yes");
        Requisition requisition1 = new Requisition("Req-002","New Building","Not Enough space","13/12/2019","125,000","Hold","Discounts","12/12/2019","Roshan Nizar","Yes");

        requisitions.add(requisition);
        requisitions.add(requisition1);

        approved.meetRequisitionStatus(requisitions);

        int actual = approved.meetRequisitionStatus(requisitions).size();

        assertEquals(1, actual);
    }

    @Test
    public void FilterPendingStatus() {

        RequisitionStatus pending = new PendingRequisitionStatus();
        List<Requisition> requisitions = new ArrayList<>();

        Requisition requisition = new Requisition("Req-001","Extension","Future Use","12/12/2019","12,500","Pending","Discounts","12/12/2019","Roshan Nizar","Yes");
        Requisition requisition1 = new Requisition("Req-002","New Building","Not Enough space","13/12/2019","125,000","Pending","Discounts","12/12/2019","Roshan Nizar","Yes");

        requisitions.add(requisition);
        requisitions.add(requisition1);

        pending.meetRequisitionStatus(requisitions);

        int actual = pending.meetRequisitionStatus(requisitions).size();

        assertEquals(2, actual);
    }

    @Test
    public void FilterNotPendingStatus() {

        RequisitionStatus pending = new PendingRequisitionStatus();
        List<Requisition> requisitions = new ArrayList<>();

        Requisition requisition = new Requisition("Req-001","Extension","Future Use","12/12/2019","12,500","Pending","Discounts","12/12/2019","Roshan Nizar","Yes");
        Requisition requisition1 = new Requisition("Req-002","New Building","Not Enough space","13/12/2019","125,000","Hold","Discounts","12/12/2019","Roshan Nizar","Yes");

        requisitions.add(requisition);
        requisitions.add(requisition1);

        pending.meetRequisitionStatus(requisitions);

        int actual = pending.meetRequisitionStatus(requisitions).size();

        assertEquals(1, actual);
    }

    @Test
    public void FilterHoldStatus() {

        RequisitionStatus hold = new HoldRequisitionStatus();
        List<Requisition> requisitions = new ArrayList<>();

        Requisition requisition = new Requisition("Req-001","Extension","Future Use","12/12/2019","12,500","Hold","Discounts","12/12/2019","Roshan Nizar","Yes");
        Requisition requisition1 = new Requisition("Req-002","New Building","Not Enough space","13/12/2019","125,000","Hold","Discounts","12/12/2019","Roshan Nizar","Yes");

        requisitions.add(requisition);
        requisitions.add(requisition1);

        hold.meetRequisitionStatus(requisitions);

        int actual = hold.meetRequisitionStatus(requisitions).size();

        assertEquals(2, actual);
    }

    @Test
    public void FilterNotHoldStatus() {

        RequisitionStatus hold = new HoldRequisitionStatus();
        List<Requisition> requisitions = new ArrayList<>();

        Requisition requisition = new Requisition("Req-001","Extension","Future Use","12/12/2019","12,500","Pending","Discounts","12/12/2019","Roshan Nizar","Yes");
        Requisition requisition1 = new Requisition("Req-002","New Building","Not Enough space","13/12/2019","125,000","Hold","Discounts","12/12/2019","Roshan Nizar","Yes");

        requisitions.add(requisition);
        requisitions.add(requisition1);

        hold.meetRequisitionStatus(requisitions);

        int actual = hold.meetRequisitionStatus(requisitions).size();

        assertEquals(1, actual);
    }
}
