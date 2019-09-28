package com.example.procurement.filters.requisition;

import com.example.procurement.models.Requisition;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;
import java.util.List;

public class ApprovedRequisitionStatus implements RequisitionStatus {
    @Override
    public List<Requisition> meetRequisitionStatus(List<Requisition> requisitions) {
        List<Requisition> approvedStatus = new ArrayList<>();

        for(Requisition requisition: requisitions) {
            if(requisition.getRequisitionStatus().equals(CommonConstants.REQUISITION_STATUS_APPROVED)) {
                approvedStatus.add(requisition);
            }
        }

        return approvedStatus;
    }
}
