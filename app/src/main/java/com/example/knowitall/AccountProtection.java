package com.example.knowitall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AccountProtection extends AppCompatActivity {
    private TextView tvChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_protection_layout);

        tvChangePassword = findViewById(R.id.tv_protection_update);
        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickResetPassword();
            }
        });
    }

    public void onClickResetPassword() {
        Intent intent = new Intent(AccountProtection.this, ChangePassword.class);
        startActivity(intent);
    }
}