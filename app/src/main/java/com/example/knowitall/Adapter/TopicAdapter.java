package com.example.knowitall.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knowitall.R;
import com.example.knowitall.data.model.TopicModel;
import com.example.knowitall.databinding.ItemTopicBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.viewHolder> {
    Context context;
    ArrayList<TopicModel> list;

    public TopicAdapter(Context context, ArrayList<TopicModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        TopicModel model = list.get(position);
        holder.binding.topicName.setText(model.getTopicName());

        Picasso.get()
                .load(model.getTopicImage())
                .placeholder(R.drawable.logo2_app) // Hình ảnh mặc định khi tải
                .error(R.drawable.ic_menu_gallery) // Hình ảnh hiển thị khi lỗi
                .into(holder.binding.imageTopic); // ImageView mà hình ảnh sẽ được hiển thị
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ItemTopicBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemTopicBinding.bind(itemView);
        }
    }
}
