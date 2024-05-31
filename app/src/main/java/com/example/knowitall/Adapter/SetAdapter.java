package com.example.knowitall.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knowitall.R;

public class SetAdapter extends BaseAdapter {
    public int sets=0;
    private final String topic;
    private final String key;
    private final GridListener gridListener;

    public SetAdapter(int sets, String topic, String key, GridListener gridListener) {
        this.sets = sets;
        this.topic = topic;
        this.key = key;
        this.gridListener = gridListener;
    }

    @Override
    public int getCount() {
        return sets + 1; // Add 1 for the "+" button
    }

    @Override
    public Object getItem(int position) {
        return null; // Or return actual set data if available
    }

    @Override
    public long getItemId(int position) {
        return position; // Return the position for item identification
    }

    static class ViewHolder {
        TextView setNameTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false);
            holder = new ViewHolder();
            holder.setNameTextView = convertView.findViewById(R.id.setName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Sử dụng ViewHolder để tránh tìm kiếm lại View
        if (holder.setNameTextView != null) {
            holder.setNameTextView.setText(position == 0 ? "+" : String.valueOf(position));
        } else {
            Log.e("SetAdapter", "Lại bị lỗi gì đó");
        }

        // Sử dụng biến final để truy cập trong OnClickListener
        final int currentPosition = position;
        convertView.setOnClickListener(v -> {
            if (currentPosition == 0) {
                gridListener.addSet();
            } else {
                Toast.makeText(parent.getContext(), "wait", Toast.LENGTH_SHORT).show();

            }
        });

        return convertView;
    }

    public interface GridListener {
        void addSet();
    }
}
