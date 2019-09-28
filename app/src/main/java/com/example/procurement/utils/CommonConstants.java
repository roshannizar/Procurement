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
    public static final String COLLECTION_REQUISITION_INVENTORY = "inventory";
    public static final String COLLECTION_REQUISITION_SUPPLIER = "supplier";
    public static final String COLLECTION_INVENTORY ="inventories";

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


    public static final String NOTE_FRAGMENT_TAG = "Note Fragment";
    public static final String ENQUIRE_FRAGMENT_TAG = "Enquire Fragment";
    public static final String ORDER_VIEW_FRAGMENT_TAG = "OrderView Fragment";
    public static final String ORDER_EDIT_FRAGMENT_TAG = "Edit Order Fragment";
    public static final String GENERATE_ORDER_FRAGMENT_TAG = "Generate Order Fragment";
    public static final String SAVE_STRING = "Save";
    public static final String UPDATE_STRING = "Update";
    public static final String CANCEL_STRING = "Cancel";

    public static ArrayList<Inventory> iInventory = new ArrayList<>();
    public static ArrayList<Supplier> iSupplier = new ArrayList<>();

    public static String ORDER_ID = "PO-0";
    public static String REQUISITION_ID = "REQ-00";

//    public static final int ORDER_STATUS_DECLINED = -1;
//    public static final int ORDER_STATUS_PENDING = 0;
//    public static final int ORDER_STATUS_APPROVED = 1;
//    public static final int ORDER_STATUS_PLACED = 2;
//    public static final int ORDER_STATUS_HOLD = 3;

//    public static final String STATUS_DECLINED_STRING = "Declined";
//    public static final String STATUS_PENDING_STRING = "Pending";
//    public static final String STATUS_APPROVED_STRING = "Approved";
//    public static final String STATUS_PLACED_STRING = "Placed";
//    public static final String STATUS_HOLD_STRING = "Hold";
//
//    public static final int ORDER_STATUS_DECLINED = -1;
//    public static final int ORDER_STATUS_PENDING = 0;
//    public static final int ORDER_STATUS_APPROVED = 1;
//    public static final int ORDER_STATUS_PLACED = 2;
//    public static final int ORDER_STATUS_HOLD = 3;


}
