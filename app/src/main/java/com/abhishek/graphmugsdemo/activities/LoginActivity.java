package com.abhishek.graphmugsdemo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.abhishek.graphmugsdemo.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    //Variables
    private TextInputLayout email, password;
    private TextInputEditText emailEditText, passwordEditText;
    private AppCompatButton forgotPassword, logIn, signUp;
    private CheckBox rememberMe;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean remember, loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("LOGIN_DETAILS", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loggedIn = sharedPreferences.getBoolean("isLoggedIn", false);


        if (loggedIn) {
            goToHomePage();
        } else {
            setContentView(R.layout.activity_login);
        }

        //Initialise the variables
        initialise();

        //Set the values if remember me is checked
        emailEditText.setText(sharedPreferences.getString("email", null));
        passwordEditText.setText(sharedPreferences.getString("password", null));

        //Hide Navigation Bar
        hideNavigationBar();

        logIn.setOnClickListener(view -> userLogin());
        signUp.setOnClickListener(view -> goToSignUp());
        forgotPassword.setOnClickListener(view -> goToForgotPassword());

    }

    private void userLogin() {

        if (!validateEmail() | !validatePassword()) {
            return;
        }

        String emailStr = email.getEditText().getText().toString().trim();
        String passwordStr = password.getEditText().getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Login the user

                        if (remember) {

                            editor.putString("email", emailStr);
                            editor.putString("password", passwordStr);
                            editor.apply();

                        }

                        editor.putBoolean("isLoggedIn", true).apply();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                        goToHomePage();
                    } else {
                        //Login Failed
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Log in Failed! Please check your credentials", Toast.LENGTH_SHORT).show();
                        emptyFields();
                    }
                });

    }

    private void emptyFields() {
        email.getEditText().setText("");
        password.getEditText().setText("");
    }

    private void goToHomePage() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToForgotPassword() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
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

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private void goToSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void hideNavigationBar() {
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    private void initialise() {
        emailEditText = findViewById(R.id.emailLogInTextInput);
        passwordEditText = findViewById(R.id.passwordLogInTextInput);
        email = findViewById(R.id.emailLogIn);
        password = findViewById(R.id.passwordLogIn);
        rememberMe = findViewById(R.id.rememberMe);
        remember = rememberMe.isChecked();
        forgotPassword = findViewById(R.id.forgotPassword);
        logIn = findViewById(R.id.loginBtn);
        signUp = findViewById(R.id.loginSignUp);
        progressBar = findViewById(R.id.progressBarLogin);
        mAuth = FirebaseAuth.getInstance();
    }
}