package com.example.knowitall.ui.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.knowitall.R;
import com.example.knowitall.data.model.QuestionModel;
import com.example.knowitall.databinding.ActivityAddQuestionBinding;
import com.google.firebase.database.FirebaseDatabase;

public class AddQuestion extends AppCompatActivity {
    ActivityAddQuestionBinding binding;
    int set;
    String topicName;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        set= getIntent().getIntExtra("setNum",-1);
        topicName= getIntent().getStringExtra("topic");

        database = FirebaseDatabase.getInstance();
        if (set == -1) {
            finish();
        }

        binding.btnUploadQuestion.setOnClickListener(view -> {
            int correctAns = -1;
            for (int i = 0; i < 4; i++) {
                EditText ans= (EditText) binding.answerContainer.getChildAt(i);
                if (ans.getText().toString().isEmpty()) {
                    ans.setError("Không được để trống");
                    return;
                }

                RadioButton rb = (RadioButton) binding.radioGroup.getChildAt(i);
                if (rb.isChecked()) {
                    correctAns= i;
                    break;
                }


            }
            if (correctAns == -1) {
                Toast.makeText(this, "Chưa chọn đáp án", Toast.LENGTH_SHORT).show();
                return;
            }
            EditText finalAnswer= (EditText) binding.answerContainer.getChildAt(correctAns);
            QuestionModel questionModel = new QuestionModel();
            questionModel.setQuestion(binding.inputQuestion.getText().toString());
            questionModel.setA(binding.ansA.getText().toString());
            questionModel.setB(binding.ansB.getText().toString());
            questionModel.setC(binding.ansC.getText().toString());
            questionModel.setD(binding.ansD.getText().toString());
            questionModel.setCorrectAnswer(finalAnswer.getText().toString());
            questionModel.setSetNum(set);

            database.getReference().child("questions").child(topicName).child("set" + String.valueOf(set)).push().setValue(questionModel).addOnSuccessListener(unused -> {
                Toast.makeText(this, "Thêm câu hỏi thành công", Toast.LENGTH_SHORT).show();
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });

        });
    }
}