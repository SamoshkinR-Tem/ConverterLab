package com.converterlab.jsonparser;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.converterlab.MainActivity;
import com.converterlab.jsonparser.model.MainJsonObject;
import com.converterlab.sqlite.DbOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by R-Tem on 14.09.2015.
 */
public class AsyncJsonParser extends AsyncTaskLoader<String> {

    public static final String KEY_JSON_FILE_PATH = "jsonFilePath";
    private static final String TAG = "AsyncJsonParser";
    private static final String REG_EXP = " / ";
    private enum Currencies {AED, AMD, AUD, AZN, BGN, BRL, BYR, CAD,
            CHF, CLP, CNY, CYP, CZK, DKK, EEK, EGP, EUR, GBP, HKD, HRK, HUF,
            ILS, INR, IQD, ISK, JPY, KGS, KRW, KWD, KZT, LBP, LTL, LVL, MDL,
            MTL, MXN, NOK, NZD, PKR, PLN, ROL, RUB, SAR, SEK, SGD, SKK, THB,
            TJS, TMT, TRY, TWD, USD, VND};
        public static final String ASK = "ask";
        public static final String BID = "bid";
    private static final String REGIONS[] = {"ua,7oiylpmiow8iy1smac8", "ua,7oiylpmiow8iy1smac1", "ua,7oiylpmiow8iy1smaci",
            "ua,7oiylpmiow8iy1smacl", "ua,7oiylpmiow8iy1smac7", "ua,7oiylpmiow8iy1smabz", "ua,7oiylpmiow8iy1smack",
            "ua,7oiylpmiow8iy1smac2", "ua,0,7oiylpmiow8iy1smad", "ua,7oiylpmiow8iy1smace", "ua,7oiylpmiow8iy1smacm",
            "ua,7oiylpmiow8iy1smac0", "ua,7oiylpmiow8iy1smacd", "ua,7oiylpmiow8iy1smacc", "ua,7oiylpmiow8iy1smacf",
            "ua,7oiylpmiow8iy1smacg", "ua,7oiylpmiow8iy1smac3", "ua,7oiylpmiow8iy1smacj", "ua,7oiylpmiow8iy1smach",
            "ua,7oiylpmiow8iy1smac6", "ua,7oiylpmiow8iy1smacb", "ua,7oiylpmiow8iy1smac5"};
    private static final String CITIES[] = {"7oiylpmiow8iy1smady", "7oiylpmiow8iy1smag3", "7oiylpmiow8iy1smaf1",
            "7oiylpmiow8iy1smadm", "7oiylpmiow8iy1smaee", "7oiylpmiow8iy1smaex", "7oiylpmiow8iy1smaev",
            "7oiylpmiow8iy1smadn", "7oiylpmiow8iy1smap5", "7oiylpmiow8iy1smaeg", "7oiylpmiow8iy1smadi",
            "7oiylpmiow8iy1smae3", "7oiylpmiow8iy1smafj", "7oiylpmiow8iy1smae8", "7oiylpmiow8iy1smaeb",
            "7oiylpmiow8iy1smadz", "7oiylpmiow8iy1smae0", "7oiylpmiow8iy1smadr", "7oiylpmiow8iy1smadq",
            "7oiylpmiow8iy1smadk", "7oiylpmiow8iy1smaer", "7oiylpmiow8iy1smadx", "7oiylpmiow8iy1smaf3",
            "7oiylpmiow8iy1smarv", "7oiylpmiow8iy1smagh", "7oiylpmiow8iy1smaek", "7oiylpmiow8iy1smaeo",
            "7oiylpmiow8iy1smadp", "7oiylpmiow8iy1smadj", "7oiylpmiow8iy1smaen", "7oiylpmiow8iy1smae7",
            "7oiylpmiow8iy1smaff", "7oiylpmiow8iy1smaea", "7oiylpmiow8iy1smael"};

    private String mJsonFilePath;
    private DbOpenHelper mDbOpenHelper;

    public AsyncJsonParser(Context _context, Bundle _bndl) {
        super(_context);
        mJsonFilePath = _bndl.getString(KEY_JSON_FILE_PATH);
        mDbOpenHelper = new DbOpenHelper(_context);
        Log.d(MainActivity.LOG_TAG, TAG + " #created");
    }

