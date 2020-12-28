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

 import com.example.app_1.R;
 import com.example.app_1.models.User;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;
    private EditText editTextUsername, editTextPassword, editTextEmail;
    private Button btnSignUp;
    private TextView textSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_up);
         progressBar = findViewById(R.id.progressBarSignUp);

        mAuth = FirebaseAuth.getInstance();

                editTextEmail = findViewById(R.id.editTextSignUpEmail);
        editTextUsername = findViewById(R.id.editTextSignUpLogin);
        editTextPassword = findViewById(R.id.editTextSignUpPassword);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v -> {
            registerUser();
        });

        
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        if (username.isEmpty()) {
            editTextUsername.setError("Login is required!");
            editTextUsername.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User(username, email, password);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignUpActivity.this, "User has be reg " +
                                                        "successfully!",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this,
                                                SignInActivity.class);
                                        startActivity(intent);
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignUpActivity.this, "User has be reg " +
                                                "failed! Try again!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, "User has be reg " +
                                "failed! Try again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}