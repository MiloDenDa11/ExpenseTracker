package com.example.expensetracker.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ExpenseTracker.db";
    private static final int DATABASE_VERSION = 1;

    // Tên bảng và các cột
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_NOTE = "note";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_TIMESTAMP + " INTEGER, " +
                COLUMN_NOTE + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    // Thêm một giao dịch mới
    public long insertTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_TYPE, transaction.getType());
        values.put(COLUMN_CATEGORY, transaction.getCategory());
        values.put(COLUMN_TIMESTAMP, transaction.getTimestamp());
        values.put(COLUMN_NOTE, transaction.getNote());

        long id = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
        return id;
    }

    // Tính tổng số dư
    public double getTotalBalance() {
        SQLiteDatabase db = this.getReadableDatabase();
        double balance = 0;

        // Tính tổng Income
        Cursor cursorIncome = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 'INCOME'", null);
        if (cursorIncome.moveToFirst()) {
            balance += cursorIncome.getDouble(0);
        }
        cursorIncome.close();

        // Trừ đi tổng Expense
        Cursor cursorExpense = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 'EXPENSE'", null);
        if (cursorExpense.moveToFirst()) {
            balance -= cursorExpense.getDouble(0);
        }
        cursorExpense.close();
        db.close();

        return balance;
    }
    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Lấy toàn bộ giao dịch
        Cursor cursor = db.rawQuery("SELECT * FROM transactions ORDER BY timestamp DESC", null);

        if (cursor.moveToFirst()) {
            do {
                //
                Transaction t = new Transaction(
                        // ID
                        cursor.getDouble(1),   // Amount
                        cursor.getString(2),   // Type (Đảm bảo cột này không null trong DB)
                        cursor.getString(3),   // Category
                        cursor.getLong(4),     // Timestamp
                        cursor.getString(5)    // Note
                );
                list.add(t);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
