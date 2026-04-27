package com.example.gomechanics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ChatBotActivity extends AppCompatActivity {

    private RecyclerView rvChat;
    private EditText etMessage;
    private ImageButton btnSend, btnBack;

    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> chatMessages;

    private OkHttpClient client;

    private static final String OPENAI_API_KEY = "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";
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final String appKnowledge =
            "You are GoMechanics AI Assistant. " +
                    "GoMechanics is a mechanic booking Android app. " +
                    "The app allows users to register, login, view mechanics, hire a mechanic, create a booking, " +
                    "view previous bookings, and allows mechanics to login, view new orders, accept orders, " +
                    "and manage mechanic service requests. " +
                    "Answer like a friendly customer support agent. " +
                    "Keep answers short, clear, and easy for normal users. " +
                    "If the user asks how to book a mechanic, explain: login as user, open dashboard, tap Hire Mechanic, " +
                    "select a mechanic, fill vehicle details, issue, budget, contact and address, then submit booking. " +
                    "If the user asks how a mechanic checks orders, explain: login as mechanic, open dashboard, " +
                    "tap new orders, view order details, then accept/commit the booking. " +
                    "If the question is outside the app, still answer helpfully but keep it simple.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        rvChat = findViewById(R.id.rvChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);

        client = new OkHttpClient();

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);

        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(chatAdapter);

        addBotMessage("Hello! I am GoMechanics AI Assistant. How can I help you today?");

        btnBack.setOnClickListener(v -> finish());

        btnSend.setOnClickListener(v -> {
            String userMessage = etMessage.getText().toString().trim();

            if (TextUtils.isEmpty(userMessage)) {
                Toast.makeText(ChatBotActivity.this, "Please type a message", Toast.LENGTH_SHORT).show();
                return;
            }

            etMessage.setText("");
            addUserMessage(userMessage);
            askGPT(userMessage);
        });
    }

    private void addUserMessage(String message) {
        chatMessages.add(new ChatMessage(message, ChatMessage.TYPE_USER));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        rvChat.scrollToPosition(chatMessages.size() - 1);
    }

    private void addBotMessage(String message) {
        chatMessages.add(new ChatMessage(message, ChatMessage.TYPE_BOT));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        rvChat.scrollToPosition(chatMessages.size() - 1);
    }

    private void askGPT(String userQuestion) {
        addBotMessage("Typing...");

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("model", "gpt-4o-mini");

            JSONArray messagesArray = new JSONArray();

            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", appKnowledge);
            messagesArray.put(systemMessage);

            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", userQuestion);
            messagesArray.put(userMessage);

            requestJson.put("messages", messagesArray);
            requestJson.put("temperature", 0.4);

            RequestBody body = RequestBody.create(requestJson.toString(), JSON);

            Request request = new Request.Builder()
                    .url(OPENAI_URL)
                    .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        removeTypingMessage();
                        addBotMessage("Sorry, I could not connect right now. Please check your internet and try again.");
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseBody responseBody = response.body();
                    String responseString = responseBody != null ? responseBody.string() : "";

                    runOnUiThread(() -> {
                        removeTypingMessage();

                        if (!response.isSuccessful()) {
                            addBotMessage("Sorry, AI service is not responding correctly. Please try again later.");
                            return;
                        }

                        String botReply = parseGPTResponse(responseString);
                        addBotMessage(botReply);
                    });
                }
            });

        } catch (Exception e) {
            removeTypingMessage();
            addBotMessage("Something went wrong. Please try again.");
        }
    }

    private String parseGPTResponse(String responseString) {
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray choices = jsonObject.getJSONArray("choices");

            if (choices.length() > 0) {
                JSONObject firstChoice = choices.getJSONObject(0);
                JSONObject message = firstChoice.getJSONObject("message");
                return message.getString("content").trim();
            }

            return "I could not understand the response. Please try again.";

        } catch (Exception e) {
            return "Sorry, I could not read the AI response.";
        }
    }

    private void removeTypingMessage() {
        if (chatMessages.size() == 0) {
            return;
        }

        int lastIndex = chatMessages.size() - 1;
        ChatMessage lastMessage = chatMessages.get(lastIndex);

        if (lastMessage.getType() == ChatMessage.TYPE_BOT
                && lastMessage.getMessage().equals("Typing...")) {
            chatMessages.remove(lastIndex);
            chatAdapter.notifyItemRemoved(lastIndex);
        }
    }
}