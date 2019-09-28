package com.example.procurement.utils;

import com.example.procurement.models.Inventory;
import com.example.procurement.models.Supplier;

import java.util.ArrayList;

public class CommonConstants {

    public static final String COLLECTION_SITE_MANGER = "siteManagers";
    public static final String COLLECTION_ORDER = "orders";
    public static final String COLLECTION_NOTES = "notes";
    public static final String COLLECTION_ENQUIRIES = "enquiries";
    public static final String COLLECTION_NOTIFICATION = "notifications";
    public static final String COLLECTION_REQUISITION = "requisitions";
    public static final String COLLECTION_SUPPLIERS = "suppliers";
    public static final String COLLECTION_SITES = "sites";
    public static final String COLLECTION_INVENTORIES= "inventories";

    public static final String ORDER_STATUS_DECLINED = "Declined";
    public static final String ORDER_STATUS_PENDING = "Pending";
    public static final String ORDER_STATUS_APPROVED = "Approved";
    public static final String ORDER_STATUS_PLACED = "Placed";
    public static final String ORDER_STATUS_HOLD = "Hold";
    public static final String ORDER_STATUS_DRAFT = "Draft";

    public static final String REQUISITION_STATUS_DECLINED = "Declined";
    public static final String REQUISITION_STATUS_PENDING = "Pending";
    public static final String REQUISITION_STATUS_APPROVED = "Approved";
    public static final String REQUISITION_STATUS_HOLD = "Hold";

    public static final String SAVE_STRING = "Save";
    public static final String UPDATE_STRING = "Update";
    public static final String CANCEL_STRING = "Cancel";

    public static ArrayList<Inventory> iInventory = new ArrayList<>();
    public static ArrayList<Supplier> iSupplier = new ArrayList<>();

    public static String ORDER_ID = "PO-0";
    public static String REQUISITION_ID = "REQ-00";

}
