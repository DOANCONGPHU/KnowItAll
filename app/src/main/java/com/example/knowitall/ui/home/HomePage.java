package com.example.knowitall.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.knowitall.Adapter.TopicAdapter;
import com.example.knowitall.data.model.TopicModel;
import com.example.knowitall.databinding.ActivityHomePageBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomePage extends AppCompatActivity {
    ActivityHomePageBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    CircleImageView topicImage;


    ProgressDialog progressDialog;

    ArrayList<TopicModel> list;
    TopicAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();

        // set layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewPrj.setLayoutManager(layoutManager);
        adapter = new TopicAdapter(this,list,false);
        binding.recyclerViewPrj.setAdapter(adapter);
        database.getReference().child("topics").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list.clear();
                    // lấy dữ liệu
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        list.add(new TopicModel(
                                dataSnapshot.child("topicName").getValue().toString(),
                                dataSnapshot.child("topicImage").getValue().toString(),
                                dataSnapshot.getKey(),
                                Integer.parseInt(dataSnapshot.child("setNum").getValue().toString())
                        ));
                    }
                    // cập nhật
                    adapter.notifyDataSetChanged();
                    binding.recyclerViewPrj.setAdapter(adapter);
                }
            }
            // nếu có lỗi
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        };
    }


