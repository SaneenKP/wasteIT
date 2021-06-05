package com.example.wasteit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){}


    }
}