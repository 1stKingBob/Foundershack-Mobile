package com.scouthub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.scouthub.R;
import com.scouthub.models.Message;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    
    private List<Message> messagesList;

    public MessageAdapter(List<Message> messagesList) {
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messagesList.get(position);
        
        if (message.isSent()) {
            // Show sent message
            holder.sentMessageContainer.setVisibility(View.VISIBLE);
            holder.receivedMessageContainer.setVisibility(View.GONE);
            holder.sentMessageText.setText(message.getText());
            holder.sentMessageTime.setText(message.getTimestamp());
        } else {
            // Show received message
            holder.sentMessageContainer.setVisibility(View.GONE);
            holder.receivedMessageContainer.setVisibility(View.VISIBLE);
            holder.receivedMessageText.setText(message.getText());
            holder.receivedMessageTime.setText(message.getTimestamp());
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public void addMessage(Message message) {
        messagesList.add(message);
        notifyItemInserted(messagesList.size() - 1);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout sentMessageContainer, receivedMessageContainer;
        TextView sentMessageText, sentMessageTime;
        TextView receivedMessageText, receivedMessageTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sentMessageContainer = itemView.findViewById(R.id.sent_message_container);
            receivedMessageContainer = itemView.findViewById(R.id.received_message_container);
            sentMessageText = itemView.findViewById(R.id.sent_message_text);
            sentMessageTime = itemView.findViewById(R.id.sent_message_time);
            receivedMessageText = itemView.findViewById(R.id.received_message_text);
            receivedMessageTime = itemView.findViewById(R.id.received_message_time);
        }
    }
}
