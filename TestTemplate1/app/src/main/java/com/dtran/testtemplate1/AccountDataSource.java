package com.dtran.testtemplate1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class AccountDataSource {
    private SQLiteDatabase database;
    private MainSQLiteHelper dbHelper;

    private String[] allColumns = { MainSQLiteHelper.ACCOUNT_COLUMN_ID,
            MainSQLiteHelper.ACCOUNT_COLUMN_WEIGHT,
            MainSQLiteHelper.ACCOUNT_COLUMN_HEIGHT,
            MainSQLiteHelper.ACCOUNT_COLUMN_BODY_FAT,
            MainSQLiteHelper.ACCOUNT_COLUMN_CREATE_DATE,
            MainSQLiteHelper.ACCOUNT_COLUMN_UPDATE_DATE
    };

    public AccountDataSource(Context context) {
        dbHelper = new MainSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Account createAccount(double weight, double height, double body_fat, String create_date, String update_date) {
        ContentValues values = new ContentValues();
        values.put(MainSQLiteHelper.ACCOUNT_COLUMN_WEIGHT, weight);
        values.put(MainSQLiteHelper.ACCOUNT_COLUMN_HEIGHT, height);
        values.put(MainSQLiteHelper.ACCOUNT_COLUMN_BODY_FAT, body_fat);
        values.put(MainSQLiteHelper.ACCOUNT_COLUMN_CREATE_DATE, create_date);
        values.put(MainSQLiteHelper.ACCOUNT_COLUMN_UPDATE_DATE, update_date);

        long insertId = database.insert(MainSQLiteHelper.TABLE_ACCOUNT, null, values);
        Cursor cursor = database.query(MainSQLiteHelper.TABLE_ACCOUNT,
                allColumns, MainSQLiteHelper.ACCOUNT_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Account newAccount = cursorToAccount(cursor);
        cursor.close();
        return newAccount;
    }

    public void deleteAccount(Account account) {
        long id = account.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MainSQLiteHelper.TABLE_ACCOUNT, MainSQLiteHelper.ACCOUNT_COLUMN_ID
                + " = " + id, null);
    }
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<Account>();

        Cursor cursor = database.query(MainSQLiteHelper.TABLE_ACCOUNT,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Account account = cursorToAccount(cursor);
            accounts.add(account);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return accounts;
    }

    public Account getLatestAccount()
    {
        Account newestAccount = new Account();
        String query = "SELECT TOP 1 FROM account ORDER BY by create_date desc";
        //Cursor cursor = database.rawQuery(query, null);
        Cursor cursor = database.query(MainSQLiteHelper.TABLE_ACCOUNT, null,null,null,null,null,"create_date DESC", "1");

        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            newestAccount = cursorToAccount(cursor);
            cursor.close();
        }
        return newestAccount;
    }

    private Account cursorToAccount(Cursor cursor) {
        Account account = new Account();
        account.setId(cursor.getLong(0));
        account.setWeight(cursor.getDouble(1));
        account.setHeight(cursor.getDouble(2));
        account.setBodyFat(cursor.getDouble(3));
        account.setCreateDate(cursor.getString(4));
        account.setUpdateDate(cursor.getString(5));
        return account;
    }
}
