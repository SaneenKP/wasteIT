package com.example.wasteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private EditText email , password;
    private Button login , register;
    private FirebaseAuth auth;
    private CustomAlertDialog customAlertDialog;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Method to remove Action Bar
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){}

        if (FirebaseAuth.getInstance().getCurrentUser() !=null){

            Intent intent = new Intent(Login.this , MainActivity.class);
            startActivity(intent);
            finish();
        }

        customAlertDialog = new CustomAlertDialog(Login.this);
        dialog = customAlertDialog.getDialog();

        //Attaching Variables with Layout Views
        email = findViewById(R.id.loginEmail);
        password  = findViewById(R.id.loginPassword);
        login = findViewById(R.id.loginbtn);
        register = findViewById(R.id.registerbtn);

        // click Listener to Button login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkFields() && checkEmail(email.getText().toString()) && checkPassword(password.getText().toString())){

                    //Calls login method using email and password
                    login(email.getText().toString() , password.getText().toString());

                }


            }
        });

        // click Listener to Button register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openRegister = new Intent(Login.this , Register.class);
                startActivity(openRegister);


            }
        });

    }

    //Method to check password text validity
    private boolean checkPassword(String password){

        String pattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$";
        Matcher patternMatcher = Pattern.compile(pattern).matcher(password);

        if (!patternMatcher.matches()){
            this.password.setError(" Must Contain Minimum six characters, at least one letter, one number and one special character");
        }
        return patternMatcher.matches();
    }


    //Method to check email text validity
    private boolean checkEmail(String email){

        String pattern = "[A-Z a-z 0-9]+@[0-9 A-Z a-z]+.com";

        Pattern pattern1 = Pattern.compile(pattern);
        Matcher matcher = pattern1.matcher(email);
        return matcher.matches();
    }

    //Method to check all the fields if they are empty or not
    private boolean checkFields(){

        boolean status = false;

        if (TextUtils.isEmpty(email.getText())){
            email.setError("email field cannot be empty");
            status = false;
        }else if(TextUtils.isEmpty(password.getText())){
            password.setError("Password field cannot be empty");
            status = false;
        }else{status = true;}

        return status;
    }


    //Method to initiate login using email and password
    private void login(String email , String pass){

        auth = FirebaseAuth.getInstance();
        dialog.show();
        auth.signInWithEmailAndPassword(email , pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    dialog.dismiss();
                     Intent openHome = new Intent(Login.this , MainActivity.class);
                    startActivity(openHome);
                    finish();

                }else
                {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext() , "Login Failed due to  : " + task.getException() , Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}