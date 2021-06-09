package com.example.wasteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wasteit.POJOclasses.PickUpDetails;
import com.example.wasteit.POJOclasses.ProductDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  {

    private EditText date , time;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private ImageButton openProfile , navigation_menu;
    private Button dispose;
    private LinearLayout linearLayout , storeContainer;
    private String dateValue , timeValue ;
    private CustomAlertDialog customAlertDialog;
    private AlertDialog alertDialog;
    private PopupMenu popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){}

        dateValue = "";
        timeValue = "";



        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        disableEditext(date);
        disableEditext(time);

        navigation_menu = findViewById(R.id.burgerMenu);

        customAlertDialog = new CustomAlertDialog(MainActivity.this);
        alertDialog = customAlertDialog.getDialog();

        openProfile = findViewById(R.id.profileMenu);
        dispose = findViewById(R.id.dispose);

        linearLayout = (LinearLayout) findViewById(R.id.layoutContainer);
        storeContainer = (LinearLayout) findViewById(R.id.storeContainer);



        navigation_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopup(v);


            }
        });


        dispose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dateValue.equals("") || timeValue.equals("")){

                    Toast.makeText(getApplicationContext() , "Please select date and time" , Toast.LENGTH_LONG).show();

                }else
                {
                    showAlertDialog();
                }


            }
        });


        openProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this , Profile.class);
                startActivity(intent);

            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        getProducts();
    }

    private void showPopup(View v) {

        popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_dorp_down, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.AboutUs:
                        Intent goToAboutUs = new Intent(MainActivity.this , AboutUs.class);
                        startActivity(goToAboutUs);
                        return true;
                    case R.id.Store:
                        Toast.makeText(getApplicationContext() , "store" , Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.logout:
                    {
                        FirebaseAuth.getInstance().signOut();
                        Intent goToLogin = new Intent(MainActivity.this , Login.class);
                        startActivity(goToLogin);
                        finish();

                        return true;
                    }

                }
                return true;
            }
        });

    }

    private void showAlertDialog(){

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("Are You Sure ? ");
        builder.setCancelable(false);

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               RegisterPickUp();


            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    
    private void RegisterPickUp(){

        PickUpDetails pickUpDetails = new PickUpDetails();
        pickUpDetails.setDate(dateValue);
        pickUpDetails.setTime(timeValue);
        pickUpDetails.setUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
        alertDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("PickUps");
        databaseReference.push().setValue(pickUpDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                        setDisposeStatus(dateValue , timeValue);
                        alertDialog.dismiss();
                        Toast.makeText(getApplicationContext() , "PickUp Registered" , Toast.LENGTH_LONG).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                alertDialog.dismiss();
                Toast.makeText(getApplicationContext() , "Fail to Register "+e , Toast.LENGTH_LONG).show();

            }
        });
        
        
    }

    private void getProducts(){


        DatabaseReference productReference = FirebaseDatabase.getInstance().getReference().child("Products");
        alertDialog.show();
        productReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()){

                     for (DataSnapshot dataSnapshot : task.getResult().getChildren()){

                        ProductDetails productDetails = dataSnapshot.getValue(ProductDetails.class);
                        setProduct(productDetails);

                    }

                }
                alertDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext() , "Failed to fetch Products : " + e , Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }
        });

    }
    

    private void setDisposeStatus(String date , String time){

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View to_add = inflater.inflate(R.layout.booking_status, linearLayout ,false);
        TextView text = to_add.findViewById(R.id.bookedStatus);

        text.setText(date + " , " + time);

        linearLayout.addView(to_add);


    }

    public void setProduct(ProductDetails productDetails){

        LayoutInflater inflater1 = LayoutInflater.from(getApplicationContext());
        View v = inflater1.inflate(R.layout.product_layout, storeContainer ,false);

        TextView productName =  v.findViewById(R.id.productName);
        TextView productDescription =  v.findViewById(R.id.productDescription);
        TextView productPrice =  v.findViewById(R.id.productPrice);
        TextView productPoints =  v.findViewById(R.id.productPoints);
        ImageView produceImage = v.findViewById(R.id.productImage);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getApplicationContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Glide.with(getApplicationContext()).load(productDetails.getImage()).placeholder(circularProgressDrawable).into(produceImage);

        
        productName.setText(productDetails.getName());
        productDescription.setText(productDetails.getDescription());
        productPrice.setText(productDetails.getPrice());
        productPoints.setText(productDetails.getPoints());

        storeContainer.addView(v);

    }

    public void showTimePickerDialog(){

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        time.setText(hourOfDay + ":" + minute);
                        timeValue = hourOfDay + " : " + minute;
                    }
                }, mHour, mMinute, false);


        timePickerDialog.show();
    }

    public void showDatePickerDialog() {

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);



        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                        date.setText(dayOfMonth + " / " + (monthOfYear+1) + " / " + year);

                        dateValue = dayOfMonth + " " + monthNames[monthOfYear+1]  + " " + year;

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    private EditText disableEditext(EditText editText){

        editText.setTag(editText.getKeyListener());
        editText.setKeyListener(null);
        return editText;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dorp_down, menu);
        return true;
    }


}