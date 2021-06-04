package com.example.wasteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText email , password;
    private Button login , register;
    private String eml , pass;
    private FirebaseAuth auth;

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

        auth = FirebaseAuth.getInstance();
        
        auth.signInWithEmailAndPassword(email , pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                     Intent openHome = new Intent(Login.this , MainActivity.class);
                    startActivity(openHome);
                    finish();

                }else
                {
                    Toast.makeText(getApplicationContext() , "Login Failed due to  : " + task.getException() , Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}