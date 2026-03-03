package com.example.expensetracker.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.controller.FinanceController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatbotActivity extends AppCompatActivity {

    private TextView tvChatHistory;
    private EditText etQuestion;
    private Button btnSend;
    private FinanceController financeController;
    //API
    private static final String API_KEY = "AIzaSyDmTcT-xrp6TgOuO4ef5WPE-X7HvhczdHM";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent?key=" + API_KEY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        tvChatHistory = findViewById(R.id.tvChatHistory);
        etQuestion = findViewById(R.id.etQuestion);
        btnSend = findViewById(R.id.btnSend);
        financeController = new FinanceController(this);
        android.view.View cardChat = findViewById(R.id.cardChat);
        android.view.View layoutInput = findViewById(R.id.layoutInput);
        cardChat.setAlpha(0f);
        cardChat.setTranslationY(100f);
        layoutInput.setAlpha(0f);
        layoutInput.setTranslationY(100f);

        cardChat.animate().alpha(1f).translationY(0f).setDuration(500).setStartDelay(100).start();
        layoutInput.animate().alpha(1f).translationY(0f).setDuration(500).setStartDelay(200).start();

        btnSend.setOnClickListener(v -> {
            String question = etQuestion.getText().toString();
            if (!question.isEmpty()) {
                appendMessage("MiloMini: " + question);
                etQuestion.setText("");
                callGeminiRAG(question);
            }
        });
    }

    private void callGeminiRAG(String userQuestion) {
        appendMessage("AI: Đang phân tích dữ liệu chi tiêu...");

        // 1 Lấy context
        double currentBalance = financeController.getCurrentBalance();
        String contextData = "Tên người dùng: MiloMini. Tổng số dư quỹ hiện tại là: " + currentBalance + " VNĐ. ";


        // 2 Ghép context
        String finalPrompt = "Dữ liệu thực tế: " + contextData + "\n\n" +
                "Câu hỏi của người dùng: " + userQuestion + "\n\n" +
                "Yêu cầu: Hãy đóng vai một trợ lý tài chính thân thiện, trả lời ngắn gọn, chính xác dựa trên dữ liệu thực tế trên.";


        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                OkHttpClient client = new OkHttpClient();

                // Build Json
                JSONObject jsonBody = new JSONObject();
                JSONArray contents = new JSONArray();
                JSONObject contentObj = new JSONObject();
                JSONArray parts = new JSONArray();
                JSONObject textObj = new JSONObject();

                textObj.put("text", finalPrompt);
                parts.put(textObj);
                contentObj.put("parts", parts);
                contents.put(contentObj);
                jsonBody.put("contents", contents);

                RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonBody.toString());
                Request request = new Request.Builder()
                        .url(API_URL)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();

                    // Json trả lời
                    JSONObject jsonObject = new JSONObject(responseData);
                    String aiAnswer = jsonObject.getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text");

                    // Update UI
                    runOnUiThread(() -> {
                        String currentChat = tvChatHistory.getText().toString();
                        tvChatHistory.setText(currentChat.replace("AI: Đang phân tích dữ liệu chi tiêu...\n\n", ""));
                        appendMessage("AI: " + aiAnswer);
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> appendMessage("AI: Lỗi kết nối. Vui lòng kiểm tra lại mạng hoặc API Key!"));
            }
        });
    }

    private void appendMessage(String message) {
        String current = tvChatHistory.getText().toString();
        tvChatHistory.setText(current + message + "\n\n");
    }
}