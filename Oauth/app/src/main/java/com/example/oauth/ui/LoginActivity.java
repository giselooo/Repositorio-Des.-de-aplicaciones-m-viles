package com.example.oauth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oauth.MainActivity;
import com.example.oauth.R;
import com.example.oauth.api.AuthRepository;

public class LoginActivity extends AppCompatActivity {

    private AuthRepository repo = new AuthRepository();
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvGoRegister;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        tvGoRegister = findViewById(R.id.tvGoRegister);

        // Ir a registro
        tvGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            btnLogin.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            repo.login(this, username, password, new AuthRepository.AuthCallback() {

                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);
                        Toast.makeText(LoginActivity.this,
                                message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish(); // evita regresar al login con el botón atrás
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);
                        Toast.makeText(LoginActivity.this,
                                error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        });
    }
}
