package com.example.expensetracker.controller;

import android.content.Context;
import com.example.expensetracker.model.DatabaseHelper;
import com.example.expensetracker.model.Transaction;
import com.example.expensetracker.utils.NotificationUtil; // Class đã đề cập ở phần trước

import java.lang.reflect.Array;

public class FinanceController {
    private DatabaseHelper dbHelper;
    private Context context;

    public FinanceController(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    public boolean processNewTransaction(double amount, String type, String category, String note) {

        Transaction newTrans = new Transaction(amount, type, category, System.currentTimeMillis(), note);

        long resultId = dbHelper.insertTransaction(newTrans);

        if (resultId != -1) {
            double currentBalance = dbHelper.getTotalBalance();


            String title = "INCOME".equals(type) ? "Nạp quỹ thành công!" : "Chi tiêu mới ghi nhận!";
            String message = "Số dư quỹ hiện tại: " + currentBalance + " VNĐ";
            NotificationUtil.showBalanceChangeNotification(context, title, message);

            return true;
        }
        return false;
    }
    public double getCurrentBalance() {
        return dbHelper.getTotalBalance();
    }
}
