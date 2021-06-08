package com.example.wasteit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText date , time;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private ImageButton openProfile;
    private Button dispose;
    private LinearLayout linearLayout , storeContainer;
    private String dateValue , timeValue ;

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

        openProfile = findViewById(R.id.profileMenu);
        dispose = findViewById(R.id.dispose);

        linearLayout = (LinearLayout) findViewById(R.id.layoutContainer);
        storeContainer = (LinearLayout) findViewById(R.id.storeContainer);

        setProduct("saneen" , "250" , "waste" , "25");
        setProduct("saneen" , "250" , "waste" , "25");
        setProduct("saneen" , "250" , "waste" , "25");
        setProduct("saneen" , "250" , "waste" , "25");



        dispose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dateValue.equals("") || timeValue.equals("")){

                    Toast.makeText(getApplicationContext() , "Please select date and time" , Toast.LENGTH_LONG).show();

                }else
                {
                    setDisposeStatus(dateValue , timeValue);
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
    }

    private void setDisposeStatus(String date , String time){

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View to_add = inflater.inflate(R.layout.booking_status, linearLayout ,false);
        TextView text = to_add.findViewById(R.id.bookedStatus);

        text.setText(date + " , " + time);

        linearLayout.addView(to_add);


    }

    public void setProduct(String name , String price , String description , String points){

        LayoutInflater inflater1 = LayoutInflater.from(getApplicationContext());
        View v = inflater1.inflate(R.layout.product_layout, storeContainer ,false);

        TextView productName =  v.findViewById(R.id.productName);
        TextView productDescription =  v.findViewById(R.id.productDescription);
        TextView productPrice =  v.findViewById(R.id.productPrice);
        TextView productPoints =  v.findViewById(R.id.productPoints);
        ImageView produceImage = v.findViewById(R.id.productImage);
        Button buyProduct = v.findViewById(R.id.buyProduct);

        productName.setText(name);
        productDescription.setText(description);
        productPrice.setText(price);
        productPoints.setText(points);


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

}