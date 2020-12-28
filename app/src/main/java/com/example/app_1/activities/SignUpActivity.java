package com.example.app_1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.app_1.R;
import com.example.app_1.databinding.FragmentSignUpBinding;
import com.example.app_1.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private FragmentSignUpBinding mFragmentSignUpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_up);
        mFragmentSignUpBinding = DataBindingUtil.setContentView(this, R.layout.fragment_sign_up);

        mAuth = FirebaseAuth.getInstance();


        mFragmentSignUpBinding.btnSignUp.setOnClickListener(v -> {
            registerUser();
        });

        mFragmentSignUpBinding.textSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String email = mFragmentSignUpBinding.editTextSignUpEmail.getText().toString().trim();
        String username = mFragmentSignUpBinding.editTextSignUpLogin.getText().toString().trim();
        String password = mFragmentSignUpBinding.editTextSignUpPassword.getText().toString().trim();


        if (username.isEmpty()) {
            mFragmentSignUpBinding.editTextSignUpLogin.setError("Login is required!");
            mFragmentSignUpBinding.editTextSignUpLogin.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            mFragmentSignUpBinding.editTextSignUpEmail.setError("Email is required!");
            mFragmentSignUpBinding.editTextSignUpEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mFragmentSignUpBinding.editTextSignUpEmail.setError("Please provide valid email!");
            mFragmentSignUpBinding.editTextSignUpEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            mFragmentSignUpBinding.editTextSignUpPassword.setError("Password is required!");
            mFragmentSignUpBinding.editTextSignUpPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mFragmentSignUpBinding.editTextSignUpPassword.setError("Min password length should be 6 characters!");
            mFragmentSignUpBinding.editTextSignUpPassword.requestFocus();
            return;
        }

        mFragmentSignUpBinding.progressBarSignUp.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User(username, email, password);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                mFragmentSignUpBinding.progressBarSignUp.setVisibility(View.GONE);
                                Toast.makeText(SignUpActivity.this, "User has be reg " +
                                                "successfully!",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this,
                                        SignInActivity.class);
                                startActivity(intent);
                            } else {
                                mFragmentSignUpBinding.progressBarSignUp.setVisibility(View.GONE);
                                Toast.makeText(SignUpActivity.this, "User has be reg " +
                                        "failed! Try again!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        mFragmentSignUpBinding.progressBarSignUp.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, "User has be reg " +
                                "failed! Try again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}