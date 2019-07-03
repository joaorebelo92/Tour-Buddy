package com.example.joorebelo.finalproject;

import android.content.ContentValues;
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

import com.example.joorebelo.finalproject.adapters.PlacesAdapter;
import com.example.joorebelo.finalproject.model.RankedPlaces;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity implements PlacesAdapter.ItemClickListener {

    static DBHelper dbHelper;
    static SQLiteDatabase TourBuddyDB;
    public static final String TBName_Places = "Places";
    public static final String TBName_Rankings = "Rankings";
    public static final String TBName_FavoritePlaces = "FavoritePlaces";
    private static String userEmail;
    private static Context context;

    static ArrayList<RankedPlaces> places;
    PlacesAdapter selectedPlacesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        setTitle(R.string.title_activity_favorites);

        dbHelper = new DBHelper(this);

        //get user from SharedPreferences
        SharedPreferences sp = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
        userEmail = sp.getString("UserEmail", "Data Missing");
        places = new ArrayList<>();

        //getPlaces from database
        getPlaces();
        context = this;

        //RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView_places);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedPlacesAdapter = new PlacesAdapter(this, places, "favorite");
        selectedPlacesAdapter.setClickListener(this);
        recyclerView.setAdapter(selectedPlacesAdapter);

        //decoration divider
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void getPlaces() {
        try {
            TourBuddyDB = dbHelper.getReadableDatabase();

            Cursor cursor = TourBuddyDB.rawQuery(("SELECT p.placeId, p.placeName, p.locationLink, p.countryName, p.cityName, p.imageLink, p.webLink, p.description, p.videoLink, " +
                    "(SELECT AVG(r.rank) FROM "+ TBName_Rankings +" r WHERE r.placeId = p.placeId) AS ranking, " +
                    "(SELECT count(cr.rank) FROM "+ TBName_Rankings +" cr WHERE cr.placeId = p.placeId) AS countrate " +
                    "FROM " + TBName_Places +" p " + " WHERE p.placeId IN " +
                    "(SELECT f.placeId " +
                    "FROM " + TBName_FavoritePlaces +" f " +
                            "WHERE f.userEmail LIKE '"+ userEmail+"') " +
                    "ORDER BY p.countryName, p.cityName, p.placeName"), null);
            while (cursor.moveToNext()){
                RankedPlaces p = new RankedPlaces(cursor.getInt(cursor.getColumnIndex("placeId")),
                        cursor.getString(cursor.getColumnIndex("placeName")),
                        cursor.getString(cursor.getColumnIndex("locationLink")),
                        cursor.getString(cursor.getColumnIndex("countryName")),
                        cursor.getString(cursor.getColumnIndex("cityName")),
                        cursor.getString(cursor.getColumnIndex("imageLink")),
                        cursor.getString(cursor.getColumnIndex("webLink")),
                        (int)cursor.getFloat(cursor.getColumnIndex("ranking")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        true,
                        cursor.getString(cursor.getColumnIndex("videoLink")),
                        cursor.getInt(cursor.getColumnIndex("countrate"))
                );
                places.add(p);
            }

        }catch (Exception e){
            Log.e("SignUpActivity", e.getMessage());
        }finally{
            TourBuddyDB.close();
        }
    }

    public static void updateFavotites(int placeId, boolean fav) {
        if(places.size()>0){
            for (int i = 0 ; i < places.size(); i++){
                if (places.get(i).getPlaceId()== placeId){
                    places.get(i).setFavorite(fav);
                }
            }
            Log.d("msg", "updateFavotites: " + placeId + " fav: " + fav + "\n");
            if(fav){
                ContentValues cv = new ContentValues();
                cv.put("placeId", placeId);
                cv.put("userEmail", userEmail);

                try {
                    TourBuddyDB = dbHelper.getWritableDatabase();
                    TourBuddyDB.insert(TBName_FavoritePlaces, null, cv);
                    Log.v("SearchPlaceActivity", "Favorite Added");
                    Toast.makeText(context, R.string.strAddedFavorite, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.e("SearchPlaceActivity", e.getMessage());
                }finally {
                    TourBuddyDB.close();
                }
            }else{

                try {
                    TourBuddyDB = dbHelper.getWritableDatabase();
                    TourBuddyDB.delete(TBName_FavoritePlaces, "placeId = " + placeId +" AND " +
                            "userEmail LIKE '"+ userEmail +"'", null);
                    Log.v("SearchPlaceActivity", "Favorite Removed");
                    Toast.makeText(context, R.string.strRemovedFavorite, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.e("SearchPlaceActivity", e.getMessage());
                }finally {
                    TourBuddyDB.close();
                }
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        RankedPlaces place = new RankedPlaces(selectedPlacesAdapter.getItem(position).getPlaceId(),
                selectedPlacesAdapter.getItem(position).getPlaceName(),
                selectedPlacesAdapter.getItem(position).getLocationLink(),
                selectedPlacesAdapter.getItem(position).getCountry(),
                selectedPlacesAdapter.getItem(position).getCity(),
                selectedPlacesAdapter.getItem(position).getImageLink(),
                selectedPlacesAdapter.getItem(position).getWebLink(),
                selectedPlacesAdapter.getItem(position).getRank(),
                selectedPlacesAdapter.getItem(position).getDescription(),
                selectedPlacesAdapter.getItem(position).isFavorite(),
                selectedPlacesAdapter.getItem(position).getVideoLink(),
                selectedPlacesAdapter.getItem(position).getCountRate()

        );
        Intent i = new Intent(this, PlaceActivity.class);
        i.putExtra("placeSelected", place);
        startActivity(i);
    }


    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("msg", "onResume ");

        places.clear();
        getPlaces();

        //old code
        /*
        SharedPreferences sp = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
        userEmail = sp.getString("UserEmail", "Data Missing");
        int selectedFav = sp.getInt("SelectedFav", -1);
        boolean selectedFavValue = sp.getBoolean("SelectedFavValue", false);

        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.remove("SelectedFav");
        spEditor.remove("SelectedFavValue");
        spEditor.apply();

        for (int i = 0; i < places.size(); i++) {
            if (places.get(i).getPlaceId()==selectedFav){
                places.get(i).setFavorite(selectedFavValue);
            }
        }
        */

        //update recyclerView
        selectedPlacesAdapter.notifyDataSetChanged();
    }
}
