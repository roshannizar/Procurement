package com.example.procurement.filterPattern.requisition;

import com.example.procurement.models.Requisition;
import com.example.procurement.utils.CommonConstants;

import java.util.ArrayList;
import java.util.List;

public class DeclineRequisitionStatus implements RequisitionStatus {
    @Override
    public List<Requisition> meetRequisitionStatus(List<Requisition> requisitions) {
        List<Requisition> declineStatus = new ArrayList<>();

        for(Requisition requisition: requisitions) {
            if(requisition.getRequisitionStatus().equals(CommonConstants.REQUISITION_STATUS_DECLINED)) {
                declineStatus.add(requisition);
            }
        }

        return declineStatus;
    }
}
