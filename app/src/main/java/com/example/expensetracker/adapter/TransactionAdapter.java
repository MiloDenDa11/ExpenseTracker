package com.example.expensetracker.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction t = transactionList.get(position);

        // Đổ dữ liệu vào view
        holder.tvCategory.setText(t.getCategory());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, HH:mm", Locale.getDefault());
        holder.tvDate.setText(sdf.format(new Date(t.getTimestamp())));

        if ("INCOME".equals(t.getType())) {
            holder.tvAmount.setText("+" + t.getAmount());
            holder.tvAmount.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
        } else {
            holder.tvAmount.setText("-" + t.getAmount());
            holder.tvAmount.setTextColor(android.graphics.Color.parseColor("#F44336"));
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvCategory, tvDate, tvAmount;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivCategoryIcon);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}