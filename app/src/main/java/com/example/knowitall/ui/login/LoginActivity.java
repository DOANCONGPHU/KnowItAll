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
//    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding1 = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding1.getRoot());
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
//        progressDialog= new ProgressDialog(LoginActivity.this);
//        progressDialog.setTitle("Đang tạo tài khoản");
//        progressDialog.setMessage("setmetsage loi");




        final ProgressBar loadingProgressBar = binding1.loading;

        binding1.signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent1);
            }
        });

        binding1.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding1.userEmail.getText().toString().trim();
                String password = binding1.password.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(binding1.userEmail.getText().toString(),binding1.password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if(task.isSuccessful()){
                                    if (email.equals("admin@gmail.com") && password.equals("congphu01")) {
                                        // Chuyển hướng đến home_page_ad
                                        Intent intent = new Intent(LoginActivity.this, home_page_ad.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


}
}