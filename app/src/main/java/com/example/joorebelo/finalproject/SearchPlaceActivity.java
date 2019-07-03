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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joorebelo.finalproject.adapters.PlacesAdapter;
import com.example.joorebelo.finalproject.adapters.RankedPlacesAdapter;
import com.example.joorebelo.finalproject.model.RankedPlaces;

import java.util.ArrayList;
import java.util.List;

public class SearchPlaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PlacesAdapter.ItemClickListener {

    static DBHelper dbHelper;
    static SQLiteDatabase TourBuddyDB;
    public static final String TBName_Places = "Places";
    public static final String TBName_Rankings = "Rankings";
    public static final String TBName_FavoritePlaces = "FavoritePlaces";
    private static String userEmail;

    static ArrayList<RankedPlaces> places;
    static ArrayList<RankedPlaces> selectedPlaces;

    ArrayList<String> countryList, cityList;
    ArrayAdapter<String> countryAdapter, cityAdapter;

    TextView txtCountry, txtCity;
    Spinner spnCountry, spnCity;
    String selectedCountry = "Canada", selectedCity = "Ottawa";

    PlacesAdapter selectedPlacesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);
        setTitle(R.string.title_activity_search_place);

        ////
        dbHelper = new DBHelper(this);
        SharedPreferences sp = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
        userEmail = sp.getString("UserEmail", "Data Missing");
        places = new ArrayList<>();
        getPlaces();
        txtCountry = findViewById(R.id.txtCountry);
        txtCity = findViewById(R.id.txtCity);

        countryList = new ArrayList<>();
        cityList = new ArrayList<>();

        //////////////////////////////////////////
        spnCountry = findViewById(R.id.spnCountry);
        getAllCountries();

        countryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCountry.setAdapter(countryAdapter);
        spnCountry.setOnItemSelectedListener(this);

        //////////////////////////////////////////
        spnCity = findViewById(R.id.spnCity);
        getAllCities();
        cityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCity.setAdapter(cityAdapter);
        spnCity.setOnItemSelectedListener(this);
        //////////////////////////////////////////

        // set up the RecyclerView
        selectedPlaces = new ArrayList<>();

        for (RankedPlaces p : places) {
            selectedPlaces.add(new RankedPlaces(p.getPlaceId(), p.getPlaceName(), p.getLocationLink(),
                    p.getCountry(), p.getCity(), p.getImageLink(), p.getWebLink(), p.getRank(), p.getDescription(), p.isFavorite(), p.getVideoLink(), p.getCountRate()));
        }


        RecyclerView recyclerView = findViewById(R.id.recyclerView_places);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedPlacesAdapter = new PlacesAdapter(this, selectedPlaces, "search");
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
            ArrayList<Integer> favoritesArray = new ArrayList<>();
            TourBuddyDB = dbHelper.getReadableDatabase();
            Cursor cursorf = TourBuddyDB.rawQuery(("SELECT f.placeId " +
                    "FROM " + TBName_FavoritePlaces +" f " +
                    "WHERE f.userEmail LIKE '"+ userEmail+"'"), null);

            while (cursorf.moveToNext()){
                int f = cursorf.getInt(cursorf.getColumnIndex("placeId"));

                favoritesArray.add(f);
            }

            Cursor cursor = TourBuddyDB.rawQuery(("SELECT p.placeId, p.placeName, p.locationLink, p.countryName, p.cityName, p.imageLink, p.webLink, p.description, videoLink, " +
                    "(SELECT AVG(r.rank) FROM "+ TBName_Rankings +" r WHERE r.placeId = p.placeId) AS ranking, " +
                    "(SELECT count(cr.rank) FROM "+ TBName_Rankings +" cr WHERE cr.placeId = p.placeId) AS countrate " +
                    "FROM " + TBName_Places +" p " +
                    "ORDER BY countryName, cityName, placeName"), null);
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

                places.add(p);
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
            Log.e("SearchPlaceActivity", e.getMessage());
        }finally{
            TourBuddyDB.close();
        }
    }

    private void getAllCountries() {
        countryList = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
            if(!countryList.contains(places.get(i).getCountry())){
                countryList.add(places.get(i).getCountry());
            }
        }
    }

    private void getAllCities() {
        cityList = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
            if(!cityList.contains(places.get(i).getCity())){
                cityList.add(places.get(i).getCity());
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId()==spnCountry.getId()){
            selectedCountry = spnCountry.getSelectedItem().toString();
            Log.d("msg", "selectedCountry: " + selectedCountry + "\n");

            //update spinner Cities
            getCitiesByCountry();
            spnCity.setSelection(0,true); //first position of the spinner
            selectedCity = spnCity.getSelectedItem().toString();
        }else if(parent.getId()==spnCity.getId()){
            selectedCity = spnCity.getSelectedItem().toString();
            Log.d("msg", "selectedCity: " + selectedCity + "\n");
        }
        //update spinners
        countryAdapter.notifyDataSetChanged();
        cityAdapter.notifyDataSetChanged();

        updateRecycleView();

    }

    private void updateRecycleView() {
        selectedPlaces.clear();
        Log.d("msg", "places.size():  " + places.size() + "\n");
        Log.d("msg", "selectedPlaces.size():  " + selectedPlaces.size() + "\n");

        for(int i=0; i < places.size(); i++)
        {
            if (places.get(i).getCity().equals(selectedCity) && places.get(i).getCountry().equals(selectedCountry)) {
                selectedPlaces.add(new RankedPlaces(places.get(i).getPlaceId(), places.get(i).getPlaceName(), places.get(i).getLocationLink(),
                        places.get(i).getCountry(), places.get(i).getCity(), places.get(i).getImageLink(), places.get(i).getWebLink(),
                        places.get(i).getRank(), places.get(i).getDescription(), places.get(i).isFavorite(), places.get(i).getVideoLink(), places.get(i).getCountRate()));
            }
        }
        Log.d("msg", "2 selectedPlaces.size():  " + selectedPlaces.size() + "\n");
        selectedPlacesAdapter.notifyDataSetChanged();
    }


    private void getCitiesByCountry() {
        cityList.clear();
        for (int i = 0; i < places.size(); i++) {
            if (!cityList.contains(places.get(i).getCity())) {
                if (places.get(i).getCountry().equals(selectedCountry)) {
                    cityList.add(places.get(i).getCity());
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        updateRecycleView();

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

        for (int i = 0; i < selectedPlaces.size(); i++) {
            if (selectedPlaces.get(i).getPlaceId()==selectedFav){
                selectedPlaces.get(i).setFavorite(selectedFavValue);
            }
        }
        */

        selectedPlacesAdapter.notifyDataSetChanged();
    }

    public static void updateFavotites(int placeId, boolean fav) {
        if(places.size()>0){
            for (int i = 0 ; i < places.size(); i++){
                if (places.get(i).getPlaceId()== placeId){
                    places.get(i).setFavorite(fav);
                }
            }
            for (int i = 0 ; i < selectedPlaces.size(); i++){
                if (selectedPlaces.get(i).getPlaceId()== placeId){
                    selectedPlaces.get(i).setFavorite(fav);
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
                }catch (Exception e){
                    Log.e("SearchPlaceActivity", e.getMessage());
                }finally {
                    TourBuddyDB.close();
                }
            }
        }



    }
}
