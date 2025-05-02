package com.example.workway;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextInputEditText editTextEmail, editTexPassword;
    Button signIn;
    TextView signUp, adminLogin;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.email);
        editTexPassword = findViewById(R.id.password);
        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);
        adminLogin = findViewById(R.id.admin_login); // Admin login TextView

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(false);
            }
        });

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(true);
            }
        });
    }

    private void signInUser(boolean isAdminLogin) {
        String email, password;
        email = String.valueOf(editTextEmail.getText());
        password = String.valueOf(editTexPassword.getText());

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                if (isAdminLogin) {
                                    // Check if the user is an admin and redirect to AdminActivity
                                    checkAdminRole(user);
                                } else {
                                    // Regular user login
                                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkAdminRole(FirebaseUser user) {
        String email = user.getEmail();

        if (email != null && email.contains("admin")) {
            // Redirect to AdminActivity if the user is an admin
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(MainActivity.this, "Access Denied: Not an Admin", Toast.LENGTH_SHORT).show();
        }
    }

}
