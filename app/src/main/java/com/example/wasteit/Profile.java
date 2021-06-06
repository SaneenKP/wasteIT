package com.example.wasteit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity implements View.OnFocusChangeListener, View.OnLongClickListener {

    private ImageView profileImage;
    private TextView points;
    private EditText name , address , houseNo , email;
    private String value;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){}

        name = findViewById(R.id.profileName);
        address = findViewById(R.id.profileAddress);
        houseNo = findViewById(R.id.profileHouseNo);
        email = findViewById(R.id.profileEmail);
        profileImage = findViewById(R.id.profile_image);
        points = findViewById(R.id.points);

        name = disableEditext(name);
        address= disableEditext(address);
        houseNo = disableEditext(houseNo);
        email = disableEditext(email );


       name = setListeners(name);
       houseNo = setListeners(houseNo);
       address = setListeners(address);
       email = setListeners(email);

       profileImage.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {


               CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                       .setAspectRatio(1, 1)
                       .start(Profile.this);

               return true;

           }
       });



    }

    private EditText setListeners(EditText editText){

        editText.setOnFocusChangeListener(this);
        editText.setOnFocusChangeListener(this);
        editText.setOnLongClickListener(this);
        return editText;

    }

    private EditText disableEditext(EditText editText){

        editText.setTag(editText.getKeyListener());
        editText.setKeyListener(null);
        return editText;

    }


    private void setProfileImage(Uri uri){
        Glide.with(getApplicationContext()).load(uri).into(profileImage);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        CropImage.ActivityResult result = CropImage.getActivityResult(data);

        if (resultCode == RESULT_OK){
            resultUri = Objects.requireNonNull(result).getUri();
            setProfileImage(resultUri);
        }
    }

    private void showAlertDialog(String value){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure to change " + value +" ? ");
        builder.setCancelable(false);

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        EditText editText = findViewById(v.getId());

        if(hasFocus) {
             value = editText.getText().toString();
        }else{
            editText.setTextColor(getResources().getColor(R.color.nonClickable));
            ViewCompat.setBackgroundTintList(
                    editText,
                    ContextCompat.getColorStateList(
                            getApplicationContext(),
                            R.color.white_transperent
                    )
            );

            disableEditext(editText);
            if (!value.equals(editText.getText().toString())){
                showAlertDialog(editText.getHint().toString());
            }

        }

    }



    @Override
    public boolean onLongClick(View v) {

        EditText editText = findViewById(v.getId());

        editText.setKeyListener((KeyListener)editText.getTag());
        editText.setTextColor(getResources().getColor(R.color.black));

        ViewCompat.setBackgroundTintList(
                editText,
                ContextCompat.getColorStateList(
                        getApplicationContext(),
                        R.color.apptheme
                )
        );



        return true;
    }


}