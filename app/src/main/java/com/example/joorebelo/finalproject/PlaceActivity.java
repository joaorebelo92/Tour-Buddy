package com.example.joorebelo.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.joorebelo.finalproject.model.RankedPlaces;

import java.util.ArrayList;

public class PlaceActivity extends AppCompatActivity {

    static DBHelper dbHelper;
    static SQLiteDatabase TourBuddyDB;

    public static final String TBName_Places = "Places";
    public static final String TBName_Rankings = "Rankings";
    public static final String TBName_FavoritePlaces = "FavoritePlaces";
    private static String userEmail;
    VideoView videoPlayerPlace;
    MediaController mediaController;

    RankedPlaces place;

    TextView txtPlaceName, txtCountry, txtCity, txtPlaceDescriptionTitle, txtPlaceDescription, txtRatePlaceTitle, txtCountRate;
    ImageView imgPlace, imgFavotite, imgLocation, imgWebLink;
    RatingBar ratingBarGlobal, ratingBar;
    Button btnRatePlace;
    int myRank;
    boolean alreadyVoted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        dbHelper = new DBHelper(this);

        txtPlaceName = findViewById(R.id.txtPlaceName);
        txtCountry = findViewById(R.id.txtCountry);
        txtCity = findViewById(R.id.txtCity);
        txtPlaceDescriptionTitle = findViewById(R.id.txtPlaceDescriptionTitle);
        txtPlaceDescription = findViewById(R.id.txtPlaceDescription);
        txtRatePlaceTitle = findViewById(R.id.txtRatePlaceTitle);
        imgPlace = findViewById(R.id.imgPlace);
        ratingBarGlobal = findViewById(R.id.ratingBarGlobal);
        txtCountRate = findViewById(R.id.txtCountRate);

        imgFavotite = findViewById(R.id.imgFavotite);
        imgLocation = findViewById(R.id.imgLocation);
        imgWebLink = findViewById(R.id.imgWebLink);


        SharedPreferences sp = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
        userEmail = sp.getString("UserEmail", "Data Missing");

        //get place given by intent
        Intent i = getIntent();
        place = (RankedPlaces)i.getSerializableExtra("placeSelected");

