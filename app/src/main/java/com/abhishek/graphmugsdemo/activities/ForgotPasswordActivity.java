package com.abhishek.graphmugsdemo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.abhishek.graphmugsdemo.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageView back;
    private TextInputLayout email;
    private AppCompatButton resetPasswordBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initialise();

        back.setOnClickListener(view -> onBackPressed());
        resetPasswordBtn.setOnClickListener(view -> resetPassword());

    }

    private void initialise() {
        back = findViewById(R.id.backForgetPass);
        email = findViewById(R.id.emailForgotPassword);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        progressBar = findViewById(R.id.progressBarForgotPass);
        mAuth = FirebaseAuth.getInstance();

    }

    private Boolean validateEmail() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private void resetPassword() {
        if (!validateEmail()) {
            return;
        }

        String emailStr = email.getEditText().getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(emailStr)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ForgotPasswordActivity.this, "Check your email to reset the password!", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ForgotPasswordActivity.this, "Something went wrong! Try again", Toast.LENGTH_SHORT).show();
                    }

                });

    }

}