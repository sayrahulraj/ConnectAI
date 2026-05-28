package com.example.connectai.adapter;

import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectai.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatMessage> chatList;

    public ChatAdapter(List<ChatMessage> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chat = chatList.get(position);
        holder.textMessage.setText(chat.getMessage());
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)holder.textMessage.getLayoutParams();
        if (chat.isUser()) {
            params.gravity = Gravity.END;
            holder.textMessage.setBackgroundColor(Color.parseColor("#DCF8C6"));
        } else {
            params.gravity = Gravity.START;
            holder.textMessage.setBackgroundColor(Color.WHITE);
        }
        holder.textMessage.setMovementMethod(new ScrollingMovementMethod());
        holder.textMessage.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
        }
    }
}
