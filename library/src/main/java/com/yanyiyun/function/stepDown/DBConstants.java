package com.yanyiyun.function.stepDown;

public class DBConstants {

    public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String DATABASE_NAME = "StepDB.db";
    public static final int VERSION = 1;
    public static final String TABLE_NAME = "TodayStepData";
    public static final String PRIMARY_KEY = "_id";
    public static final String TODAY = "today";
    public static final String DATE = "date";
    public static final String STEP = "step";
    public static final String LAST_SENOR_STEP="last_senor_step";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TODAY + " TEXT, "
            + DATE + " long, "
            + STEP + " long);";

    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String SQL_QUERY_ALL = "SELECT * FROM " + TABLE_NAME;
    public static final String SQL_QUERY_STEP = "SELECT * FROM " + TABLE_NAME + " WHERE " + TODAY
            +" = ? AND " + STEP + " = ?";
    public static final String SQL_QUERY_STEP_BY_DATE = "SELECT * FROM " + TABLE_NAME + " WHERE "
            + TODAY + " = ?";
    public static final String SQL_DELETE_TODAY = "DELETE FROM " + TABLE_NAME + " WHERE " + TODAY
            +" = ?";
    public static final String SQL_QUERY_STEP_ORDER_BY = "SELECT * FROM " + TABLE_NAME + " WHERE "
            + TODAY + " = ? ORDER BY " + STEP + " DESC";

    public static final int ERROR=0;
}
