package com.dtran.testtemplate1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class LiftDataSource {
    private SQLiteDatabase database;
    private MainSQLiteHelper dbHelper;

    private String[] allColumns = { MainSQLiteHelper.LIFT_COLUMN_ID,
            MainSQLiteHelper.LIFT_COLUMN_LIFT,
            MainSQLiteHelper.LIFT_COLUMN_BODY_PART,
            MainSQLiteHelper.LIFT_COLUMN_WEIGHT,
            MainSQLiteHelper.LIFT_COLUMN_REPS,
            MainSQLiteHelper.LIFT_COLUMN_INSERT_DATE,
            MainSQLiteHelper.LIFT_COLUMN_LIFT_DATE,
            MainSQLiteHelper.LIFT_COLUMN_ACTIVE
};

    public LiftDataSource(Context context) {
        dbHelper = new MainSQLiteHelper(context);
    }

    public String getDBBPath()
    {
       return database.getPath();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Lift createLift(String lift, String bodyPart, double weight, int reps, long insertDate,long liftDate, int active) {
        ContentValues values = new ContentValues();
        values.put(MainSQLiteHelper.LIFT_COLUMN_LIFT, lift);
        values.put(MainSQLiteHelper.LIFT_COLUMN_BODY_PART, bodyPart);
        values.put(MainSQLiteHelper.LIFT_COLUMN_WEIGHT, weight);
        values.put(MainSQLiteHelper.LIFT_COLUMN_REPS, reps);
        values.put(MainSQLiteHelper.LIFT_COLUMN_INSERT_DATE, insertDate);
        values.put(MainSQLiteHelper.LIFT_COLUMN_LIFT_DATE, liftDate);
        values.put(MainSQLiteHelper.LIFT_COLUMN_ACTIVE, active);

        long insertId = database.insert(MainSQLiteHelper.TABLE_LIFTS, null, values);
        Cursor cursor = database.query(MainSQLiteHelper.TABLE_LIFTS,
                allColumns, MainSQLiteHelper.LIFT_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Lift newLift = cursorToLift(cursor);
        cursor.close();
        return newLift;
    }

    public void deleteLift(Lift lift) {
        long id = lift.getId();
        System.out.println("Lift deleted with id: " + id);
        database.delete(MainSQLiteHelper.TABLE_LIFTS, MainSQLiteHelper.LIFT_COLUMN_ID
                + " = " + id, null);
    }

    public void deleteLiftById(long id) {
        database.delete(MainSQLiteHelper.TABLE_LIFTS, MainSQLiteHelper.LIFT_COLUMN_ID + " = " + id, null);
    }

    public List<Lift> getAllLifts() {
        List<Lift> lifts = new ArrayList<Lift>();

        Cursor cursor = database.query(MainSQLiteHelper.TABLE_LIFTS, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Lift lift = cursorToLift(cursor);
            lifts.add(lift);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return lifts;
    }

    public void updateLift(long id, String lift, String bodyPart, double weight, int reps) {
        Lift currentLift = getLiftById(id);

        ContentValues args = new ContentValues();
        args.put(MainSQLiteHelper.LIFT_COLUMN_LIFT, lift);
        args.put(MainSQLiteHelper.LIFT_COLUMN_BODY_PART, bodyPart);
        args.put(MainSQLiteHelper.LIFT_COLUMN_WEIGHT, weight);
        args.put(MainSQLiteHelper.LIFT_COLUMN_REPS, reps);
        args.put(MainSQLiteHelper.LIFT_COLUMN_INSERT_DATE, currentLift.getInsertDate());
        args.put(MainSQLiteHelper.LIFT_COLUMN_LIFT_DATE, currentLift.getLiftDate());
        args.put(MainSQLiteHelper.LIFT_COLUMN_ACTIVE, 1);

        database.update(MainSQLiteHelper.TABLE_LIFTS, args, "_id=" + id, null);
        String query = "UPDATE lift_history SET lift='"+ lift
                + "',body_part='" + bodyPart
                + "',weight='" + weight
                + "',reps='" + reps
                +"' WHERE _id='" + id+"'";
        database.rawQuery(query, null);

/*
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("phone", phone);
            contentValues.put("email", email);
            contentValues.put("street", street);
            contentValues.put("place", place);
            db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
      */
    }

    public Lift getLiftById(long id) {
        Lift lift = new Lift();

        //Cursor cursor = database.query(MainSQLiteHelper.TABLE_LIFTS, allColumns, null, null, null, null, null);
        String query = "SELECT * FROM lift_history WHERE _id = " + id;
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            lift = cursorToLift(cursor);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return lift;
    }

    public List<Lift> getLifts(long date) {
        List<Lift> lifts = new ArrayList<Lift>();
        //query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, CancellationSignal cancellationSignal)
        //query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
        //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        String query = "SELECT * FROM lift_history WHERE lift_date = " + date + " ORDER BY insert_date asc";

        //Cursor cursor = database.query(MainSQLiteHelper.TABLE_LIFTS, allColumns, null, null, null, null, null);
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Lift lift = cursorToLift(cursor);
            lifts.add(lift);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return lifts;
    }

    public List<Lift> checkIfAnyLifts(long startDate, long endDate){
        List<Lift> lifts = new ArrayList<Lift>();

        //Cursor cursor = database.query(MainSQLiteHelper.TABLE_LIFTS, null,null,null,null,null,"create_date ASC", null);
        String query = "SELECT * FROM lift_history WHERE insert_date between " + startDate + " AND " + endDate +" ORDER BY insert_date asc";
        //Stri[] args = new Long[] { startDate, endDate };
        Cursor cursor = database.rawQuery(query, null);

        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Lift lift = cursorToLift(cursor);
                lifts.add(lift);
                cursor.moveToNext();
            }
        }
        return lifts;
    }

    private Lift cursorToLift(Cursor cursor) {
        Lift lift = new Lift();
        lift.setId(cursor.getLong(0));
        lift.setLift(cursor.getString(1));
        lift.setBodyPart(cursor.getString(2));
        lift.setWeight(cursor.getDouble(3));
        lift.setReps(cursor.getInt(4));
        lift.setInsertDate(cursor.getLong(5));
        lift.setLiftDate(cursor.getLong(6));
        lift.setActive(cursor.getInt(7));
        return lift;
    }
}
