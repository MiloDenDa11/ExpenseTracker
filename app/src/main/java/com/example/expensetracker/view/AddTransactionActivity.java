package com.example.expensetracker.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.controller.FinanceController;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText etAmount, etCategory, etNote;
    private RadioGroup rgType;
    private Button btnSave;
    private FinanceController financeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        // Controller
        financeController = new FinanceController(this);
        Button btnPayMoMo = findViewById(R.id.btnPayMoMo);
        btnPayMoMo.setOnClickListener(v -> handleMoMoPayment());

        // View
        etAmount = findViewById(R.id.etAmount);
        etCategory = findViewById(R.id.etCategory);
        etNote = findViewById(R.id.etNote);
        rgType = findViewById(R.id.rgType);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveTransaction());
    }

    private void saveTransaction() {
        String amountStr = etAmount.getText().toString();
        String category = etCategory.getText().toString();
        String note = etNote.getText().toString();

        if (amountStr.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tiền và danh mục!", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        // Check
        String type = (rgType.getCheckedRadioButtonId() == R.id.rbIncome) ? "INCOME" : "EXPENSE";

        // Back to MainActivity
        boolean success = financeController.processNewTransaction(amount, type, category, note);

        if (success) {
            double currentBalance = financeController.getCurrentBalance();

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm - dd/MM/yyyy", java.util.Locale.getDefault());
            String time = sdf.format(new java.util.Date());

            String title = type.equals("INCOME") ? "💰 Biến động: Nạp quỹ" : "💸 Biến động: Chi tiêu";
            String sign = type.equals("INCOME") ? "+" : "-";

            String message = "Giao dịch: " + sign + amount + " VNĐ (" + category + ")\n" +
                    "Thời gian: " + time + "\n" +
                    "Số dư cuối: " + currentBalance + " VNĐ";

            com.example.expensetracker.utils.NotificationUtil.showNotification(this, title, message);

            Toast.makeText(this, "Đã lưu thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi lưu dữ liệu.", Toast.LENGTH_SHORT).show();
        }
    }
    private void openMoMoToDeposit(String targetPhone, double amount, String note) {
        try {
           // Ép kiểu
            String amountStr = String.valueOf((int) amount);

            // Deeplink
            String url = "momo://?action=transfer&phone=" + targetPhone + "&amount=" + amountStr + "&msg=" + Uri.encode(note);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            // Momo
            Toast.makeText(this, "Máy bạn chưa cài đặt ứng dụng MoMo!", Toast.LENGTH_LONG).show();
        }
    }
    private void handleMoMoPayment() {
        String amountStr = etAmount.getText().toString();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tiền trước!", Toast.LENGTH_SHORT).show();
            return;
        }

        int amount = (int) Double.parseDouble(amountStr);
        String phoneNumber = "09xxxxxxxx";
        String note = "Nap tien vao quy MiloMini: " + etCategory.getText().toString();

        Uri uri = Uri.parse("momo://?action=transfer&phone=" + phoneNumber +
                "&amount=" + amount +
                "&msg=" + Uri.encode(note));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "Bạn chưa cài ứng dụng MoMo hoặc lỗi liên kết!", Toast.LENGTH_LONG).show();
        }
    }
}