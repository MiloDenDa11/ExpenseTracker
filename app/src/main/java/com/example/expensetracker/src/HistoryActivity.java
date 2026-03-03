package com.example.expensetracker.src;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.TransactionAdapter;
import com.example.expensetracker.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarHistory);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> finish()); // Quay lại khi bấm nút Back
        }

        // RecyclerView và Adapter
        RecyclerView rvTransactions = findViewById(R.id.rvTransactions);
        rvTransactions.setLayoutManager(new LinearLayoutManager(this));

        // 3. Tạo dữ liệu giả lập
        List<Transaction> mockData = new ArrayList<>();
        mockData.add(new Transaction(1, 100000, "INCOME", "Nạp tiền từ MoMo", System.currentTimeMillis() - 10000, "Nạp quỹ"));
        mockData.add(new Transaction(2, 50000, "EXPENSE", "Ăn sáng", System.currentTimeMillis() - 200000, "Uống cafe"));
        mockData.add(new Transaction(3, 2000000, "INCOME", "Lương tháng 03", System.currentTimeMillis() - 1000000, "Lương"));
        mockData.add(new Transaction(mockData.size(), 80000, "EXPENSE", "Hóa đơn điện", System.currentTimeMillis() - 5000000, "Điện lực"));
        mockData.add(new Transaction(mockData.size(), mockData.size() * 10000, "EXPENSE", "Mua sắm", System.currentTimeMillis() - 200000, "Uống cafe"));

        TransactionAdapter adapter = new TransactionAdapter(mockData);
        rvTransactions.setAdapter(adapter);
        rvTransactions.scheduleLayoutAnimation();
    }
}