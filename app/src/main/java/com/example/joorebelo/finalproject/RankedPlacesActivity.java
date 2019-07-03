package com.example.joorebelo.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.joorebelo.finalproject.adapters.RankedPlacesAdapter;
import com.example.joorebelo.finalproject.model.RankedPlaces;

import java.util.ArrayList;

public class RankedPlacesActivity extends AppCompatActivity implements RankedPlacesAdapter.ItemClickListener {

    DBHelper dbHelper;
    SQLiteDatabase TourBuddyDB;
    ArrayList<RankedPlaces> rankedPlaces;
    RankedPlacesAdapter adapter;
    public static final String TBName_Places = "Places";
    public static final String TBName_Rankings = "Rankings";
    public static final String TBName_FavoritePlaces = "FavoritePlaces";
    private static String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranked_places);
        setTitle(R.string.title_activity_ranked);

        ////
        dbHelper = new DBHelper(this);
        rankedPlaces = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
        userEmail = sp.getString("UserEmail", "Data Missing");
        getRankedPlaces();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.ranked_places);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RankedPlacesAdapter(this, rankedPlaces);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onItemClick(View view, int position) {
        //int placeId, String placeName, String locationLink, String country, String city, String imageLink, String webLink, int rank, String description, boolean favorite
        RankedPlaces place = new RankedPlaces(adapter.getItem(position).getPlaceId(),
                adapter.getItem(position).getPlaceName(),
                adapter.getItem(position).getLocationLink(),
                adapter.getItem(position).getCountry(),
                adapter.getItem(position).getCity(),
                adapter.getItem(position).getImageLink(),
                adapter.getItem(position).getWebLink(),
                adapter.getItem(position).getRank(),
                adapter.getItem(position).getDescription(),
                adapter.getItem(position).isFavorite(),
                adapter.getItem(position).getVideoLink(),
                adapter.getItem(position).getCountRate()
        );
        Intent i = new Intent(this, PlaceActivity.class);
        i.putExtra("placeSelected", place);
        startActivity(i);
    }

    private void getRankedPlaces() {
        try {
            ArrayList<Integer> favoritesArray = new ArrayList<>();
            TourBuddyDB = dbHelper.getReadableDatabase();
            Cursor cursorf = TourBuddyDB.rawQuery(("SELECT f.placeId " +
                    "FROM " + TBName_FavoritePlaces +" f " +
                    "WHERE f.userEmail LIKE '"+ userEmail+"'"), null);

            while (cursorf.moveToNext()){
                int f = cursorf.getInt(cursorf.getColumnIndex("placeId"));

                favoritesArray.add(f);
            }


            Cursor cursor = TourBuddyDB.rawQuery(("SELECT p.placeId, p.placeName, p.locationLink, p.countryName, p.cityName, p.imageLink, p.webLink, p.description, p.videoLink, " +
                    "(SELECT AVG(r.rank) FROM "+ TBName_Rankings +" r WHERE r.placeId = p.placeId) AS ranking, " +
                    "(SELECT count(cr.rank) FROM "+ TBName_Rankings +" cr WHERE cr.placeId = p.placeId) AS countrate " +
                    "FROM " + TBName_Places +" p " +
                    "ORDER BY ranking DESC"), null);
            boolean isFavorite = false;
            while (cursor.moveToNext()){
                if (favoritesArray.contains(cursor.getInt(cursor.getColumnIndex("placeId")))){
                    isFavorite = true;
                }else{
                    isFavorite = false;
                }
                RankedPlaces p = new RankedPlaces(cursor.getInt(cursor.getColumnIndex("placeId")),
                        cursor.getString(cursor.getColumnIndex("placeName")),
                        cursor.getString(cursor.getColumnIndex("locationLink")),
                        cursor.getString(cursor.getColumnIndex("countryName")),
                        cursor.getString(cursor.getColumnIndex("cityName")),
                        cursor.getString(cursor.getColumnIndex("imageLink")),
                        cursor.getString(cursor.getColumnIndex("webLink")),
                        (int)cursor.getFloat(cursor.getColumnIndex("ranking")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        isFavorite,
                        cursor.getString(cursor.getColumnIndex("videoLink")),
                        cursor.getInt(cursor.getColumnIndex("countrate")));

                rankedPlaces.add(p);
            }

            //for debug
            /*
            for (int i = 0; i < places.size(); i++) {
                Log.d("msg", "Place: " + places.get(i).getPlaceName()
                        + " \nCountry: " + places.get(i).getCountry()
                        + " \nCity: " + places.get(i).getCity()
                        + " \nRank: " + places.get(i).getRank() + "\n\n");
            }*/

        }catch (Exception e){
            Log.e("SignUpActivity", e.getMessage());
        }finally{
            TourBuddyDB.close();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("msg", "onResume ");

        /* //old code
        SharedPreferences sp = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
        userEmail = sp.getString("UserEmail", "Data Missing");
        int selectedFav = sp.getInt("SelectedFav", -1);
        boolean selectedFavValue = sp.getBoolean("SelectedFavValue", false);

        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.remove("SelectedFav");
        spEditor.remove("SelectedFavValue");
        spEditor.apply();

        for (int i = 0; i < rankedPlaces.size(); i++) {
            if (rankedPlaces.get(i).getPlaceId()==selectedFav){
                rankedPlaces.get(i).setFavorite(selectedFavValue);
            }
        }
        */

        //update layout
        rankedPlaces.clear();
        getRankedPlaces();

        adapter.notifyDataSetChanged();
    }
}
