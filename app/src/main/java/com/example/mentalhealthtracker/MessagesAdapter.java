package com.example.mentalhealthtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.senderlayout,parent,false);
            return new SenderViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiverlayout,parent,false);
            return new ReceiverViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = messagesArrayList.get(position);

        if (holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.SenderMessage.setText(messages.getMessage());
        }
        else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.ReceiverMessage.setText(messages.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.senderId)){
            return ITEM_SEND;
        }
        else {
            return  ITEM_RECEIVE;
        }

    }

    class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView SenderMessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            SenderMessage = itemView.findViewById(R.id.SenderMessage);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView ReceiverMessage;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            ReceiverMessage = itemView.findViewById(R.id.ReceiverMessage);
        }
    }

}