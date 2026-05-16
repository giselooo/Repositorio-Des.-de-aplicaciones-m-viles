package com.example.oauth.api;

import android.content.Context;

import com.example.oauth.models.AuthResponse;
import com.example.oauth.models.LoginRequest;
import com.example.oauth.models.RegisterRequest;
import com.example.oauth.utils.TokenManager;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthRepository {
    private final Gson gson = new Gson();

    public interface AuthCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public void register(String email, String username,
                         String password, String fullName, AuthCallback cb) {
        RegisterRequest body = new RegisterRequest(email, username, password, fullName);
        RequestBody reqBody = RequestBody.create(gson.toJson(body), ApiClient.JSON());
        Request request = new Request.Builder()
                .url(ApiClient.BASE_URL + "api/auth/register")
                .post(reqBody)
                .build();

        ApiClient.getClient().newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                cb.onError("Sin conexión: " + e.getMessage());
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) cb.onSuccess("Cuenta creada exitosamente");
                else cb.onError("Error " + response.code() + ": " + response.body().string());
            }
        });
    }

    public void login(Context ctx, String username,
                      String password, AuthCallback cb) {
        LoginRequest body = new LoginRequest(username, password);
        RequestBody reqBody = RequestBody.create(gson.toJson(body), ApiClient.JSON());
        Request request = new Request.Builder()
                .url(ApiClient.BASE_URL + "api/auth/login")
                .post(reqBody)
                .build();

        ApiClient.getClient().newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                cb.onError("Sin conexión: " + e.getMessage());
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                String respBody = response.body().string();
                if (response.isSuccessful()) {
                    AuthResponse auth = gson.fromJson(respBody, AuthResponse.class);
                    new TokenManager(ctx).saveToken(auth.getAccessToken());
                    cb.onSuccess("Login exitoso");
                } else cb.onError("Credenciales incorrectas");
            }
        });
    }

    public void logout(Context ctx, AuthCallback cb) {
        String token = new TokenManager(ctx).getToken();
        Request request = new Request.Builder()
                .url(ApiClient.BASE_URL + "api/auth/logout")
                .post(RequestBody.create("", ApiClient.JSON()))
                .addHeader("Authorization", "Bearer " + token)
                .build();

        ApiClient.getClient().newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                cb.onError("Error de red: " + e.getMessage());
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                new TokenManager(ctx).clearToken();
                cb.onSuccess("Sesión cerrada");
            }
        });
    }

    public void deleteAccount(Context ctx, AuthCallback cb) {
        String token = new TokenManager(ctx).getToken();
        Request request = new Request.Builder()
                .url(ApiClient.BASE_URL + "api/users/me")
                .delete()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        ApiClient.getClient().newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                cb.onError("Error de red: " + e.getMessage());
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    new TokenManager(ctx).clearToken();
                    cb.onSuccess("Cuenta eliminada");
                } else cb.onError("Error al eliminar: " + response.code());
            }
        });
    }
}
