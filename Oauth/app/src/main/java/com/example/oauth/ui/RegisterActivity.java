package com.example.oauth.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oauth.R;
import com.example.oauth.api.AuthRepository;

public class RegisterActivity extends AppCompatActivity {

    private AuthRepository repo = new AuthRepository();
    private EditText etFullName, etUsername, etEmail, etPassword;
    private Button btnRegister;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.etFullName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);

        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
           String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 8) {
                Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            btnRegister.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            repo.register(email, username, password, fullName, new AuthRepository.AuthCallback() {
                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnRegister.setEnabled(true);
                        Toast.makeText(RegisterActivity.this,
                                message, Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnRegister.setEnabled(true);
                        Toast.makeText(RegisterActivity.this,
                                error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        });
    }
}
