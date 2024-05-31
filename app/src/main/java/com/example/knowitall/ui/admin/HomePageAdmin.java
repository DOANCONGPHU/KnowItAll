package com.example.knowitall.ui.admin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.knowitall.Adapter.TopicAdapter;
import com.example.knowitall.R;
import com.example.knowitall.data.model.TopicModel;
import com.example.knowitall.databinding.ActivityHomePageAdBinding;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;


import de.hdodenhof.circleimageview.CircleImageView;

public class HomePageAdmin extends AppCompatActivity {
    ActivityHomePageAdBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    CircleImageView topicImage;
    EditText topicName;
    Button topicUpload;
    Dialog dialog;

    View fetchImage;
    Uri imageUri;
    int i=0;
    ProgressDialog progressDialog;

    ArrayList<TopicModel> list;
    TopicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page_ad);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = ActivityHomePageAdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        list = new ArrayList<>();


        dialog= new Dialog( this);
        dialog.setContentView(R.layout.add_item_topic);

        if(dialog.getWindow() != null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
        }

        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Đang tải lên");
        progressDialog.setMessage("Loading");

        topicUpload = dialog.findViewById(R.id.btn_upload_topic);
        topicName = dialog.findViewById(R.id.input_topic);
        topicImage = dialog.findViewById(R.id.image_topic);
        fetchImage = dialog.findViewById(R.id.fetchImage);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerTopic.setLayoutManager(layoutManager);

        adapter = new TopicAdapter(this,list);
        binding.recyclerTopic.setAdapter(adapter);

        database.getReference().child("topics").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        list.add(new TopicModel(
                                dataSnapshot.child("topicName").getValue().toString(),
                                dataSnapshot.child("topicImage").getValue().toString(),
                                dataSnapshot.getKey(),
                                Integer.parseInt(dataSnapshot.child("setNum").getValue().toString())
                        ));
                    }
                    adapter.notifyDataSetChanged();
                    binding.recyclerTopic.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePageAdmin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        binding.addTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

            }
        });
        fetchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        topicUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = topicName.getText().toString();
                if(imageUri==null){
                    Toast.makeText(HomePageAdmin.this,"Hãy thêm ảnh", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()) {
                    topicName.setError("Nhập tên topic");
                    
                }else {
                    progressDialog.show();
                    uploadData();
                    dialog.dismiss();
                }

            }
        });

    }
    // đẩy dữ liệu lên firebase
    private void uploadData() {
        final StorageReference reference = storage.getReference().child("topic").child( new Date().getTime()+" ");
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        TopicModel topicModel= new TopicModel();
                        topicModel.setTopicName(topicName.getText().toString());
                        topicModel.setSetNum(0);
                        topicModel.setTopicImage(uri.toString());

                        database.getReference().child("topics").child("topic" + i++).setValue(topicModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(HomePageAdmin.this,"Đã tải lên", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(HomePageAdmin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });

                    }
                });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            if(data != null) {
                imageUri = data.getData();
                topicImage.setImageURI(imageUri);

            }
        }
    }
}