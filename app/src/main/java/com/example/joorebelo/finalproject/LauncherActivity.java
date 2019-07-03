package com.example.joorebelo.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

public class LauncherActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase TourBuddyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        WebView webGif = findViewById(R.id.webGif);

        webGif.loadUrl("file:///android_asset/WebGif.html");

        final Context context = this;

        Thread timer = new Thread(){
            public void run(){
                try {
                    Thread.sleep(3000);

                }catch (Exception ex){
                    Toast.makeText(context, R.string.strSometingWentWrong, Toast.LENGTH_LONG).show();
                }finally {

                    startActivity(new Intent(context,LoginActivity.class));
                    finish();
                }
            }
        };
        timer.start();

        //fill DB
        dbHelper = new DBHelper(this);
       insertData();

        //just of testing
        //displayData();
    }

    private void insertData(){


        ContentValues cv_places_1 = new ContentValues();
        cv_places_1.put("placeId", 1);
        cv_places_1.put("placeName", "CN Tower");
        cv_places_1.put("locationLink", "43.642579,-79.387092");
        cv_places_1.put("countryName", "Canada");
        cv_places_1.put("cityName", "Toronto");
        cv_places_1.put("imageLink", "cn_tower");
        cv_places_1.put("webLink", "ca/?aGeoCode%5Blat%5D=43.644749&aGeoCode%5Blng%5D=-79.391251&iGeoDistanceItem=4033378");
        cv_places_1.put("description", "Toronto's famous landmark, the 553-meter CN Tower, " +
                "is one of the city's must see attractions and also the most impossible to miss. " +
                "Towering above the downtown, the structure can be seen from almost everywhere in the city. " +
                "Visitors have the option of simply appreciating the building from the ground, or taking a " +
                "trip up to one of the observation areas or restaurants for fabulous views of the city " +
                "and Lake Ontario.");
        cv_places_1.put("videoLink", "cn_tower");


        ContentValues cv_places_2 = new ContentValues();
        cv_places_2.put("placeId", 2);
        cv_places_2.put("placeName", "Royal Ontario Museum");
        cv_places_2.put("locationLink", "43.667753,-79.394735");
        cv_places_2.put("countryName", "Canada");
        cv_places_2.put("cityName", "Toronto");
        cv_places_2.put("imageLink", "cn_tower");
        cv_places_2.put("webLink", "ca/?aGeoCode%5Blat%5D=43.667767&aGeoCode%5Blng%5D=-79.394516&iGeoDistanceItem=169122");
        cv_places_2.put("description", "The Royal Ontario Museum, known as the ROM, is one of " +
                "Canada's premier museums with an international reputation for excellence. It houses " +
                "an outstanding collection and also features major exhibitions from around the world.");
        cv_places_2.put("videoLink", "cn_tower");

        ContentValues cv_places_3 = new ContentValues();
        cv_places_3.put("placeId", 3);
        cv_places_3.put("placeName", "Rogers Centre");
        cv_places_3.put("locationLink", "43.641449,-79.389326");
        cv_places_3.put("countryName", "Canada");
        cv_places_3.put("cityName", "Toronto");
        cv_places_3.put("imageLink", "cn_tower");
        cv_places_3.put("webLink", "ca/?aGeoCode%5Blat%5D=43.641354&aGeoCode%5Blng%5D=-79.389221&iGeoDistanceItem=701711");
        cv_places_3.put("description", "Immediately adjacent to the CN Tower is Rogers Centre, a " +
                "massive domed sports arena. The unique design includes a roof, which slides back, " +
                "allowing it to be opened in favorable weather.");
        cv_places_3.put("videoLink", "cn_tower");

        ContentValues cv_places_4 = new ContentValues();
        cv_places_4.put("placeId", 4);
        cv_places_4.put("placeName", "Art Gallery of Ontario");
        cv_places_4.put("locationLink", "43.653618,-79.392490");
        cv_places_4.put("countryName", "Canada");
        cv_places_4.put("cityName", "Toronto");
        cv_places_4.put("imageLink", "cn_tower");
        cv_places_4.put("webLink", "ca/?aGeoCode%5Blat%5D=43.653854&aGeoCode%5Blng%5D=-79.392799&iGeoDistanceItem=910489");
        cv_places_4.put("description", "The renowned Art Gallery of Ontario (AGO) occupies a unique " +
                "looking modern building on the west side of the city center. A whole series of " +
                "temporary exhibitions are mounted throughout the year by this exceptionally well " +
                "endowed gallery. ");
        cv_places_4.put("videoLink", "cn_tower");

        ContentValues cv_places_5 = new ContentValues();
        cv_places_5.put("placeId", 5);
        cv_places_5.put("placeName", "Casa Loma");
        cv_places_5.put("locationLink", "43.678055,-79.409405");
        cv_places_5.put("countryName", "Canada");
        cv_places_5.put("cityName", "Toronto");
        cv_places_5.put("imageLink", "cn_tower");
        cv_places_5.put("webLink", "ca/?aGeoCode%5Blat%5D=43.677715&aGeoCode%5Blng%5D=-79.409378&iGeoDistanceItem=162529");
        cv_places_5.put("description", "Standing in beautifully kept grounds, Casa Loma is an " +
                "extraordinary building somewhat reminiscent of a medieval castle.");
        cv_places_5.put("videoLink", "cn_tower");

        ContentValues cv_places_6 = new ContentValues();
        cv_places_6.put("placeId", 6);
        cv_places_6.put("placeName", "Toronto Zoo");
        cv_places_6.put("locationLink", "43.817724,-79.185850");
        cv_places_6.put("countryName", "Canada");
        cv_places_6.put("cityName", "Toronto");
        cv_places_6.put("imageLink", "cn_tower");
        cv_places_6.put("webLink", "ca/?aGeoCode%5Blat%5D=43.820503&aGeoCode%5Blng%5D=-79.180504&iGeoDistanceItem=753246");
        cv_places_6.put("description", "Toronto's huge zoo, with its collection of several thousand " +
                "animals, lies on the Red River some 40 kilometers northeast of the city center. " +
                "One of the major attractions is the panda exhibit, which opened in Toronto in 2013.");
        cv_places_6.put("videoLink", "cn_tower");

        ContentValues cv_places_7 = new ContentValues();
        cv_places_7.put("placeId", 7);
        cv_places_7.put("placeName", "St. Lawrence Market");
        cv_places_7.put("locationLink", "43.648603,-79.371535");
        cv_places_7.put("countryName", "Canada");
        cv_places_7.put("cityName", "Toronto");
        cv_places_7.put("imageLink", "cn_tower");
        cv_places_7.put("webLink", "ca/?aGeoCode%5Blat%5D=43.648666&aGeoCode%5Blng%5D=-79.371574&iGeoDistanceItem=938800");
        cv_places_7.put("description", "The St. Lawrence Market houses a variety of vendors selling " +
                "various food products, flowers, and specialty items. The St. Lawrence Hall was " +
                "built in Toronto in 1850 and served as a public meeting place and a concert venue.");
        cv_places_7.put("videoLink", "cn_tower");

        ContentValues cv_places_8 = new ContentValues();
        cv_places_8.put("placeId", 8);
        cv_places_8.put("placeName", "Entertainment District");
        cv_places_8.put("locationLink", "43.643968,-79.388690");
        cv_places_8.put("countryName", "Canada");
        cv_places_8.put("cityName", "Toronto");
        cv_places_8.put("imageLink", "cn_tower");
        cv_places_8.put("webLink", "ca/?aGeoCode%5Blat%5D=43.647514&aGeoCode%5Blng%5D=-79.390495&iGeoDistanceItem=4072720");
        cv_places_8.put("description", "Toronto's answer to New York's Broadway, the Toronto " +
                "Entertainment District comes to life in the evenings. This is the place to come to " +
                "see major theater productions with the latest shows and musicals, concerts, " +
                "and other performing arts.");
        cv_places_8.put("videoLink", "cn_tower");

        ContentValues cv_places_9 = new ContentValues();
        cv_places_9.put("placeId", 9);
        cv_places_9.put("placeName", "City Hall");
        cv_places_9.put("locationLink", "43.653463,-79.384041");
        cv_places_9.put("countryName", "Canada");
        cv_places_9.put("cityName", "Toronto");
        cv_places_9.put("imageLink", "cn_tower");
        cv_places_9.put("webLink", "ca/?aGeoCode%5Blat%5D=43.652699&aGeoCode%5Blng%5D=-79.381828&iGeoDistanceItem=538621");
        cv_places_9.put("description", "Dominating the spacious Nathan Philips Square with its " +
                "bronze sculpture, \"The Archer,\" by Henry Moore, is the still highly acclaimed " +
                "new City Hall. It was designed by the gifted Finnish architect Viljo Revell " +
                "and built in 1965.");
        cv_places_9.put("videoLink", "cn_tower");

        ContentValues cv_places_10 = new ContentValues();
        cv_places_10.put("placeId", 10);
        cv_places_10.put("placeName", "Eaton Center");
        cv_places_10.put("locationLink", "43.654490,-79.380642");
        cv_places_10.put("countryName", "Canada");
        cv_places_10.put("cityName", "Toronto");
        cv_places_10.put("imageLink", "cn_tower");
        cv_places_10.put("webLink", "ca/?aGeoCode%5Blat%5D=43.654049&aGeoCode%5Blng%5D=-79.38076&iGeoDistanceItem=196191");
        cv_places_10.put("description", "The huge Eaton Center is located at the north end of the " +
                "Central Business District. With its own subway station, this ultra-modern shopping " +
                "complex extends over several blocks and is continually being renovated and enlarged.");
        cv_places_10.put("videoLink", "cn_tower");

        ContentValues cv_places_11 = new ContentValues();
        cv_places_11.put("placeId", 11);
        cv_places_11.put("placeName", "Mosteiro dos Jeronimos");
        cv_places_11.put("locationLink", "38.697979,-9.206712");
        cv_places_11.put("countryName", "Portugal");
        cv_places_11.put("cityName", "Lisboa");
        cv_places_11.put("imageLink", "cn_tower");
        cv_places_11.put("webLink", "pt/?aGeoCode%5Blat%5D=38.697399&aGeoCode%5Blng%5D=-9.2071&iGeoDistanceItem=18321");
        cv_places_11.put("description", "A highlight of any Lisbon sightseeing tour, the 16th-century" +
                " Jerónimos monastery is one of the great landmarks of Portugal, a stunning monument " +
                "of immense historic and cultural significance deserving of its UNESCO World Heritage " +
                "Site accolade.");
        cv_places_11.put("videoLink", "cn_tower");

        ContentValues cv_places_12 = new ContentValues();
        cv_places_12.put("placeId", 12);
        cv_places_12.put("placeName", "Rideau Canal");
        cv_places_12.put("locationLink", "45.404520,-75.680988");
        cv_places_12.put("countryName", "Canada");
        cv_places_12.put("cityName", "Ottawa");
        cv_places_12.put("imageLink", "cn_tower");
        cv_places_12.put("webLink", "ca/?aGeoCode%5Blat%5D=45.413483&aGeoCode%5Blng%5D=-75.684982&iGeoDistanceItem=9860100");
        cv_places_12.put("description", "During the warmer months, explore the canal by strolling or " +
                "cycling along its scenic banks, which make their way from Ottawa’s downtown core to " +
                "Dows Lake. Or for the more adventurous visitor.");
        cv_places_12.put("videoLink", "cn_tower");

        ContentValues cv_places_13 = new ContentValues();
        cv_places_13.put("placeId", 13);
        cv_places_13.put("placeName", "Taj Mahal");
        cv_places_13.put("locationLink", "27.175004,78.042155");
        cv_places_13.put("countryName", "India");
        cv_places_13.put("cityName", "Agra");
        cv_places_13.put("imageLink", "cn_tower");
        cv_places_13.put("webLink", "in/?aGeoCode%5Blat%5D=27.174999&aGeoCode%5Blng%5D=78.042198&iGeoDistanceItem=133621");
        cv_places_13.put("description", "Construction of the mausoleum was essentially completed in 1643 but work continued " +
                "on other phases of the project for another 10 years.");
        cv_places_13.put("videoLink", "cn_tower");

        ContentValues cv_user = new ContentValues();
        cv_user.put("email", "test@test.com");
        cv_user.put("password", "qwer");
        cv_user.put("name", "Joao");
        cv_user.put("phone", "654987985");
        cv_user.put("dob", "1500-01-01");
        cv_user.put("role", "User");

        ContentValues cv_user_2 = new ContentValues();
        cv_user_2.put("email", "test2@test.com");
        cv_user_2.put("password", "qwer");
        cv_user_2.put("name", "test");
        cv_user_2.put("phone", "654987985");
        cv_user_2.put("dob", "1500-01-01");
        cv_user_2.put("role", "User");

        ContentValues cv_user_3 = new ContentValues();
        cv_user_3.put("email", "test");
        cv_user_3.put("password", "qwer");
        cv_user_3.put("name", "test");
        cv_user_3.put("phone", "654987985");
        cv_user_3.put("dob", "1500-01-01");
        cv_user_3.put("role", "User");

        ContentValues cv_ranking_1 = new ContentValues();
        cv_ranking_1.put("userEmail", "test2@test.com");
        cv_ranking_1.put("placeId", 3);
        cv_ranking_1.put("rank", 4);

        ContentValues cv_ranking_2 = new ContentValues();
        cv_ranking_2.put("userEmail", "test@test.com");
        cv_ranking_2.put("placeId", 7);
        cv_ranking_2.put("rank", 5);

        ContentValues cv_ranking_3 = new ContentValues();
        cv_ranking_3.put("userEmail", "test@test.com");
        cv_ranking_3.put("placeId", 1);
        cv_ranking_3.put("rank", 3);

        ContentValues cv_favorite_1 = new ContentValues();
        cv_favorite_1.put("placeId", 11);
        cv_favorite_1.put("userEmail", "test");

        try {
            TourBuddyDB = dbHelper.getWritableDatabase();

            TourBuddyDB.insert("Places", null, cv_places_1);
            TourBuddyDB.insert("Places", null, cv_places_2);
            TourBuddyDB.insert("Places", null, cv_places_3);
            TourBuddyDB.insert("Places", null, cv_places_4);
            TourBuddyDB.insert("Places", null, cv_places_5);
            TourBuddyDB.insert("Places", null, cv_places_6);
            TourBuddyDB.insert("Places", null, cv_places_7);
            TourBuddyDB.insert("Places", null, cv_places_8);
            TourBuddyDB.insert("Places", null, cv_places_9);
            TourBuddyDB.insert("Places", null, cv_places_10);
            TourBuddyDB.insert("Places", null, cv_places_11);
            TourBuddyDB.insert("Places", null, cv_places_12);
            TourBuddyDB.insert("Places", null, cv_places_13);
            TourBuddyDB.insert("User", null, cv_user);
            TourBuddyDB.insert("User", null, cv_user_2);
            TourBuddyDB.insert("User", null, cv_user_3);
            TourBuddyDB.insert("Rankings", null, cv_ranking_1);
            TourBuddyDB.insert("Rankings", null, cv_ranking_2);
            TourBuddyDB.insert("Rankings", null, cv_ranking_3);
            TourBuddyDB.insert("FavoritePlaces", null, cv_favorite_1);





            Log.v("LauncherActivity", "Account Created");
        }catch (Exception e){
            Log.e("LauncherActivity", e.getMessage());
        }finally {
            TourBuddyDB.close();
        }


    }

    private void displayData() {
        try {

            TourBuddyDB = dbHelper.getReadableDatabase();

            /*
            String columns[] = {"placeId", "placeName", "locationLink", "countryName", "cityName", "imageLink", "webLink"};
            Cursor cursor = TourBuddyDB.query("Places", columns, null, null,null, null,null);
            while (cursor.moveToNext()){
                String UserData = String.valueOf(cursor.getInt(cursor.getColumnIndex("placeId")));
                UserData += "\n" + cursor.getString(cursor.getColumnIndex("placeName"));
                UserData += "\n" + cursor.getString(cursor.getColumnIndex("locationLink"));
                UserData += "\n" + cursor.getString(cursor.getColumnIndex("countryName"));
                UserData += "\n" + cursor.getString(cursor.getColumnIndex("cityName"));
                UserData += "\n" + cursor.getString(cursor.getColumnIndex("imageLink"));
                UserData += "\n" + cursor.getString(cursor.getColumnIndex("webLink"));

                Toast.makeText(this, UserData, Toast.LENGTH_LONG).show();

            }
            */
            /*
            String columns[] = {"email", "password", "name", "phone", "dob", "role"};
            Cursor cursor = TourBuddyDB.query("User", columns, null, null,null, null,null);
            while (cursor.moveToNext()){
                String UserData = cursor.getString(cursor.getColumnIndex("email"));
                UserData += "\n" + cursor.getString(cursor.getColumnIndex("password"));
                UserData += "\n" + cursor.getString(cursor.getColumnIndex("name"));
                UserData += "\n" + cursor.getString(cursor.getColumnIndex("phone"));
                UserData += "\n" + cursor.getString(cursor.getColumnIndex("dob"));
                UserData += "\n" + cursor.getString(cursor.getColumnIndex("role"));

                Toast.makeText(this, UserData, Toast.LENGTH_LONG).show();

            }
            */
        }catch (Exception e){
            Log.e("SignUpActivity", e.getMessage());
        }finally{
            TourBuddyDB.close();
        }
    }
}
