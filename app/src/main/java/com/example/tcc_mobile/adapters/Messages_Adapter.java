package com.example.tcc_mobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.Messages;

import java.util.ArrayList;
import java.util.List;

public class Messages_Adapter extends BaseAdapter {
    private List<Messages> message_list = new ArrayList<>();
    Context context;

    public Messages_Adapter(Context context) {
        this.context = context;
    }

    public List<Messages> getLista_messages() {
        return message_list;
    }

    public void add(Messages message) {
        this.message_list.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }
    public void clear(){
        this.message_list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return message_list.size();
    }

    @Override
    public Object getItem(int position) {
        return message_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MessagesViewHolder holder = new MessagesViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Messages message = message_list.get(position);

        if (message.getTo_user() == message.getRequest_user()) {
            convertView = messageInflater.inflate(R.layout.message, null);
            holder.message_text = convertView.findViewById(R.id.message_body_my);
            holder.message_text.setText(message.getMessage());
            convertView.setTag(holder);
        } else{
            convertView = messageInflater.inflate(R.layout.their_message, null);
            holder.message_text = convertView.findViewById(R.id.message_body_their);
            holder.message_text.setText(message.getMessage());
            convertView.setTag(holder);
        }

        return convertView;
    }

    public static class MessagesViewHolder {
        TextView message_text;
    }
}
