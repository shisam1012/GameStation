package com.example.server.Auth;

/**
 * Represents a user in the system.
 * Holds basic user information - username, password and email.
 * In use in UserController when getting an http request from the client to sign up 
 * Requires a default constructor for proper JSON mapping 
 */

public class User {
    private String username;
    private String email;
    private String password;

    // Getters for each one of the fields

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    //Setters for each one of the fields

    public void setUsername(String username) {
        this.username = username;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
}
