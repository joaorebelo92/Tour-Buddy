package com.example.joorebelo.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBName = "TourBuddyDB";
    public static final String TBName_User = "User";
    public static final String TBName_FavoritePlaces = "FavoritePlaces";
    public static final String TBName_Rankings = "Rankings";
    public static final String TBName_Places = "Places";
    public static final String TBName_Country = "Country";
    public static final String TBName_City = "City";
    public static final String TBName_Support = "Support";




    //public static final String TBName_ParkingInfo = "ParkingInfo";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DBName, null, 1);
    }

    public DBHelper(Context context) {
        super(context, DBName, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            //Places
            String CREATE_TABLE_PLACES = "CREATE TABLE " + TBName_Places +
                    " (placeId INTEGER  PRIMARY KEY, " +
                    "placeName VARCHAR(50), " +
                    "locationLink VARCHAR(50), " +
                    "countryName VARCHAR(50), " +
                    "cityName VARCHAR(50), " +
                    "imageLink VARCHAR(50), " +
                    "webLink VARCHAR(200), " +
                    "description VARCHAR(500)," +
                    "videoLink VARCHAR(50))";
            Log.v("DBHelper", CREATE_TABLE_PLACES);
            db.execSQL(CREATE_TABLE_PLACES);

            //users
            String CREATE_TABLE_USER = "CREATE TABLE " + TBName_User +
                    " (email VARCHAR(50) PRIMARY KEY, " +
                    "password VARCHAR(20), " +
                    "name VARCHAR(100), " +
                    "phone VARCHAR(20), " +
                    "dob VARCHAR(20), " +
                    "role VARCHAR(20))";
            Log.v("DBHelper", CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_USER);

            //Ratings
            String CREATE_TABLE_RATINGS = "CREATE TABLE " + TBName_Rankings +
                    " (userEmail VARCHAR(50), " +
                    "placeId INTEGER, " +
                    "rank INTEGER, " +
                    "PRIMARY KEY(placeId, userEmail), " +
                    "FOREIGN KEY (placeId) REFERENCES " + TBName_Places + " (placeId), " +
                    "FOREIGN KEY (userEmail) REFERENCES " + TBName_User + " (email))";
            Log.v("DBHelper", CREATE_TABLE_RATINGS);
            db.execSQL(CREATE_TABLE_RATINGS);

            //Support
            String CREATE_TABLE_SUPPORT = "CREATE TABLE " + TBName_Support +
                    " (supportId INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "userEmail VARCHAR(50), " +
                    "subject VARCHAR(50), " +
                    "message VARCHAR(50), " +
                    "date VARCHAR(50), " +
                    "FOREIGN KEY (userEmail) REFERENCES " + TBName_User + " (email))";
            Log.v("DBHelper", CREATE_TABLE_SUPPORT);
            db.execSQL(CREATE_TABLE_SUPPORT);

            //Favorite Places
            String CREATE_TABLE_FAVORITE_PLACES =
                    "CREATE TABLE " + TBName_FavoritePlaces +
                            " (placeId INTEGER, " +
                            "userEmail VARCHAR(50), " +
                            "PRIMARY KEY(placeId, userEmail), " +
                            "FOREIGN KEY (placeId) REFERENCES " + TBName_Places + " (placeId), " +
                            "FOREIGN KEY (userEmail) REFERENCES " + TBName_User + " (email))";
            Log.v("DBHelper", CREATE_TABLE_FAVORITE_PLACES);
            db.execSQL(CREATE_TABLE_FAVORITE_PLACES);

        }catch (Exception e){
            Log.e("DBHelper", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TBName_FavoritePlaces);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS " + TBName_Support);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS " + TBName_Rankings);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS " + TBName_User);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS " + TBName_Places);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS " + TBName_City);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS " + TBName_Country);
            onCreate(db);
        }catch (Exception e){
            Log.e("DBHelper", e.getMessage());
        }
    }
}