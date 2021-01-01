package com.example.app_1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.app_1.R;
import com.example.app_1.databinding.FragmentSignInBinding;
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
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private FirebaseAuth mAuth;
    private FragmentSignInBinding mFragmentSignInBinding;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;

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
        mFragmentSignInBinding.btnSignInWithGoogle.setOnClickListener(v -> userLoginWithGoogle());
    }

    private void userLoginWithGoogle() {
        mFragmentSignInBinding.progressBarSignIn.setVisibility(View.VISIBLE);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                mFragmentSignInBinding.progressBarSignIn.setVisibility(View.GONE);
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        mFragmentSignInBinding.progressBarSignIn.setVisibility(View.GONE);
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        mFragmentSignInBinding.progressBarSignIn.setVisibility(View.GONE);
                        Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
                    }
                });
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