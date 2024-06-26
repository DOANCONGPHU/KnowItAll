package com.example.knowitall.ui.login;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.knowitall.R;
import com.example.knowitall.ui.home.HomePage;
import com.example.knowitall.databinding.ActivityLoginBinding;
import com.example.knowitall.ui.admin.HomePageAdmin;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

public class LoginActivity extends AppCompatActivity {

    private TextView forgotPass;

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding1;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding1 = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding1.getRoot());

        firebaseDatabase= FirebaseDatabase.getInstance();

        // Chuyển hướng đến SignUp
        binding1.signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent1);
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        firebaseAuth= FirebaseAuth.getInstance();
        binding1.loginGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ggSignIn();
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
                                    if (email.equals("admin@gmail.com") && password.equals("admin001")) {
                                        // Chuyển hướng đến home_page_ad
                                        hander();
                                        Intent intent = new Intent(LoginActivity.this, HomePageAdmin.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        // Chuyển hướng đến Main
                                        hander();
                                        Intent intent= new Intent(LoginActivity.this,HomePage.class);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập bằng GG thành công", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, HomePage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập bằng GG thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void ggSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser user) {

    }

}