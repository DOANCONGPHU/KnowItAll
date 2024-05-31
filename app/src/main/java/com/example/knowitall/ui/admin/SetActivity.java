package com.example.knowitall.ui.admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.knowitall.Adapter.SetAdapter;
import com.example.knowitall.R;
import com.example.knowitall.databinding.ActivitySetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class SetActivity extends AppCompatActivity {
    ActivitySetBinding binding;
    FirebaseDatabase database;
    SetAdapter adapter;
    int a=1;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         database = FirebaseDatabase.getInstance();
         key=getIntent().getStringExtra("key");

         adapter= new SetAdapter(getIntent().getIntExtra("sets",0),
                getIntent().getStringExtra("topic"),key,new SetAdapter.GridListener(){
            @Override
            public void addSet() {
                database.getReference().child("topics").child(key).
                        child("setNum").setValue(getIntent().getIntExtra("sets",0)+a++)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    adapter.sets++;
                                    adapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(SetActivity.this, "Lối gì đó, đéo biết", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });;
            }
        });

        binding.gridView.setAdapter(adapter);;

    }
}