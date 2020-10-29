package com.example.connectme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import control.AcctMgr;
import entity.AccountMgrInterface;

public class LoginActivity extends AppCompatActivity {

    private AccountMgrInterface accountMgr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountMgr = new AcctMgr();

        final EditText emailEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar progressBar = findViewById(R.id.loading);
        final TextView createAccount = findViewById(R.id.create_account);
        final TextView forgotPassword = findViewById(R.id.forgot_password);

        loginButton.setOnClickListener(v -> {
            final String email = getInput(emailEditText);
            final String password = getInput(passwordEditText);

            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Email is Required.");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Password is Required.");
                return;
            }

            if (password.length() < 6) {
                passwordEditText.setError("Password Must be >= 6 Characters");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            accountMgr.signIn(email, password, LoginActivity.this);
        });

        createAccount.setOnClickListener(v -> {
                Intent intent = new Intent(createAccount.getContext(), CreateAccountActivity.class);
                startActivity(intent);
                finish();
        });
    }

    public String getInput(EditText editText) {
        return editText.getText().toString().trim();
    }
}

