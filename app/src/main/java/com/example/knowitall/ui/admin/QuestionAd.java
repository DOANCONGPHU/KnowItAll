package com.example.knowitall.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.knowitall.Adapter.QuestionAdapter;
import com.example.knowitall.data.model.QuestionModel;
import com.example.knowitall.databinding.ActivityQuestionBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionAd extends AppCompatActivity {
    ActivityQuestionBinding binding;
    FirebaseDatabase db ;
    ArrayList<QuestionModel> questionLists;
    QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db= FirebaseDatabase.getInstance();
        questionLists = new ArrayList<>();

        //get set number
        int setNum = getIntent().getIntExtra("setNum", 0);
        String topicName = getIntent().getStringExtra("topicName");

        //set question
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyQuestion.setLayoutManager(layoutManager);
        questionAdapter = new QuestionAdapter(this, questionLists);
        binding.recyQuestion.setAdapter(questionAdapter);

        db.getReference().child("questions").child(topicName).child("set" + setNum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    questionLists.clear();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        QuestionModel questionModel = ds.getValue(QuestionModel.class);
                        questionModel.setKey(ds.getKey());
                        questionLists.add(questionModel);

                    }
                    questionAdapter.notifyDataSetChanged();
                    binding.recyQuestion.setAdapter(questionAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuestionAd.this, "Câu hỏi không tồn tại", Toast.LENGTH_SHORT).show();
            }
        });
        //add question
        binding.addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionAd.this, AddQuestion.class);
                intent.putExtra("setNum", setNum);
                intent.putExtra("topic", topicName);
                startActivity(intent);
            }
        });
    }
}