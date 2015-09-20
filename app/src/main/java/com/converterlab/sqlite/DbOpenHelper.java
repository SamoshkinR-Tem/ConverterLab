package com.converterlab.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.converterlab.MainActivity;

/**
 * Created by R-Tem on 16.09.2015.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbOpenHelper";
    public static final String DB_NAME = "currency_exchange.db";
    public static final int DB_VERSION = 1;
    public static final String COL_ROW_ID = "row_id";
    public static final String COL_REGION_ID = "regionId";
    public static final String COL_CITY_ID = "cityId";

    //----------------------------------------------------------------------------------------------
    public static final String ORGANIZATIONS_TABLE_NAME = "organizations";
    //    public static final String COL_ROW_ID = "row_id";
    public static final String COL_ID = "id";
    public static final String COL_OLD_ID = "oldId";
    public static final String COL_ORG_TYPE = "orgType";
    public static final String COL_TITLE = "title";
    //    public static final String COL_REGION_ID = "regionId";
    //    public static final String COL_CITY_ID = "cityId";
    public static final String COL_PHONE = "phone";
    public static final String COL_ADDRESS = "address";
    public static final String COL_LINK = "link";
    public static final String COL_CURRENCIES = "currencies";
    //    public static final String COL_ASK = "ask";
    //    public static final String COL_BID = "bid";
    public static final String ORGANIZATIONS_TABLE_CREATE =
            "CREATE TABLE " + ORGANIZATIONS_TABLE_NAME + " (" +
                    COL_ROW_ID + " INTEGER " + "PRIMARY KEY AUTOINCREMENT, " +
                    COL_ID + " TEXT, " +
//                    COL_OLD_ID + " TEXT, " +
//                    COL_ORG_TYPE + " TEXT, " +
                    COL_TITLE + " TEXT, " +
                    COL_REGION_ID + " TEXT, " +
                    COL_CITY_ID + " TEXT, " +
                    COL_PHONE + " TEXT, " +
                    COL_ADDRESS + " TEXT, " +
                    COL_LINK + " TEXT, " +
                    COL_CURRENCIES + " TEXT" + ");";
    //----------------------------------------------------------------------------------------------
    public static final String CURRENCIES_TABLE_NAME = "currencies";
    //    public static final String COL_ROW_ID = "row_id";
    public static final String COL_CURRENCY_ID = "currency_id";
    public static final String COL_CURRENCY_NAME = "currency_name";
    public static final String CURRENCIES_TABLE_CREATE =
            "CREATE TABLE " + CURRENCIES_TABLE_NAME + " (" +
                    COL_ROW_ID + " INTEGER " + "PRIMARY KEY AUTOINCREMENT, " +
                    COL_CURRENCY_ID + " TEXT, " +
                    COL_CURRENCY_NAME + " TEXT" + ");";
    //----------------------------------------------------------------------------------------------
    public static final String REGIONS_TABLE_NAME = "regions";
    //    public static final String COL_ROW_ID = "row_id";
    //    public static final String COL_REGION_ID = "organizations";
    public static final String COL_REGION_NAME = "region_name";
    public static final String REGIONS_TABLE_CREATE =
            "CREATE TABLE " + REGIONS_TABLE_NAME + " (" +
                    COL_ROW_ID + " INTEGER " + "PRIMARY KEY AUTOINCREMENT, " +
                    COL_REGION_ID + " TEXT, " +
                    COL_REGION_NAME + " TEXT" + ");";
    //----------------------------------------------------------------------------------------------
    public static final String CITIES_TABLE_NAME = "cities";
    //    public static final String COL_ROW_ID = "row_id";
    //    public static final String COL_CITY_ID = "organizations";
    public static final String COL_CITY_NAME = "city_name";
    public static final String CITIES_TABLE_CREATE =
            "CREATE TABLE " + CITIES_TABLE_NAME + " (" +
                    COL_ROW_ID + " INTEGER " + "PRIMARY KEY AUTOINCREMENT, " +
                    COL_CITY_ID + " TEXT, " +
                    COL_CITY_NAME + " TEXT" + ");";
    //----------------------------------------------------------------------------------------------

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase _sqLiteDatabase) {
        // create tables
        _sqLiteDatabase.execSQL(ORGANIZATIONS_TABLE_CREATE);
        Log.d(MainActivity.LOG_TAG, TAG + "onCreate#ORGANIZATIONS_TABLE_CREATED");
        _sqLiteDatabase.execSQL(CURRENCIES_TABLE_CREATE);
        Log.d(MainActivity.LOG_TAG, TAG + "onCreate#CURRENCIES_TABLE_CREATED");
        _sqLiteDatabase.execSQL(REGIONS_TABLE_CREATE);
        Log.d(MainActivity.LOG_TAG, TAG + "onCreate#REGIONS_TABLE_CREATED");
        _sqLiteDatabase.execSQL(CITIES_TABLE_CREATE);
        Log.d(MainActivity.LOG_TAG, TAG + "onCreate#CITIES_TABLE_CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(MainActivity.LOG_TAG, "--- onUpgrade database ---");
    }
}