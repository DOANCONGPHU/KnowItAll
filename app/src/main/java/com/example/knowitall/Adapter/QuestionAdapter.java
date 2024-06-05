package com.example.knowitall.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knowitall.R;
import com.example.knowitall.data.model.QuestionModel;
import com.example.knowitall.databinding.ItemQuestionBinding;

import java.util.ArrayList;

public class QuestionAdapter extends  RecyclerView.Adapter<QuestionAdapter.ViewHolder>{
    Context context;
    ArrayList<QuestionModel> questionModels;
    public QuestionAdapter(Context context, ArrayList<QuestionModel> questionModels) {
        this.context = context;
        this.questionModels = questionModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionModel questionModel = questionModels.get(position);
        holder.binding.question.setText(questionModel.getQuestion());


    }

    @Override
    public int getItemCount() {
        return questionModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemQuestionBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemQuestionBinding.bind(itemView);

        }
    }
}
