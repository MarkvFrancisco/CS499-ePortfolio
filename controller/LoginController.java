package com.example.cs360_projectthree.controller;

import android.content.Context;
import android.text.TextUtils;
import com.example.cs360_projectthree.model.LoginDBHelper;

public class LoginController {

    private LoginDBHelper DB;
    private LoginView view;

    // Interface so the Controller can talk back to the View (MainActivity)
    public interface LoginView {
        void showStatusMessage(String message);
        void onLoginSuccess();
    }

    public LoginController(Context context, LoginView view) {
        this.DB = new LoginDBHelper(context);
        this.view = view;
    }

    public void handleLogin(String username, String password) {
        //checks for empty fields
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            view.showStatusMessage("Please enter a username and password.");
            return;
        }

        //checks for valid username and password
        boolean checkUserPass = DB.checkUsernamePassword(username, password);
        if (checkUserPass) {
            view.showStatusMessage("Login successful.");
            view.onLoginSuccess();
        } else {
            view.showStatusMessage("Invalid username or password.");
        }
    }

    public void handleAccountCreation(String username, String password) {
        // 1. Check for empty fields
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            view.showStatusMessage("Please fill in all fields.");
            return;
        }

        // --- USERNAME VALIDATION ---

        // Length check
        if (username.length() < 3 || username.length() > 15) {
            view.showStatusMessage("Username must be between 3 and 15 characters.");
            return;
        }

        // Alphanumeric only (letters and numbers, no special characters)
        if (!username.matches("[a-zA-Z0-9]+")) {
            view.showStatusMessage("Username can only contain letters and numbers.");
            return;
        }

        // --- PASSWORD VALIDATION ---

        // Passwords match check
//        if (!password.equals(confirmPassword)) {
//            view.showStatusMessage("Passwords do not match.");
//            return;
//        }

        // Password cannot be the same as the username
        if (password.equalsIgnoreCase(username)) {
            view.showStatusMessage("Password cannot be the same as your username.");
            return;
        }

        // Minimum length
        if (password.length() < 8) {
            view.showStatusMessage("Password must be at least 8 characters long.");
            return;
        }

        // Requires at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            view.showStatusMessage("Password must contain at least one uppercase letter.");
            return;
        }

        // Requires at least one number
        if (!password.matches(".*[0-9].*")) {
            view.showStatusMessage("Password must contain at least one number.");
            return;
        }

        // Requires at least one special character
        if (!password.matches(".*[!@#$%^&*+=?-].*")) {
            view.showStatusMessage("Password must contain a special character (e.g., !@#$%).");
            return;
        }

        // --- ACCOUNT CREATION ---

        boolean checkUser = DB.checkUsername(username);
        if (!checkUser) {
            boolean insert = DB.insertData(username, password);
            if (insert) {
                view.showStatusMessage("Account created successfully.");
            } else {
                view.showStatusMessage("Account creation failed.");
            }
        } else {
            view.showStatusMessage("User already exists. Please log in.");
        }
    }
}