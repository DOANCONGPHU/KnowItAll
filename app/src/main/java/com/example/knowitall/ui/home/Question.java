package com.example.knowitall.ui.home;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.knowitall.R;
import com.example.knowitall.data.model.QuestionModel;
import com.example.knowitall.databinding.ActivityTestBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.knowitall.databinding.BtnextBinding;

import java.util.ArrayList;

public class Question extends AppCompatActivity {

    ActivityTestBinding binding;
    ArrayList<QuestionModel> list;
    int count = 0;
    int position = 0;
    int score = 0;
    CountDownTimer countDownTimer;
    FirebaseDatabase database;
    String topicName;
    int set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        topicName = getIntent().getStringExtra("topicName");
        set = getIntent().getIntExtra("set", 1);
        list = new ArrayList<>();
        BtnextBinding btnextBinding = BtnextBinding.bind(binding.include.getRoot());

        database.getReference().child("questions").child(topicName).child("set" + set).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                QuestionModel model = dataSnapshot.getValue(QuestionModel.class);
                                list.add(model);
                            }
                            if (list.size() > 0) {
                                updateQuestionAndAnswers();
                                for (int i = 0; i < 4; i++) {

                                    binding.optionAns.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            CheckAns((Button) view);
                                        }
                                    });
                                }

                                btnextBinding.btnNext.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        enbleOption(true);
                                        btnextBinding.btnNext.setEnabled(false);
                                        position++;
                                        if(position == list.size()){
                                            Intent intent = new Intent(Question.this, EndActivity.class);
                                            intent.putExtra("score", score);
                                            intent.putExtra("toltalQuestion", list.size());
                                            startActivity(intent);
                                            finish();

                                            return;
                                        }
                                        count=0;
                                        playAnimation(binding.textQuest,0,list.get(position).getQuestion());
                                    }
                                });
                            }else{
                                Toast.makeText(Question.this, "Không tồn tại câu hỏi", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(Question.this, "Không tồn tại câu hỏi", Toast.LENGTH_SHORT).show();
                        }


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Question.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateQuestionAndAnswers() {

        playAnimation(binding.textQuest, 0, list.get(position).getQuestion());


        String[] options = {list.get(position).getA(), list.get(position).getB(), list.get(position).getC(), list.get(position).getD()};
        for (int i = 0; i < options.length; i++) {
            playAnimation(binding.optionAns.getChildAt(i), 0, options[i]);
        }
    }
    private void enbleOption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            binding.optionAns.getChildAt(i).setEnabled(enable);
            if(enable){
                binding.optionAns.getChildAt(i).setBackgroundColor(R.drawable.btn_option);;
            }
        }
    }

    private void playAnimation(View view,int value, String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {
                        if(value==0 && count<4){
                            String option="";
                            if(count==0) {
                                option = list.get(position).getA();
                            }else if(count==1){
                                option = list.get(position).getB();
                            }else if(count==2){
                                option = list.get(position).getC();
                            }else if(count==3) {
                                option = list.get(position).getD();

                            }

                            playAnimation(binding.optionAns.getChildAt(count),1,option);
                            count++;



                        }
                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        if(value==0){
                            try {
                                ((TextView)view).setText(data);

                            }catch (Exception e){
                                ((Button)view).setText(data);
                            }
                            view.setTag(data);
                            playAnimation(view,1,data);
                        }

                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {


                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {

                        }


                });

    }

    private void CheckAns(Button view) {
        enbleOption(false);
        BtnextBinding btnextBinding = BtnextBinding.bind(binding.include.getRoot());
        btnextBinding.btnNext.setEnabled(true);
        if(view.getText().toString().equals(list.get(position).getCorrectAnswer())){
            score++;
            view.setBackgroundColor(R.drawable.correct_ans);
        }else{
            view.setBackgroundColor(R.drawable.wrong_ans);
            Button corectAns= (Button) binding.optionAns.findViewWithTag(list.get(position).getCorrectAnswer());
            corectAns.setBackgroundColor(R.drawable.correct_ans);
        }
    }
}



   