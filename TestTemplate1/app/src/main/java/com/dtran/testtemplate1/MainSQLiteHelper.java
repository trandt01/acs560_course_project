package com.dtran.testtemplate1;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MainSQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_LIFTS = "lift_history";
    public static final String LIFT_COLUMN_ID = "_id";
    public static final String LIFT_COLUMN_LIFT = "lift";
    public static final String LIFT_COLUMN_BODY_PART = "body_part";
    public static final String LIFT_COLUMN_WEIGHT = "weight";
    public static final String LIFT_COLUMN_REPS = "reps";
    public static final String LIFT_COLUMN_INSERT_DATE = "insert_date";
    public static final String LIFT_COLUMN_LIFT_DATE = "lift_date";
    public static final String LIFT_COLUMN_ACTIVE = "active";

    public static final String TABLE_ACCOUNT = "account";
    public static final String ACCOUNT_COLUMN_ID = "_id";
    public static final String ACCOUNT_COLUMN_WEIGHT = "weight";
    public static final String ACCOUNT_COLUMN_HEIGHT = "height";
    public static final String ACCOUNT_COLUMN_BODY_FAT = "body_fat";
    public static final String ACCOUNT_COLUMN_CREATE_DATE = "create_date";
    public static final String ACCOUNT_COLUMN_UPDATE_DATE = "update_date";

    public static final String TABLE_SYNC = "sync_history";
    public static final String SYNC_COLUMN_ID = "_id";
    public static final String SYNC_COLUMN_DATE = "date";

    private static final String DATABASE_NAME = "workoutTracker.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE_TABLE_LIFT = "create table "
            + TABLE_LIFTS + "(" + LIFT_COLUMN_ID + " integer primary key autoincrement, "
            + LIFT_COLUMN_LIFT + " text not null,"
            + LIFT_COLUMN_BODY_PART + " text not null,"
            + LIFT_COLUMN_WEIGHT + " real not null,"
            + LIFT_COLUMN_REPS + " integer not null,"
            + LIFT_COLUMN_INSERT_DATE + " integer not null,"
            + LIFT_COLUMN_LIFT_DATE + " integer not null,"
            + LIFT_COLUMN_ACTIVE + " integer not null"
            +  " );";

    private static final String DATABASE_CREATE_ACCOUNT = "create table "
            + TABLE_ACCOUNT + "(" + ACCOUNT_COLUMN_ID + " integer primary key autoincrement, "
            + ACCOUNT_COLUMN_WEIGHT + " real not null,"
            + ACCOUNT_COLUMN_HEIGHT + " real not null,"
            + ACCOUNT_COLUMN_BODY_FAT + " real not null,"
            + ACCOUNT_COLUMN_CREATE_DATE + " text not null,"
            + ACCOUNT_COLUMN_UPDATE_DATE + " text not null"
            + ");";

    private static final String DATABASE_CREATE_SYNC = "create table "
            + TABLE_SYNC + "(" + SYNC_COLUMN_ID + " integer primary key autoincrement, "
            + SYNC_COLUMN_DATE + " integer not null"
            + ");";

    public MainSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_TABLE_LIFT);
        database.execSQL(DATABASE_CREATE_ACCOUNT);
        database.execSQL(DATABASE_CREATE_SYNC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MainSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIFTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNC);
        onCreate(db);
    }
}
