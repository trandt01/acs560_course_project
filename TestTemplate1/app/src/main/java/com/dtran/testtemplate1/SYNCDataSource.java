package com.dtran.testtemplate1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class SYNCDataSource {
    private SQLiteDatabase database;
    private MainSQLiteHelper dbHelper;

    private String[] allColumns = { MainSQLiteHelper.SYNC_COLUMN_ID,
            MainSQLiteHelper.SYNC_COLUMN_DATE
    };

    public SYNCDataSource(Context context) {
        dbHelper = new MainSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public SyncObject createSync(long insertDate) {
        ContentValues values = new ContentValues();
        values.put(MainSQLiteHelper.SYNC_COLUMN_DATE, insertDate);

        long insertId = database.insert(MainSQLiteHelper.TABLE_SYNC, null, values);
        Cursor cursor = database.query(MainSQLiteHelper.TABLE_SYNC,
                allColumns, MainSQLiteHelper.SYNC_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        SyncObject newSync = cursorToSync(cursor);
        cursor.close();
        return newSync;
    }

    public void deleteSync(SyncObject sync) {
        long id = sync.getId();
        System.out.println("Sync deleted with id: " + id);
        database.delete(MainSQLiteHelper.TABLE_SYNC, MainSQLiteHelper.SYNC_COLUMN_ID
                + " = " + id, null);
    }
    public List<SyncObject> getAllSync() {
        List<SyncObject> syncList = new ArrayList<SyncObject>();

        Cursor cursor = database.query(MainSQLiteHelper.TABLE_SYNC,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SyncObject sync = cursorToSync(cursor);
            syncList.add(sync);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return syncList;
    }

    private SyncObject cursorToSync(Cursor cursor) {
        SyncObject sync = new SyncObject();
        sync.setId(cursor.getLong(0));
        sync.setDate(cursor.getLong(1));
        return sync;
    }
}
