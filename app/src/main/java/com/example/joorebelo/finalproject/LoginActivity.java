package com.example.joorebelo.finalproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joorebelo.finalproject.model.LocaleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLogin, btnRegister;
    EditText edtEmail, edtPassword;
    DBHelper dbHelper;
    SQLiteDatabase TourBuddyDB;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.context = this;

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        dbHelper = new DBHelper(this);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //translation onCreate
        SharedPreferences preferences = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
        String lang = preferences.getString("LANG", "");
        boolean langSelected = preferences.getBoolean("langSelected", false);
        SharedPreferences.Editor editor = preferences.edit();
        if (langSelected) {
            editor.clear();
            editor.putString("LANG", lang);
            editor.putBoolean("langSelected", true);
            editor.apply();
            LocaleUtils.updateConfig(this,lang);
        } else {
            LocaleUtils.updateConfig(this, Locale.getDefault().getLanguage());
            editor.clear();
            editor.putString("lang", Locale.getDefault().getLanguage());
            editor.putBoolean("langSelected", false);
            editor.apply();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {
            showChangeLangDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.language_dialog, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinner1 = (Spinner) dialogView.findViewById(R.id.spinner1);

        dialogBuilder.setTitle("Language Selector");
        dialogBuilder.setMessage("Change the app Language");
        dialogBuilder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int langpos = spinner1.getSelectedItemPosition();
                switch(langpos) {
                    case 0: //English
                        SharedPreferences sp = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("LANG", "en");
                        edit.commit();
                        changeLocal("en");
                        LoginActivity.this.recreate();

                        return;
                    case 1: //Portuguese
                        SharedPreferences sp2 = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit2 = sp2.edit();
                        edit2.putString("LANG", "pt");
                        edit2.commit();
                        //setLangRecreate("pt");
                        changeLocal("pt");
                        LoginActivity.this.recreate();

                        return;
                    default: //By default ( English )
                        SharedPreferences sp3 = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit3 = sp3.edit();
                        edit3.putString("LANG", "en");
                        edit3.commit();
                        //setLangRecreate("en");
                        changeLocal("en");
                        LoginActivity.this.recreate();

                        return;
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


    public static ContextWrapper changeLang(Context context, String lang_code){
        Locale sysLocale;

        Resources rs = context.getResources();
        Configuration config = rs.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = config.getLocales().get(0);
        } else {
            sysLocale = config.locale;
        }
        if (!lang_code.equals("") && !sysLocale.getLanguage().equals(lang_code)) {

            Locale locale = new Locale(lang_code);
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale);
            } else {
                config.locale = locale;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context = context.createConfigurationContext(config);
            } else {
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }
        }
        return new ContextWrapper(context);
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }

    public void changeLocal(String lang){
        SharedPreferences preferences = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString("LANG", lang);
                editor.putBoolean("langSelected", true);
        editor.apply();
        LocaleUtils.updateConfig(this,lang);
        Intent intent = this.getIntent();
        this.overridePendingTransition(0, 0);
        this.finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.overridePendingTransition(0, 0);
        this.startActivity(intent);
    }

    public void setLangRecreate(String langval) {
        /* //old code
        Toast.makeText(context, "Lang- " + langval , Toast.LENGTH_LONG).show();
        Locale locale;
        Configuration config = getBaseContext().getResources().getConfiguration();
        locale = new Locale(langval);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        */
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnLogin.getId()){
            String username = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            if (verifyLogin()){
                finish();
                Toast.makeText(this, R.string.strLoginSucessful, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,HomeActivity.class));
            }else{
                Toast.makeText(this, R.string.strInvalidEmailOrPassword, Toast.LENGTH_SHORT).show();
            }
        }else if(view.getId() == btnRegister.getId()){
            Intent signUpIntent = new Intent(this, SignUpActivity.class);

            startActivity(signUpIntent);

        }
    }

    private boolean verifyLogin() {
        try {

            TourBuddyDB = dbHelper.getReadableDatabase();
            String columns[] = {"email", "password"};
            String userData[] = {edtEmail.getText().toString(), edtPassword.getText().toString()};

            Cursor cursor = TourBuddyDB.query("User", columns, "email = ? AND password = ?", userData,null, null,null);
            if(cursor != null){
                if(cursor.getCount() > 0){
                    //save to SharedPreferences
                    SharedPreferences sp = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("UserEmail", edtEmail.getText().toString());
                    edit.commit();

                    return true;
                }
            }
            return false;


        }catch (Exception e){
            Log.e("LoginActivity", e.getMessage());
            return false;
        }finally {
            TourBuddyDB.close();
        }
    }
}

