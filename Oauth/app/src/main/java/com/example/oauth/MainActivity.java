package com.example.oauth;

import android.content.Intent;
import android.os.Bundle;

import com.example.oauth.api.AuthRepository;
import com.example.oauth.ui.LoginActivity;
import com.example.oauth.utils.TokenManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AuthRepository repo = new AuthRepository();
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tokenManager = new TokenManager(this);

        // Si no hay token, regresa al login de inmediato
        if (!tokenManager.isLoggedIn()) {
            goToLogin();
            return;
        }

        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        btnLogout.setOnClickListener(v -> {
            repo.logout(this, new AuthRepository.AuthCallback() {
                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this,
                                message, Toast.LENGTH_SHORT).show();
                        goToLogin();
                    });
                }

                @Override
                public void onError(String error) {
                    // Aunque falle el servidor, limpiamos token y mandamos al login
                    runOnUiThread(() -> goToLogin());
                }
            });
        });

        btnDeleteAccount.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar cuenta")
                    .setMessage("¿Estás seguro? Esta acción no se puede deshacer.")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        repo.deleteAccount(this, new AuthRepository.AuthCallback() {
                            @Override
                            public void onSuccess(String message) {
                                runOnUiThread(() -> {
                                    Toast.makeText(MainActivity.this,
                                            message, Toast.LENGTH_SHORT).show();
                                    goToLogin();
                                });
                            }

                            @Override
                            public void onError(String error) {
                                runOnUiThread(() ->
                                        Toast.makeText(MainActivity.this,
                                                error, Toast.LENGTH_LONG).show()
                                );
                            }
                        });
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}