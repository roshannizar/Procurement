package com.example.procurement.utils;

import com.example.procurement.models.Inventory;
import com.example.procurement.models.Supplier;

import java.util.ArrayList;

public class CommonConstants {

    //welcome splash screen timeout
    public static final int WELCOME_TIMEOUT = 1000;

    //firestore db collection references
    public static final String COLLECTION_SITE_MANGER = "siteManagers";
    public static final String COLLECTION_ORDER = "orders";
    public static final String COLLECTION_NOTES = "notes";
    public static final String COLLECTION_ENQUIRIES = "enquiries";
    public static final String COLLECTION_NOTIFICATION = "notifications";
    public static final String COLLECTION_REQUISITION = "requisitions";
    public static final String COLLECTION_SUPPLIERS = "suppliers";
    public static final String COLLECTION_SITES = "sites";
    public static final String COLLECTION_INVENTORIES= "inventories";

    //order status strings
    public static final String ORDER_STATUS_DECLINED = "Declined";
    public static final String ORDER_STATUS_PENDING = "Pending";
    public static final String ORDER_STATUS_APPROVED = "Approved";
    public static final String ORDER_STATUS_HOLD = "Hold";
    public static final String ORDER_STATUS_PLACED = "Placed";
    public static final String ORDER_STATUS_DRAFT = "Draft";

    //requisition status strings
    public static final String REQUISITION_STATUS_DECLINED = "Declined";
    public static final String REQUISITION_STATUS_PENDING = "Pending";
    public static final String REQUISITION_STATUS_APPROVED = "Approved";
    public static final String REQUISITION_STATUS_HOLD = "Hold";

    //common strings
    public static final String SAVE_STRING = "Save";
    public static final String UPDATE_STRING = "Update";
    public static final String CANCEL_STRING = "Cancel";

    //initialized lists
    public static ArrayList<Inventory> iInventory = new ArrayList<>();
    public static ArrayList<Supplier> iSupplier = new ArrayList<>();

    //initialized ids
    public static String ORDER_ID = "PO-0";
    public static String REQUISITION_ID = "REQ-00";

    //message list
    public static final String ERROR_MSG = "Error";
    public static final String SUCCESS_MSG = "Success";

    //Identify Class Type
    public static String CLASS_TYPE="REQUISITION";
    public static String REQUISITION_KEY_VALUE="";

}
