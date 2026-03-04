package com.example.expensetracker.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;

import java.util.List;

public class CategoryIconAdapter extends RecyclerView.Adapter<CategoryIconAdapter.IconViewHolder> {

    private List<Integer> iconList; // Chứa danh sách các ID của hình ảnh (R.drawable.xxx)
    private int selectedPosition = 0; // Mặc định chọn icon đầu tiên
    private OnIconClickListener listener;

    // Interface để gửi dữ liệu icon được chọn về lại Activity
    public interface OnIconClickListener {
        void onIconClick(int iconResId);
    }

    public CategoryIconAdapter(List<Integer> iconList, OnIconClickListener listener) {
        this.iconList = iconList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_icon, parent, false);
        return new IconViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
        int iconResId = iconList.get(position);
        holder.ivIcon.setImageResource(iconResId);

        // HIỆU ỨNG ĐỔI MÀU: Nếu item này đang được chọn
        if (position == selectedPosition) {
            holder.ivIcon.setBackgroundResource(R.drawable.circle_background_green); // Viền xanh
            holder.ivIcon.setColorFilter(Color.WHITE); // Đổi màu ruột icon thành trắng
        } else {
            // Trạng thái bình thường
            holder.ivIcon.setBackgroundResource(R.drawable.circle_background_gray); // Viền xám
            holder.ivIcon.setColorFilter(Color.parseColor("#888888")); // Đổi màu ruột icon thành xám
        }

        // Bắt sự kiện người dùng bấm vào Icon
        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition(); // Cập nhật vị trí mới

            // Chỉ load lại (đổi màu) 2 cái: cái vừa bỏ chọn và cái mới được chọn cho mượt
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);

            // Gửi dữ liệu icon ra ngoài Activity
            if (listener != null) {
                listener.onIconClick(iconResId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return iconList != null ? iconList.size() : 0;
    }

    static class IconViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;

        IconViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }
}