    @Override
    public String loadInBackground() {
        Log.d(MainActivity.LOG_TAG, TAG + "#loadInBackground#loading");

        try {
            return loadData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String loadData() throws IOException, JSONException {

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

        final String jsonString = getJsonString();

        JSONObject jsonObject           = new JSONObject(jsonString);
        JSONArray jsonOrganizations     = jsonObject.getJSONArray("organizations");
//        JSONObject jsonOrgTypesObj      = jsonObject.getJSONObject("orgTypes");
        JSONObject jsonCurrenciesObj    = jsonObject.getJSONObject("currencies");
        JSONObject jsonRegionsObj       = jsonObject.getJSONObject("regions");
        JSONObject jsonCitiesObj        = jsonObject.getJSONObject("cities");

        writeOrganizationsToDb(db, jsonOrganizations);
//        writeCurrenciesToDb(db, jsonCurrenciesObj);
//        writeRegionsToDb(db, jsonRegionsObj);
//        writeCitiesToDb(db, jsonCitiesObj);
        return "Database filled";
    }

    private String getJsonString() throws IOException {
        InputStream is = new FileInputStream(mJsonFilePath);
        final BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null){
            sb.append(line);
        }
        return sb.toString();
    }

    private void writeOrganizationsToDb(SQLiteDatabase _db, JSONArray _jsonOrganizations) {
        try {
            Log.d(MainActivity.LOG_TAG, TAG + "#writeOrganizationsToDb" +
            "\n" + _jsonOrganizations.getJSONObject(1).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        for (int i = 0; i < _jsonOrganizations.length(); i++) {
//            String insert = null;
//            try{
//                insert = "INSERT INTO " + mDbOpenHelper.ORGANIZATIONS_TABLE_NAME + " (" +
//                        mDbOpenHelper.COL_ID + ", " +
////                        mDbOpenHelper.COL_OLD_ID + ", " +
////                        mDbOpenHelper.COL_ORG_TYPE + ", " +
//                        mDbOpenHelper.COL_TITLE + ", " +
//                        mDbOpenHelper.COL_REGION_ID + ", " +
//                        mDbOpenHelper.COL_CITY_ID + ", " +
//                        mDbOpenHelper.COL_PHONE + ", " +
//                        mDbOpenHelper.COL_ADDRESS + ", " +
//                        mDbOpenHelper.COL_LINK + ", " +
//                        mDbOpenHelper.COL_CURRENCIES + ") VALUES (" +
//                        "'" + _jsonOrganizations.getJSONObject(i).getString(mDbOpenHelper.COL_ID) +
//                        "', '" + _jsonOrganizations.getJSONObject(i).getString(mDbOpenHelper.COL_TITLE) +
//                        "', '" + _jsonOrganizations.getJSONObject(i).getString(mDbOpenHelper.COL_REGION_ID) +
//                        "', '" + _jsonOrganizations.getJSONObject(i).getString(mDbOpenHelper.COL_CITY_ID) +
//                        "', '" + _jsonOrganizations.getJSONObject(i).getString(mDbOpenHelper.COL_PHONE) +
//                        "', '" + _jsonOrganizations.getJSONObject(i).getString(mDbOpenHelper.COL_ADDRESS) +
//                        "', '" + _jsonOrganizations.getJSONObject(i).getString(mDbOpenHelper.COL_LINK) +
//                        "', '" + _jsonOrganizations.getJSONObject(i)
//                                    .getJSONObject(mDbOpenHelper.COL_CURRENCIES) + REG_EXP +
//                                    _jsonOrganizations.getJSONObject(i)
//                                        .getJSONObject(mDbOpenHelper.COL_CURRENCIES)
//                                        .getJSONObject(Currencies.USD.name()) + REG_EXP +
//                                    _jsonOrganizations.getJSONObject(i)
//                                        .getJSONObject(mDbOpenHelper.COL_CURRENCIES)
//                                        .getJSONObject(Currencies.USD.name())
//                                        .getString(ASK)+"; "+ "')";
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            _db.execSQL(insert);
//        }
    }

    private void writeCurrenciesToDb(SQLiteDatabase _db, JSONObject _jsonCurrenciesObj) {
        for (int i = 0; i < Currencies.values().length; i++) {
            String insert = null;
            try{
                insert = "INSERT INTO " + mDbOpenHelper.CURRENCIES_TABLE_NAME + " (" +
                        mDbOpenHelper.COL_CURRENCY_ID + ", " +
                        mDbOpenHelper.COL_CURRENCY_NAME + ") VALUES (" +
                        "'" + Currencies.values()[i] +
                        "', '" + _jsonCurrenciesObj.getString(Currencies.values()[i].name()) + "')";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            _db.execSQL(insert);
        }
    }

    private void writeRegionsToDb(SQLiteDatabase _db, JSONObject _jsonRegionsObj) {
        for (int i = 0; i < REGIONS.length; i++) {
            String insert = null;
            try{
                insert = "INSERT INTO " + mDbOpenHelper.REGIONS_TABLE_NAME + " (" +
                        mDbOpenHelper.COL_REGION_ID + ", " +
                        mDbOpenHelper.COL_REGION_NAME + ") VALUES (" +
                        "'" + REGIONS[i] +
                        "', '" + _jsonRegionsObj.getString(REGIONS[i]) + "')";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            _db.execSQL(insert);
        }
    }

    private void writeCitiesToDb(SQLiteDatabase _db, JSONObject _jsonCitiesObj) {
        for (int i = 0; i < CITIES.length; i++) {
            String insert = null;
            try {
                insert = "INSERT INTO " + mDbOpenHelper.CITIES_TABLE_NAME + " (" +
                        mDbOpenHelper.COL_CITY_ID + ", " +
                        mDbOpenHelper.COL_CITY_NAME + ") VALUES (" +
                        "'" + CITIES[i] +
                        "', '" + _jsonCitiesObj.getString(CITIES[i]) + "')";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            _db.execSQL(insert);
        }
    }

}
