package com.example.wasteit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    private EditText email , password;
    private Button login , register;
    private String eml , pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Method to remove Action Bar
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){}


        //Attaching Variables with Layout Views
        email = findViewById(R.id.loginEmail);
        password  = findViewById(R.id.loginPassword);
        login = findViewById(R.id.loginbtn);
        register = findViewById(R.id.registerbtn);

        // click Listener to Button login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eml = email.getText().toString();
                pass = password.getText().toString();

                //Calls login method using email and password
                login(eml , pass);


            }
        });

        // click Listener to Button register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });

    }

    private void login(String email , String pass){

    }

   
}