package com.example.knowitall.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.knowitall.R;
import com.example.knowitall.ui.admin.QuestionAd;
import com.example.knowitall.ui.home.Question;
import com.example.knowitall.ui.home.StartTest;

public class ChooseQuesAdapter extends BaseAdapter {
    public int sets=0;
    private final String topic;

    public ChooseQuesAdapter(int sets, String topic) {
        this.sets = sets;
        this.topic = topic;

    }


    public int getCount() {
        return sets + 1; // Add 1 for the "+" button
    }


    public Object getItem(int position) {
        return null; // Or return actual set data if available
    }


    public long getItemId(int position) {
        return position; // Return the position for item identification
    }

    static class ViewHolder {
        TextView setNameTextView;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        SetAdapter.ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false);
            holder = new SetAdapter.ViewHolder();
            holder.setNameTextView = convertView.findViewById(R.id.setName);
            convertView.setTag(holder);
        } else {
            holder = (SetAdapter.ViewHolder) convertView.getTag();
        }

        // Sử dụng ViewHolder để tránh tìm kiếm lại View
        if (holder.setNameTextView != null) {
            if (position == 0) {
                ((CardView) convertView.findViewById(R.id.setcard)).setVisibility(View.GONE);
            } else {
                holder.setNameTextView.setText(String.valueOf(position));
            }
        } else {
            Log.e("SetAdapter", "Lại bị lỗi gì đó");
        }

        // Sử dụng biến final để truy cập trong OnClickListener
        final int currentPosition = position;
        convertView.setOnClickListener(v -> {

                Intent intent = new Intent(parent.getContext(), StartTest.class);
                intent.putExtra("setNum", position);
                intent.putExtra("topicName", topic);
                parent.getContext().startActivity(intent);

        });

        return convertView;
    }

}
