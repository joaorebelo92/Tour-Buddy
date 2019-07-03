package com.example.joorebelo.finalproject;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegister;
    EditText edtName, edtPassword, edtPhone, edtEmail;
    TextView txtDOB;

    DBHelper dbHelper;
    SQLiteDatabase TourBuddyDB;
    public static final String TBName_User = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle(R.string.title_activity_Register);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        txtDOB = findViewById(R.id.txtDOB);
        txtDOB.setOnClickListener(this);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == btnRegister.getId()){
            /*
            String data = edtName.getText().toString()+"\n" + edtPhone.getText().toString() +
                    "\n" + edtEmail.getText().toString() + "\n" + edtPassword.getText().toString();
            Toast.makeText(this, data, Toast.LENGTH_LONG).show();
             */
            insertData();

        }else if(view.getId() == txtDOB.getId()){
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, datePickerListener, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            String DOB = String.valueOf(month+1) + "/" + String.valueOf(day) + "/" + String.valueOf(year);
            txtDOB.setText(DOB);
        }
    };

    private void insertData(){
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String dob = txtDOB.getText().toString();
        String regexEmail = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:"
                + "[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\"
                + "x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*"
                + "[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]"
                + "|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x"
                + "53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";


        if (name.equals("") || phone.equals("") || email.equals("") || password.equals("") || dob.equals("")){
            Toast.makeText(getApplicationContext(), R.string.strFieldsMustBeFilled, Toast.LENGTH_LONG).show();
        }else if(!email.matches(regexEmail)) {
            Toast.makeText(getApplicationContext(), R.string.strEnterValidEmail, Toast.LENGTH_LONG).show();
        }else{
            //verify if email is on database
            if(verifyEmail(email)<1){
                ContentValues cv = new ContentValues();
                cv.put("email", email);
                cv.put("password", password);
                cv.put("name", name);
                cv.put("phone", phone);
                cv.put("dob", dob);
                cv.put("role", "User");

                try {
                    TourBuddyDB = dbHelper.getWritableDatabase();
                    TourBuddyDB.insert(TBName_User, null, cv);
                    Log.v("SignUpActivity", "Account Created");
                    Toast.makeText(getApplicationContext(), R.string.strAccountCreated, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, LoginActivity.class));
                }catch (Exception e){
                    Log.e("SignUpActivity", e.getMessage());
                }finally {
                    TourBuddyDB.close();
                }
            }else{
                Toast.makeText(getApplicationContext(), R.string.strEmailAlreadyExists, Toast.LENGTH_LONG).show();
            }
        }
    }

    private int verifyEmail(String email) {
        int count = 0;
        try {
            TourBuddyDB = dbHelper.getWritableDatabase();


            TourBuddyDB = dbHelper.getReadableDatabase();
            Cursor cursorf = TourBuddyDB.rawQuery(("SELECT email " +
                    "FROM " + TBName_User +
                    " WHERE email LIKE '"+ email +"'"), null);
            count = cursorf.getCount();
            /*
            while (cursorf.moveToNext()){
                if(cursorf.getString(cursorf.getColumnIndex("rank")).equals(email)){
                    count++;
                }
            }*/
            Log.v("SignUpActivity", "Account Created");
        }catch (Exception e){
            Log.e("SignUpActivity", e.getMessage());
        }finally {
            TourBuddyDB.close();
        }
        return count;
    }
}
