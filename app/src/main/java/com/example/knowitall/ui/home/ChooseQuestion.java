package com.example.knowitall.ui.home;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.knowitall.Adapter.ChooseQuesAdapter;
import com.example.knowitall.Adapter.SetAdapter;
import com.example.knowitall.databinding.ActivityChooseQuestionBinding;
import com.example.knowitall.databinding.ActivitySetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class ChooseQuestion extends AppCompatActivity {
    ActivityChooseQuestionBinding binding;
    FirebaseDatabase database;
    ChooseQuesAdapter adapter;

    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        key=getIntent().getStringExtra("key");

        adapter= new ChooseQuesAdapter(getIntent().getIntExtra("sets",0),
                getIntent().getStringExtra("topic"));

        binding.gridView1.setAdapter(adapter);
    }
}