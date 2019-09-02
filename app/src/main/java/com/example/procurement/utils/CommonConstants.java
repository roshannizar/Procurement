package com.example.procurement.utils;

public class CommonConstants {

    public static final int ORDER_STATUS_DECLINED = -1;
    public static final int ORDER_STATUS_PENDING = 0;
    public static final int ORDER_STATUS_APPROVED = 1;
    public static final int ORDER_STATUS_PLACED = 2;
    public static final int ORDER_STATUS_HOLD = 3;

    public static final String VIEW_STATUS_ORDER_EXTRA = "VIEW_STATUS_ORDER_EXTRA";
    public static final String VIEW_STATUS_ORDER_POSITION = "VIEW_STATUS_ORDER_POSITION";

    public static final String TABLE_NAME = "orders";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_DATE = "created_date";
    public static final String COLUMN_ACCEPTED = "delivery_date";
    public static final String COLUMN_SHOP = "shop";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_STATUS + " INTEGER DEFAULT 0, "
                    + COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + COLUMN_ACCEPTED + " DATE, "
                    + COLUMN_SHOP + " INTEGER)";

}
