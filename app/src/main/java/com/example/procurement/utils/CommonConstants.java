package com.example.procurement.utils;

import static com.example.procurement.activities.SignInActivity.uid;

public class CommonConstants {

    public static final String USER_DB_URL = "employees/siteManagers/" + uid;

    public static final String FIREBASE_ORDER_DB = "orders";
    public static final String FIREBASE_NOTES_DB = "notes";
    public static final String FIREBASE_ENQUIRIES_DB = "enquiries";
    public static final String FIREBASE_NOTIFICATION_DB = "notifications";

    public static final String ORDER_STATUS_DECLINED = "Declined";
    public static final String ORDER_STATUS_PENDING = "Pending";
    public static final String ORDER_STATUS_APPROVED = "Approved";
    public static final String ORDER_STATUS_PLACED = "Placed";
    public static final String ORDER_STATUS_HOLD = "Hold";

    public static final String NOTE_FRAGMENT_TAG = "Note Fragment";
    public static final String ENQUIRE_FRAGMENT_TAG = "Enquire Fragment";
    public static final String ORDER_VIEW_FRAGMENT_TAG = "OrderView Fragment";
    public static final String SAVE_STRING = "Save";
    public static final String UPDATE_STRING = "Update";
    public static final String CANCEL_STRING = "Cancel";

    public static String ORDER_ID = null;

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
