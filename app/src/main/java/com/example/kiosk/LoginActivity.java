package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button loginBtn;
    private MaterialButton backBtn;

    private static final String TAG = "LoginActivity";

    // Firebase reference for admins
    private DatabaseReference adminsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        backBtn = findViewById(R.id.backtostartbtn);
        etUsername = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);

        // Back button
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }

        // Firebase reference
        adminsRef = FirebaseDatabase.getInstance().getReference().child("admins");

        // Login button click
        loginBtn.setOnClickListener(v -> {
            String inputUsername = etUsername.getText().toString().trim();
            String inputPassword = etPassword.getText().toString().trim();

            if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            checkAdminLogin(inputUsername, inputPassword);
        });
    }

    // Check login against Firebase admins
    private void checkAdminLogin(String username, String password) {
        adminsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                boolean loginSuccess = false;

                for (DataSnapshot adminSnapshot : snapshot.getChildren()) {
                    // Read values safely as strings regardless of key order
                    String firebaseUsername = String.valueOf(adminSnapshot.child("username").getValue());
                    String firebasePassword = String.valueOf(adminSnapshot.child("password").getValue());

                    if (firebaseUsername.equals(username) && firebasePassword.equals(password)) {
                        loginSuccess = true;
                        break;
                    }
                }

                if (loginSuccess) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, AdminDashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Firebase error: ", task.getException());
            }
        });
    }
}