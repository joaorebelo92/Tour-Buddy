package com.example.joorebelo.finalproject;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class SupportActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSubmit, btnCall, btnSMS, btnEmail;
    TextView txtSupportTitle, txtSupportTitle2, txtFeedBackTitle;
    EditText edtSubject, edtText;
    DBHelper dbHelper;
    SQLiteDatabase TourBuddyDB;

    private static String userEmail;
    public static final String TBName_Support = "Support";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        setTitle(R.string.title_activity_support);

        dbHelper = new DBHelper(this);
        SharedPreferences sp = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
        userEmail = sp.getString("UserEmail", "Data Missing");

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        edtSubject = findViewById(R.id.edtSubject);
        edtText = findViewById(R.id.edtText);



        btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(this);

        btnSMS = findViewById(R.id.btnSMS);
        btnSMS.setOnClickListener(this);

        btnEmail = findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCall:
                makeCall();
                break;
            case R.id.btnSMS:
                sendSMS();
                break;
            case R.id.btnEmail:
                sendEmail();
                break;
            case R.id.btnSubmit:
                if(edtSubject.getText().toString().equals("") || edtText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), R.string.strFieldsMustBeFilled, Toast.LENGTH_LONG).show();
                }else{
                    insertData();
                    startActivity(new Intent(this, HomeActivity.class));
                }
                break;
        }
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"joao.rebelo92@gmail.com", "706998@cestarcollege.com"});

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.strSupportEmail));
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.strSupportBodyMessage));

        emailIntent.setType("*/*");

        startActivity(Intent.createChooser(emailIntent, getString(R.string.strSelectEmailAccount)));
    }

    private void sendSMS() {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:1234567890"));
        smsIntent.putExtra("sms_body", getString(R.string.strSMSBodyMessage));
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), R.string.strSMSPermissionDenied, Toast.LENGTH_LONG).show();
            return;
        }
        startActivity(smsIntent);
    }

    private void makeCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:1234567890"));

        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), R.string.strCallPermissionDenied, Toast.LENGTH_LONG).show();
            return;
        }

        startActivity(callIntent);
    }

    private void insertData(){
        String subject = edtSubject.getText().toString();
        String test = edtText.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(new Date());
        //Toast.makeText(getApplicationContext(), "date: " + date, Toast.LENGTH_LONG).show();

        ContentValues cv = new ContentValues();
        cv.put("subject", subject);
        cv.put("message", test);
        cv.put("date", date);
        cv.put("userEmail", userEmail);

        try {
            TourBuddyDB = dbHelper.getWritableDatabase();
            TourBuddyDB.insert(TBName_Support, null, cv);
            Log.v(TBName_Support, "FeedBack Created");
            Toast.makeText(getApplicationContext(), R.string.strFeedBackSended, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e("SupportActivity", e.getMessage());
        }finally {
            TourBuddyDB.close();
        }


    }
}
