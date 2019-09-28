package com.example.procurement.filterPattern.requisition;

import com.example.procurement.models.Requisition;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;
import java.util.List;

public class PendingRequisitionStatus implements RequisitionStatus {
    @Override
    public List<Requisition> meetRequisitionStatus(List<Requisition> requisitions) {
        List<Requisition> pendingStatus = new ArrayList<>();

        for(Requisition requisition: requisitions) {
            if(requisition.getRequisitionStatus().equals(CommonConstants.REQUISITION_STATUS_PENDING)) {
                pendingStatus.add(requisition);
            }
        }

        return pendingStatus;
    }
}
