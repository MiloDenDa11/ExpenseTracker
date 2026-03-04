package com.example.expensetracker.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.CategoryIconAdapter;

import java.util.ArrayList;
import java.util.List;


public class AddCategoryActivity extends AppCompatActivity {

    // Khai báo các biến UI
    private EditText edtCategoryName;
    private RadioGroup rgCategoryType;
    private RecyclerView rvIcons;
    private Button btnSaveCategory;
    private ImageView ivSelectedIconPreview;
    private int selectedIconResId;
    private CategoryIconAdapter iconAdapter;
    private com.example.expensetracker.model.DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Liên kết với file giao diện XML
        setContentView(R.layout.activity_add_category);

        // 1. Ánh xạ các thành phần từ giao diện
        Toolbar toolbar = findViewById(R.id.toolbarCategory);
        edtCategoryName = findViewById(R.id.edtCategoryName);
        rgCategoryType = findViewById(R.id.rgCategoryType);
        rvIcons = findViewById(R.id.rvIcons);
        List<Integer> myIcons = new ArrayList<>();
        btnSaveCategory = findViewById(R.id.btnSaveCategory);
        ivSelectedIconPreview = findViewById(R.id.ivSelectedIconPreview);
        myIcons.add(R.drawable.icon_menu_game);
        myIcons.add(R.drawable.icon_menu_eat);
        myIcons.add(R.drawable.icon_menu_shopping);
        myIcons.add(R.drawable.icon_menu_pets);
        myIcons.add(R.drawable.icon_menu_sport);
        myIcons.add(R.drawable.icon_menu_calling);
        myIcons.add(R.drawable.icon_menu_home);
        myIcons.add(R.drawable.icon_menu_travel);
        myIcons.add(R.drawable.icon_menu_education);
        myIcons.add(R.drawable.icon_menu_picture);
        myIcons.add(R.drawable.icon_menu_health);

// Mặc định gán icon đầu tiên
        selectedIconResId = myIcons.get(0);
        ivSelectedIconPreview.setImageResource(selectedIconResId);

// Khởi tạo Adapter
        iconAdapter = new CategoryIconAdapter(myIcons, new CategoryIconAdapter.OnIconClickListener() {
            @Override
            public void onIconClick(int iconResId) {
                // Khi người dùng bấm 1 icon ở dưới lưới, ta cập nhật hình ảnh ở phía trên cùng
                selectedIconResId = iconResId;
                ivSelectedIconPreview.setImageResource(iconResId);
            }
        });
        dbHelper = new com.example.expensetracker.model.DatabaseHelper(this);
// Gắn Adapter vào RecyclerView
        rvIcons.setAdapter(iconAdapter);

        // 2. Cài đặt Thanh tiêu đề (Toolbar) và nút Back
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiện nút mũi tên quay lại
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // Xử lý sự kiện khi bấm nút Back trên Toolbar
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        rvIcons.setLayoutManager(new GridLayoutManager(this, 4));

        btnSaveCategory.setOnClickListener(v -> {
            String categoryName = edtCategoryName.getText().toString().trim();
            if (categoryName.isEmpty()) {
                android.widget.Toast.makeText(AddCategoryActivity.this, "Vui lòng nhập tên danh mục!", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            String type = "EXPENSE";
            if (rgCategoryType.getCheckedRadioButtonId() == R.id.rbIncome) {
                type = "INCOME";
            }
            long resultId = dbHelper.insertCategory(categoryName, type, selectedIconResId);

            if (resultId != -1) {
                android.widget.Toast.makeText(this, "Đã lưu danh mục: " + categoryName, android.widget.Toast.LENGTH_SHORT).show();
                finish();
            } else {
                android.widget.Toast.makeText(this, "Lỗi khi lưu danh mục!", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
}