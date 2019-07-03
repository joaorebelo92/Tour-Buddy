package com.example.joorebelo.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joorebelo.finalproject.model.RankedPlaces;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String userEmail;
    private String userName;

    TextView txtUserName, txtUserEmail;
    private View navHeader;
    Context context;

    DBHelper dbHelper;
    SQLiteDatabase TourBuddyDB;
    public static final String TBName_User = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dbHelper = new DBHelper(this);
        //get user profile from database
        getProfile();

        navHeader = navigationView.getHeaderView(0);

        txtUserName = (TextView) navHeader.findViewById(R.id.txtUserName);
        txtUserEmail = (TextView) navHeader.findViewById(R.id.txtUserEmail);

       txtUserEmail.setText(userEmail);
       txtUserName.setText(userName);
    }

    private void getProfile() {
        try {

            TourBuddyDB = dbHelper.getReadableDatabase();
            SharedPreferences sp = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
            userEmail = sp.getString("UserEmail", "Data Missing");

            Cursor cursor = TourBuddyDB.rawQuery(("SELECT email, password, name, phone, dob " +
                    "FROM "+ TBName_User +" WHERE email LIKE '"+ userEmail +"'"), null);
            SharedPreferences.Editor edit = sp.edit();
            while (cursor.moveToNext()){
                edit.putString("UserPassword", cursor.getString(cursor.getColumnIndex("password")));
                edit.putString("UserName", cursor.getString(cursor.getColumnIndex("name")));
                userName = cursor.getString(cursor.getColumnIndex("name"));
                edit.putString("UserPhone", cursor.getString(cursor.getColumnIndex("phone")));
                edit.putString("UserDOB", cursor.getString(cursor.getColumnIndex("dob")));
                edit.commit();
                break;
            }
        }catch (Exception e){
            Log.e("HomeActivity", e.getMessage());
        }finally{
            TourBuddyDB.close();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();

            //perform a logout and open login activity
            SharedPreferences sp = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
            SharedPreferences.Editor spEditor = sp.edit();
            spEditor.remove("UserEmail");
            spEditor.apply();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_place_ranking) {
            startActivity(new Intent(this,RankedPlacesActivity.class));
        } else if (id == R.id.nav_favorite_places) {
            startActivity(new Intent(this, FavoritesActivity.class));
        } else if (id == R.id.nav_search_places) {
            startActivity(new Intent(this, SearchPlaceActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.nav_support) {
            startActivity(new Intent(this, SupportActivity.class));
        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, R.string.strLogOut, Toast.LENGTH_SHORT).show();

            //perform a logout and open login activity
            SharedPreferences sp = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
            SharedPreferences.Editor spEditor = sp.edit();
            spEditor.remove("UserEmail");
            spEditor.apply();

            finishAffinity();
            startActivity(new Intent(this, LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
