package com.example.joorebelo.finalproject;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnUpdate;
    EditText edtName, edtPassword, edtPhone, edtEmail;
    TextView txtDOB;

    DBHelper dbHelper;
    SQLiteDatabase TourBuddyDB;
    public static final String TBName_User = "User";

    private static String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(R.string.title_activity_profile);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        txtDOB = findViewById(R.id.txtDOB);
        txtDOB.setOnClickListener(this);

        dbHelper = new DBHelper(this);

        SharedPreferences sp = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
        userEmail = sp.getString("UserEmail", "Data Missing");

        fillProfileFromDatabase();

        Log.v("email", userEmail);
    }

    private void fillProfileFromDatabase() {
        try {

            TourBuddyDB = dbHelper.getReadableDatabase();
            Cursor cursor = TourBuddyDB.rawQuery(("SELECT email, phone, password, name, dob " +
                    "FROM "+ TBName_User +" WHERE email LIKE '"+ userEmail +"'"), null);
            while (cursor.moveToNext()){
                edtName.setText(cursor.getString(cursor.getColumnIndex("name")));
                edtPhone.setText(cursor.getString(cursor.getColumnIndex("phone")));
                edtEmail.setText(cursor.getString(cursor.getColumnIndex("email")));
                edtPassword.setText(cursor.getString(cursor.getColumnIndex("password")));

                txtDOB.setText(cursor.getString(cursor.getColumnIndex("dob")));
                break;

            }

        }catch (Exception e){
            Log.e("ProfileActivity", e.getMessage());
        }finally{
            TourBuddyDB.close();
        }
    }

    DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            String DOB = String.valueOf(month+1) + "/" + String.valueOf(day) + "/" + String.valueOf(year);
            txtDOB.setText(DOB);
        }
    };

    @Override
    public void onClick(View view) {
        if(view.getId() == btnUpdate.getId()){
            updateData();

        }else if(view.getId() == txtDOB.getId()){
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, datePickerListener, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private void updateData(){
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String dob = txtDOB.getText().toString();



        if (name.equals("") || phone.equals("") || password.equals("") || dob.equals("")){
            Toast.makeText(getApplicationContext(), R.string.strFieldsMustBeFilled, Toast.LENGTH_SHORT).show();
        }else{
            ContentValues cv = new ContentValues();
            cv.put("password", password);
            cv.put("name", name);
            cv.put("phone", phone);
            cv.put("dob", dob);

            try {
                TourBuddyDB = dbHelper.getWritableDatabase();
                TourBuddyDB.update(TBName_User, cv,"email LIKE '"+email + "'", null);
                Log.v("ProfileActivity", "Account Updated");
                Toast.makeText(getApplicationContext(), R.string.strYourProfileWasUpdated, Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.e("ProfileActivity", e.getMessage());
            }finally {
                TourBuddyDB.close();
            }
        }
    }

}
