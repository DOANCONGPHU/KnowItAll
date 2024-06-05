package com.example.knowitall.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.knowitall.R;
import com.example.knowitall.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        binding= ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(SignUp.this, LoginActivity.class);
                startActivity(intent1);
            }
        });

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        progressDialog= new ProgressDialog(SignUp.this);
        progressDialog.setTitle("Đang tạo tài khoản");
        progressDialog.setMessage("Vui lòng đợi");

        binding.btnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = binding.password.getText().toString();
                String confirmPassword = binding.confirmPass.getText().toString();
                String userEmail= binding.userEmail.getText().toString();
                String userName= binding.userName.getText().toString();

                String msg="Email không hợp lệ";
                if (password.isEmpty() || confirmPassword.isEmpty() || userName.isEmpty() || userEmail.isEmpty()) {
                    Toast.makeText(SignUp.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(LoginViewModel.isUserNameValid(userEmail))) {
                    msg="Tên không hợp lệ";

                }
                if (!password.equals(confirmPassword)) {
                    msg="Mật khẩu không khớp";
                }
                if (!(LoginViewModel.isPasswordValid(password))) {
                    msg="Mật khẩu phải nhiều hơn 5 kí tự";
                }

                progressDialog.show();
                String finalMsg = msg;
                firebaseAuth.createUserWithEmailAndPassword(userEmail, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();

                                if (task.isSuccessful()) {
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("userName", userName);
                                    map.put("userEmail", userEmail);
                                    map.put("password", password);
                                    String id = task.getResult().getUser().getUid();

                                    firebaseDatabase.getReference().child("user").child(id).setValue(map);

                                    Intent intent = new Intent(SignUp.this, LoginActivity.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(SignUp.this, finalMsg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }
}