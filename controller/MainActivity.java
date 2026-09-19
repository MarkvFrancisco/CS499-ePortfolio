package com.example.cs360_projectthree.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs360_projectthree.R;

//--------commented out and MOVED TO the LoginController.java file per MVC pattern--------
//import com.example.cs360_projectthree.model.LoginDBHelper;

public class MainActivity extends AppCompatActivity implements LoginController.LoginView {

    //establishing datatypes for UI elements
    EditText usernameText, passwordText;
    Button buttonLogin, buttonCreateAccount;
    TextView textStatus;

    //LoginDBHelper DB;

    private LoginController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linking the UI elements from the activity_main.xml
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        textStatus = findViewById(R.id.textStatus);

        //Creating an instance of the LoginDBHelper
        //DB = new LoginDBHelper(this);

        controller = new LoginController(this, this);

        //Click listener for create account button
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = usernameText.getText().toString();
                String pass = passwordText.getText().toString();

                //--------commented out and MOVED TO the LoginController.java file per MVC pattern--------

//                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
//                    textStatus.setText("Please enter a username and password.");
//                } else {
//                    boolean checkUser = DB.checkUsername(user);
//
//                    if (!checkUser) {
//                        boolean insert = DB.insertData(user, pass);
//                        if (insert) {
//                            textStatus.setText("Account created successfully.");
//                            Toast.makeText(MainActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
//                        } else {
//                            textStatus.setText("Account creation failed.");
//                        }
//                    } else {
//                        textStatus.setText("User already exists. Please log in.");
//                    }
//                }

                //Account creation
                controller.handleAccountCreation(user, pass);
            }
        });

        //Click listener for login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = usernameText.getText().toString();
                String pass = passwordText.getText().toString();

                //--------commented out and MOVED TO the LoginController.java file per MVC pattern--------

//                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
//                    textStatus.setText("Please enter a username and password.");
//                } else {
//                    boolean checkUserPass = DB.checkUsernamePassword(user, pass);
//
//                    if (checkUserPass) {
//                        textStatus.setText("Login successful.");
//                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
//
//                        //opens EventDataActivity
//                        Intent intent = new Intent(MainActivity.this, EventDataActivity.class);
//                        startActivity(intent);
//                    } else {
//                        textStatus.setText("Invalid username or password.");
//                    }
//                }

                //Login
                controller.handleLogin(user, pass);
            }
        });
    }

    @Override
    public void showStatusMessage(String message) {
        textStatus.setText(message);
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginSuccess() {
        // Handle successful navigation to the next screen
        Intent intent = new Intent(MainActivity.this, EventPlannerActivity.class);
        startActivity(intent);

    }
}