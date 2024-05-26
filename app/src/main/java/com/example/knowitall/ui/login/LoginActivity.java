package com.example.knowitall.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.knowitall.MainActivity;
import com.example.knowitall.R;
import com.example.knowitall.databinding.ActivityLoginBinding;
import com.example.knowitall.ui.admin.home_page_ad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private TextView forgotPass;

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding1;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;


    LottieAnimationView lottieAnimationView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding1 = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding1.getRoot());
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();

        // Chuyển hướng đến SignUp
        binding1.signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent1);
            }
        });
        // Xử lí sự kiện đăng nhập
        binding1.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding1.userEmail.getText().toString().trim();
                String password = binding1.password.getText().toString().trim();
                if (password.isEmpty()  ||  email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding1.btnLogin.setVisibility(View.GONE);
                showProgressBar();
                firebaseAuth.signInWithEmailAndPassword(binding1.userEmail.getText().toString(),binding1.password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    if (email.equals("admin@gmail.com") && password.equals("congphu01")) {
                                        // Chuyển hướng đến home_page_ad
                                        hander();
                                        Intent intent = new Intent(LoginActivity.this, home_page_ad.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        // Chuyển hướng đến Main
                                        hander();
                                        Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                else {
                                    // Trả về lỗi
                                    Toast.makeText(LoginActivity.this, "Thông tin tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                                    hideProgressBar();
                                    binding1.btnLogin.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        });




    }

    private void hander() {
        new Handler().postDelayed(() -> {
            // Ẩn Lottie Animation sau khi xử lý xong
            hideProgressBar();
            binding1.btnLogin.setVisibility(View.VISIBLE);
            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        }, 2000);
    }

    private void showProgressBar() {
        binding1.animationView.setVisibility(View.VISIBLE);
        binding1.animationView.playAnimation();

    }

    private void hideProgressBar() {
        binding1.animationView.cancelAnimation();
        binding1.animationView.setVisibility(View.GONE);
    }

}