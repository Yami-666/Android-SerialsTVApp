package com.example.app_1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.app_1.R;
import com.example.app_1.databinding.FragmentSignInBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FragmentSignInBinding mFragmentSignInBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_in);
        mFragmentSignInBinding = DataBindingUtil.setContentView(this, R.layout.fragment_sign_in);

        mAuth = FirebaseAuth.getInstance();

        mFragmentSignInBinding.textSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        mFragmentSignInBinding.btnSignIn.setOnClickListener(v -> userLogin());
    }

    private void userLogin() {
        String email = mFragmentSignInBinding.editTextSignInLogin.getText().toString().trim();
        String password = mFragmentSignInBinding.editTextSignInPassword.getText().toString().trim();

        if (email.isEmpty()) {
            mFragmentSignInBinding.editTextSignInLogin.setError("Email is required!");
            mFragmentSignInBinding.editTextSignInLogin.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mFragmentSignInBinding.editTextSignInLogin.setError("Please enter a valid email!");
            mFragmentSignInBinding.editTextSignInLogin.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            mFragmentSignInBinding.editTextSignInPassword.setError("Password is required!");
            mFragmentSignInBinding.editTextSignInPassword.requestFocus();
            return;
        }

        mFragmentSignInBinding.progressBarSignIn.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mFragmentSignInBinding.progressBarSignIn.setVisibility(View.GONE);
                //redirect to main activity
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                mFragmentSignInBinding.progressBarSignIn.setVisibility(View.GONE);
                Toast.makeText(SignInActivity.this, "Failed to login! Try again later!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}