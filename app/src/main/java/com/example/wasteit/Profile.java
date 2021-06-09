package com.example.wasteit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wasteit.POJOclasses.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

public class Profile extends AppCompatActivity implements View.OnFocusChangeListener, View.OnLongClickListener {

    private ImageView profileImage;
    private TextView points;
    private EditText name , address , houseNo , email;
    private String value;
    private Uri resultUri;
    private CustomAlertDialog customAlertDialog;
    private android.app.AlertDialog dialog;
    private String userId;


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
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        customAlertDialog = new CustomAlertDialog(Profile.this);
        dialog = customAlertDialog.getDialog();

        name = disableEditText(name);
        address= disableEditText(address);
        houseNo = disableEditText(houseNo);
        email = disableEditText(email );


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

        getUserDetails();

    }

    private EditText setListeners(EditText editText){

        editText.setOnFocusChangeListener(this);
        editText.setOnFocusChangeListener(this);
        editText.setOnLongClickListener(this);
        return editText;

    }

    private EditText disableEditText(EditText editText){

        editText.setTag(editText.getKeyListener());
        editText.setKeyListener(null);
        return editText;

    }


    private void setProfileImage(Uri uri){

        dialog.show();
        DatabaseReference userDetailsReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        StorageReference profileStorageReference = FirebaseStorage.getInstance().getReference().child("Users/"+ userId);
        profileStorageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){


                    profileStorageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()){

                                userDetailsReference.child("coverPic").setValue(task.getResult().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getApplicationContext());
                                        circularProgressDrawable.setStrokeWidth(5f);
                                        circularProgressDrawable.setCenterRadius(30f);
                                        circularProgressDrawable.start();

                                        Glide.with(getApplicationContext()).load(task.getResult()).placeholder(circularProgressDrawable).into(profileImage);
                                        dialog.dismiss();
                                    }
                                });

                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Task Failed : "+ e , Toast.LENGTH_LONG).show();


                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Task Failed : "+ e , Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getUserDetails(){

        dialog.show();
        DatabaseReference getUserDetailsReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        getUserDetailsReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                Log.d("user details" , task.getResult().toString());
                UserDetails userDetails = task.getResult().getValue(UserDetails.class);
                name.setText(userDetails.getName());
                address.setText(userDetails.getAddress());
                email.setText(userDetails.getEmail());
                houseNo.setText(userDetails.getHouseNo());


                if (userDetails.getCoverPic()!=null){
                    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getApplicationContext());
                    circularProgressDrawable.setStrokeWidth(5f);
                    circularProgressDrawable.setCenterRadius(30f);
                    circularProgressDrawable.start();
                    Glide.with(getApplicationContext()).load(userDetails.getCoverPic()).placeholder(circularProgressDrawable).into(profileImage);

                }

                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext() , "Data could not be fetched" , Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

    }

    private void changeUserDetails(String child , String value){

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child(child);
        dialog.show();
        userReference.setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    dialog.dismiss();
                    Toast.makeText(getApplicationContext() , "Value Succesfully Changed" , Toast.LENGTH_LONG).show();


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                dialog.dismiss();
                Toast.makeText(getApplicationContext() , "Failed : "+e , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showAlertDialog(String hint , String value){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure to change " + hint +" ? ");
        builder.setCancelable(false);

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               changeUserDetails(hint , value);


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

            disableEditText(editText);
            if (!value.equals(editText.getText().toString())){
                showAlertDialog(editText.getHint().toString() , editText.getText().toString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        CropImage.ActivityResult result = CropImage.getActivityResult(data);

        if (resultCode == RESULT_OK){
            resultUri = Objects.requireNonNull(result).getUri();
            setProfileImage(resultUri);
        }
    }


}