        ratingBar = findViewById(R.id.ratingBar);
        myRank = getMyRating();// get my rating for this place
        ratingBar.setRating((float) myRank);
        btnRatePlace = findViewById(R.id.btnRatePlace);
        btnRatePlace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ratingBar.getRating() > 0){
                    updateMyRating(ratingBar.getRating());
                }
            }
        });

        //fill views
        txtPlaceName.setText(place.getPlaceName());
        txtCountry.setText(getString(R.string.strCountry) + ": " + place.getCountry());
        txtCity.setText(getString(R.string.strCity) + ": " + place.getCity());
        txtPlaceDescription.setText(place.getDescription());
        imgPlace.setImageURI(Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/drawable/" + place.getImageLink()));
        ratingBarGlobal.setRating( (float) place.getRank());
        txtCountRate.setText("(" + getString(R.string.strRatedBy) + " " + place.getCountRate() + " " + getString(R.string.strUsers) + ")");

        if (place.isFavorite()){
            imgFavotite.setImageResource(R.drawable.ic_favorite_places);
        }else
        {
            imgFavotite.setImageResource(R.drawable.ic_action_favorite_empty);
        }

        //imgFavotite listener
        imgFavotite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (place.isFavorite()){
                    place.setFavorite(false);
                    imgFavotite.setImageResource(R.drawable.ic_action_favorite_empty);
                    //Toast.makeText(getApplicationContext(), R.string.strRemovedFavorite, Toast.LENGTH_SHORT).show();
                }else{
                    place.setFavorite(true);
                    imgFavotite.setImageResource(R.drawable.ic_favorite_places);
                    //Toast.makeText(getApplicationContext(), R.string.strAddedFavorite, Toast.LENGTH_SHORT).show();
                }
                updateFavoriteDataBase();


            }
        });

        //imgLocation listener
        imgLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:"
                        + place.getLocationLink()
                        + " (" + place.getPlaceName() + ")";
                Intent locationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(locationIntent);
            }
        });

        //imgWebLink listener
        imgWebLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), WebHotelsNearActivity.class);
                i.putExtra("webLink", place.getWebLink());
                i.putExtra("placeName", place.getPlaceName());
                startActivity(i);
            }
        });


        /// video
        videoPlayerPlace = findViewById(R.id.videoPlayerPlace);
        videoPlayerPlace.setVideoURI(Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID+"/raw/"+place.getVideoLink()));
        //videoPlayerPlace.seekTo(200);
        mediaController = new MediaController(this);
        //mediaController.show(300);
        videoPlayerPlace.setMediaController(mediaController);
        //videoPlayerPlace.pause();
        videoPlayerPlace.start();

        //if we want to infinite replay
        /*videoPlayerPlace.setOnCompletionListener ( new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoPlayerPlace.start();
            }
        });*/
    }

    private int getMyRating() {
        myRank = 0;
        try {
            TourBuddyDB = dbHelper.getReadableDatabase();
            Cursor cursorf = TourBuddyDB.rawQuery(("SELECT r.rank " +
                    "FROM " + TBName_Rankings +" r " +
                    "WHERE r.userEmail LIKE '"+ userEmail +"' AND r.placeId = " + place.getPlaceId()), null);

            while (cursorf.moveToNext()){
                myRank = cursorf.getInt(cursorf.getColumnIndex("rank"));

            }
        }catch (Exception e){
            Log.e("PlaceActivity", e.getMessage());
        }finally{
            TourBuddyDB.close();
        }

        if (myRank>0){
            alreadyVoted = true;
        }
        return myRank;
    }

    private void updateMyRating(float rating) {
        ContentValues cv = new ContentValues();
        cv.put("userEmail", userEmail);
        cv.put("placeId", place.getPlaceId());
        cv.put("rank", rating);

        if (alreadyVoted){//Already voted for this place
            try {
                TourBuddyDB = dbHelper.getWritableDatabase();
                ContentValues cv2 = new ContentValues();
                cv2.put("rank", rating);

                TourBuddyDB.update(TBName_Rankings, cv2, "userEmail LIKE '"+userEmail
                        + "' AND placeId = " + place.getPlaceId(), null);

                Log.v("PlaceActivity", "Place updated");
                Toast.makeText(this, R.string.strPlaceRated, Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.e("PlaceActivity", e.getMessage());
            }finally {
                TourBuddyDB.close();
            }
        }else{//first time voting in this place
            try {
                TourBuddyDB = dbHelper.getWritableDatabase();
                TourBuddyDB.insert(TBName_Rankings, null, cv);
                Log.v("PlaceActivity", "rank Added");
                Toast.makeText(this, R.string.strPlaceRated, Toast.LENGTH_SHORT).show();
                place.setCountRate(place.getCountRate()+1);
                txtCountRate.setText("(" + getString(R.string.strRatedBy) + " " + place.getCountRate() + " " + getString(R.string.strUsers) + ")");
            }catch (Exception e){
                Log.e("PlaceActivity", e.getMessage());
            }finally {
                TourBuddyDB.close();
            }
            alreadyVoted = true;
        }

        myRank = (int) rating;
        ratingBar.setRating(rating);

        //update the global ranking for this place
        updateGlobalRating();
    }

    private void updateGlobalRating() {
        int globalRank = 0;
        try {
            TourBuddyDB = dbHelper.getReadableDatabase();
            Cursor cursor = TourBuddyDB.rawQuery(("SELECT AVG(r.rank) AS ranking " +
                    "FROM " + TBName_Rankings +" r " +
                    "WHERE r.placeId = " + place.getPlaceId()), null);

            while (cursor.moveToNext()){
                globalRank = (int)cursor.getFloat(cursor.getColumnIndex("ranking"));

            }
        }catch (Exception e){
            Log.e("PlaceActivity", e.getMessage());
        }finally{
            TourBuddyDB.close();
        }

        ratingBarGlobal.setRating(globalRank);

    }

    private void updateFavoriteDataBase() {
        if(place.isFavorite()){
            ContentValues cv = new ContentValues();
            cv.put("placeId", place.getPlaceId());
            cv.put("userEmail", userEmail);

            try {
                TourBuddyDB = dbHelper.getWritableDatabase();
                TourBuddyDB.insert(TBName_FavoritePlaces, null, cv);
                Log.v("SearchPlaceActivity", "Favorite Added.");
                Toast.makeText(this,R.string.strAddedFavorite, Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.e("SearchPlaceActivity", e.getMessage());
            }finally {
                TourBuddyDB.close();
            }
        }else{

            try {
                TourBuddyDB = dbHelper.getWritableDatabase();
                TourBuddyDB.delete(TBName_FavoritePlaces, "placeId = " + place.getPlaceId() +" AND " +
                        "userEmail LIKE '"+ userEmail +"'", null);
                Log.v("SearchPlaceActivity", "Favorite Removed");
                Toast.makeText(this,R.string.strRemovedFavorite, Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.e("SearchPlaceActivity", e.getMessage());
            }finally {
                TourBuddyDB.close();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.d("msg", "onBackPressed ");
        ////old code
        /*
        SharedPreferences sp = getSharedPreferences("com.example.joorebelo.finalproject.shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("SelectedFav", place.getPlaceId());
        edit.putBoolean("SelectedFavValue", place.isFavorite());
        edit.commit();
        */
    }
}
