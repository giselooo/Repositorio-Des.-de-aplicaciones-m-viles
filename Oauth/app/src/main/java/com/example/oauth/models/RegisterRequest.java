package com.example.oauth.models;

public class RegisterRequest {
    private String email;
    private String username;
    private String password;
    private String full_name;

    public RegisterRequest(String email, String username, String password, String full_name) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.full_name = full_name;
    }
}
