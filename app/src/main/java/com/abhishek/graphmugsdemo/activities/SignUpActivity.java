package com.abhishek.graphmugsdemo.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.abhishek.graphmugsdemo.R;
import com.abhishek.graphmugsdemo.classes.UserHelperClass;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    //Variables
    private TextInputLayout regName, regUsername, regEmail, regPhoneNo, regPassword;
    private AppCompatButton register, goToLogin;
    private RelativeLayout relativeLayout;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialise();

        hideNavigationBar();

        goToLogin.setOnClickListener(view -> goToLoginActivity());

        register.setOnClickListener(view -> registerUser(view));

    }

    private void goToLoginActivity() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this, relativeLayout, "login_signup_tran");
        startActivity(intent, options.toBundle());
        finish();
    }

    private void hideNavigationBar() {
        //Hide Navigation Bar
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
        //Initialising Variables
        regName = findViewById(R.id.name);
        regUsername = findViewById(R.id.usernameSignUp);
        regEmail = findViewById(R.id.emailRegistration);
        regPhoneNo = findViewById(R.id.phoneRegistration);
        regPassword = findViewById(R.id.passwordSignUp);
        goToLogin = findViewById(R.id.logInBtnSignUp);
        register = findViewById(R.id.registerBtn);
        relativeLayout = findViewById(R.id.rlSignUp);
        progressBar = findViewById(R.id.progressBarSignUp);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("LOGIN_DETAILS", MODE_PRIVATE);
    }

    private Boolean validateName() {
        String val = regName.getEditText().getText().toString();
        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            regUsername.setError("Username too long");
            return false;
        } else if (val.length() <= 3) {
            regUsername.setError("Username too short");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            regUsername.setError("Invalid Username");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = regPhoneNo.getEditText().getText().toString();
        if (val.isEmpty()) {
            regPhoneNo.setError("Field cannot be empty");
            return false;
        } else {
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            regPassword.setError("Password is too weak");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    private void registerUser(View view) {

        if (!validateName() | !validatePassword() | !validatePhoneNo() | !validateEmail() | !validateUsername()) {
            return;
        }

        //Get all the values in string
        String name = regName.getEditText().getText().toString().trim();
        String username = regUsername.getEditText().getText().toString().trim();
        String email = regEmail.getEditText().getText().toString().trim();
        String phoneNo = regPhoneNo.getEditText().getText().toString().trim();
        String password = regPassword.getEditText().getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneNo);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(helperClass).addOnCompleteListener(task1 -> {

                                    if (task1.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignUpActivity.this, "You have been registered successfully", Toast.LENGTH_SHORT).show();
                                        goToHomePage();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignUpActivity.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                                        emptyFields();
                                    }
                                });

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                        emptyFields();
                    }

                });

    }

    private void emptyFields() {
        regName.getEditText().setText("");
        regUsername.getEditText().setText("");
        regEmail.getEditText().setText("");
        regPhoneNo.getEditText().setText("");
        regPassword.getEditText().setText("");
    }

    private void goToHomePage() {
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}