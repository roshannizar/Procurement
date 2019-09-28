package com.example.procurement.filters.requisition;

import com.example.procurement.models.Requisition;

import java.util.List;

public interface RequisitionStatus {
    List<Requisition> meetRequisitionStatus(List<Requisition> requisitions);
}
