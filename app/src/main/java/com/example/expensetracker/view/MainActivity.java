package com.example.expensetracker.view;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.TransactionAdapter;
import com.example.expensetracker.controller.FinanceController;
import com.example.expensetracker.model.DatabaseHelper;
import com.example.expensetracker.model.Transaction;
import com.example.expensetracker.src.HistoryActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvTransactions;
    private TransactionAdapter adapter;
    private DatabaseHelper dbHelper;
    private TextView tvBalance;
    private Button btnAddTransaction;
    private FinanceController financeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                androidx.core.app.ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
        dbHelper = new DatabaseHelper(this);
        rvTransactions = findViewById(R.id.rvTransactions);
        rvTransactions.setLayoutManager(new LinearLayoutManager(this));

        Button btnAddCategory = findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(viewCt -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, AddCategoryActivity.class);
            startActivity(intent);
        });

//        Button btnChatbot = findViewById(R.id.btnChatbot);
//        btnChatbot.setOnClickListener(view -> {
//            android.content.Intent intent = new android.content.Intent(MainActivity.this, com.example.expensetracker.view.ChatbotActivity.class);
//            startActivity(intent);
//        });
        //  Controller
        financeController = new FinanceController(this);

        // Ánh xạ View
        tvBalance = findViewById(R.id.tvBalance);
        btnAddTransaction = findViewById(R.id.btnAddTransaction);

        // Update
        updateBalanceDisplay();
// Lấy các view cần tạo hiệu ứng
        android.view.View cardBalance = findViewById(R.id.cardBalance);
        android.view.View pieChart = findViewById(R.id.pieChart);

        // Đặt vị trí ban đầu là bị đẩy xuống dưới và trong suốt
        cardBalance.setTranslationY(100f);
        cardBalance.setAlpha(0f);
        pieChart.setTranslationY(100f);
        pieChart.setAlpha(0f);
        btnAddTransaction.setTranslationY(100f);
        btnAddTransaction.setAlpha(0f);

        // Animation
        cardBalance.animate().translationY(0f).alpha(1f).setDuration(500).setStartDelay(100).start();
        pieChart.animate().translationY(0f).alpha(1f).setDuration(500).setStartDelay(200).start();
        btnAddTransaction.animate().translationY(0f).alpha(1f).setDuration(500).setStartDelay(300).start();

        btnAddTransaction.setOnClickListener(v -> {
            btnAddTransaction.setOnClickListener(vAdd -> {
                Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
                startActivity(intent);
            });
        });
        Button btnViewHistory = findViewById(R.id.btnViewHistory);
        btnViewHistory.setOnClickListener(v -> {

            android.content.Intent intent = new android.content.Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    private void handleMoMoPayment() {
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateBalanceDisplay();
        setupPieChart();
        displayTransactionHistory();
    }
    private void displayTransactionHistory() {
        List<Transaction> list = dbHelper.getAllTransactions();

        if (list != null) {
            adapter = new TransactionAdapter(list);
            rvTransactions.setAdapter(adapter);

            rvTransactions.scheduleLayoutAnimation();
        }
    }
    private void setupPieChart() {
        PieChart pieChart = findViewById(R.id.pieChart);

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(500000f, "Ăn uống"));
        entries.add(new PieEntry(200000f, "Đi lại"));
        entries.add(new PieEntry(340000f, "Mua sắm"));

        if (entries.isEmpty()) {
            pieChart.setNoDataText("Chưa có khoản chi tiêu nào để hiển thị.");
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(android.graphics.Color.WHITE);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        pieChart.setCenterText("Chi tiêu");
        pieChart.setCenterTextSize(16f);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);

        pieChart.invalidate();
    }

    private void updateBalanceDisplay() {
        double currentBalance = financeController.getCurrentBalance();
        tvBalance.setText(currentBalance + " VNĐ");
    }
    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("FINANCE_CH", "Biến động số dư", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private void sendNotification(String type, double amount) {
        String message = (type.equals("INCOME") ? "Vừa nạp: +" : "Vừa chi: -") + amount + " VNĐ";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "FINANCE_CH")
                .setSmallIcon(R.drawable.ic_wallet)
                .setContentTitle("Biến động số dư")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }

}