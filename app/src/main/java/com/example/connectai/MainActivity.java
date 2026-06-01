package com.example.connectai;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectai.adapter.ChatAdapter;
import com.example.connectai.adapter.ChatMessage;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final OkHttpClient client = new OkHttpClient();
    private RecyclerView recyclerView;
    private EditText editMessage;
    private Button buttonSend;
    private ChatAdapter adapter;
    private List<ChatMessage> chatList;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ConnectAI");
        recyclerView = findViewById(R.id.recyclerViewChat);
        editMessage = findViewById(R.id.editMessage);
        buttonSend = findViewById(R.id.buttonSend);
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        buttonSend.setOnClickListener(v -> {
            String message = editMessage.getText().toString().trim();
            if (message.isEmpty()) return;
            chatList.add(new ChatMessage(message, true));
            adapter.notifyItemInserted(chatList.size() - 1);
            recyclerView.post(() -> recyclerView.scrollToPosition(chatList.size() - 1));
            editMessage.setText("");
            callAI(message);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void callAI(String userMessage) {
        chatList.add(new ChatMessage("Typing...", false));
        adapter.notifyItemInserted(chatList.size() - 1);
        recyclerView.post(() -> recyclerView.scrollToPosition(chatList.size() - 1));

        new Thread(() -> {
            try {
                JSONObject mainObject = new JSONObject();
                mainObject.put("model", "openai/gpt-3.5-turbo");
                JSONArray messages = new JSONArray();
                JSONObject userObj = new JSONObject();
                userObj.put("role", "user");
                userObj.put("content", userMessage);
                messages.put(userObj);
                mainObject.put("messages", messages);
                RequestBody body = RequestBody.create(mainObject.toString(), MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url("https://openrouter.ai/api/v1/chat/completions")
                        .post(body)
                        .addHeader("Authorization", "Bearer " + BuildConfig.API_KEY)
                        .addHeader("Content-Type", "application/json")
                        .build();
                Response response = client.newCall(request).execute();
                String responseText = response.body().string();
                JSONObject jsonObject = new JSONObject(responseText);
                JSONArray choices = jsonObject.getJSONArray("choices");
                String aiReply = choices.getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
                runOnUiThread(() -> {
                    chatList.remove(chatList.size() - 1);
                    chatList.add(new ChatMessage(aiReply, false));
                    adapter.notifyDataSetChanged();
                    recyclerView.post(() -> recyclerView.scrollToPosition(chatList.size() - 1));
                });
            } catch (Exception exception) {
                runOnUiThread(() -> {
                    chatList.remove(chatList.size() - 1);
                    chatList.add(new ChatMessage("Error: " + exception.getMessage(), false));
                    adapter.notifyDataSetChanged();
                    recyclerView.post(() -> recyclerView.scrollToPosition(chatList.size() - 1));
                });
            }
        }).start();
    }
}