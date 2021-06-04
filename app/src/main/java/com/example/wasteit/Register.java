package com.example.wasteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wasteit.POJOclasses.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {


    private Button register;
    private EditText name , address , houseNo , email , password , confirmPassword;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private CustomAlertDialog customAlertDialog;
    private AlertDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Method to remove Action Bar
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){}

        customAlertDialog = new CustomAlertDialog(Register.this);
        dialog = customAlertDialog.getDialog();
        register = findViewById(R.id.registerbtn);
        name = findViewById(R.id.registerName);
        address = findViewById(R.id.registerAddress);
        houseNo = findViewById(R.id.registerHouseNo);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.registerConfirmPassword);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkFields() && checkEmail(email.getText().toString()) && checkPassword(password.getText().toString())){

                    if (password.getText().toString().equals(confirmPassword.getText().toString())) {

                        UserDetails userDetails = new UserDetails();
                        userDetails.setName(name.getText().toString());
                        userDetails.setAddress(address.getText().toString());
                        userDetails.setEmail(email.getText().toString());
                        userDetails.setHouseNo(houseNo.getText().toString());
                        register(email.getText().toString() , password.getText().toString() , userDetails);

                    }else{
                        Toast.makeText(getApplicationContext() , "Password Does Not Match" , Toast.LENGTH_LONG).show();
                    }

                }

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

    //Methods to check all the fields if they are empty or not
    private boolean checkFields(){

        boolean status = false;

        if (TextUtils.isEmpty(email.getText())){
            email.setError("email field cannot be empty");
            status = false;
        }else if(TextUtils.isEmpty(name.getText())){
            name.setError("name field cannot be empty");
            status = false;
        }else if(TextUtils.isEmpty(address.getText())){
            address.setError("Address field cannot be empty");
            status = false;
        }else if(TextUtils.isEmpty(houseNo.getText())){
            houseNo.setError("House No is crucial and cannot be empty");
            status = false;
        }else if(TextUtils.isEmpty(password.getText())){
            password.setError("Password field cannot be empty");
            status = false;
        }else if(TextUtils.isEmpty(confirmPassword.getText())){
            confirmPassword.setError(" field cannot be empty");
            status = false;
        }else{status = true;}

        return status;
    }

    private void register(String email , String password , UserDetails userDetails){

        firebaseAuth = FirebaseAuth.getInstance();

        dialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                dialog.dismiss();
                                Toast.makeText(getApplicationContext() , "Registration Complete" , Toast.LENGTH_LONG).show();

                                /*Intent openHome = new Intent();
                                startActivity(openHome);*/

                            }else
                            {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext() , "Registration Failed : "+task.getException() , Toast.LENGTH_LONG).show();

                            }

                        }
                    });


                }
                else
                {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext() , "Registration Failed : "+task.getException() , Toast.LENGTH_LONG).show();

                }

            }
        });

    }
}