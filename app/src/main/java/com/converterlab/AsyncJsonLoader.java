package com.converterlab;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by R-Tem on 14.09.2015.
 */
public class AsyncJsonLoader extends AsyncTaskLoader<String> {

    public static final String TAG = "AsyncJsonLoader";
    public static final String KEY_JSON_URL = "jsonUrl";
    public static final int CONNECT_TIMEOUT = 5000;

    private String mJsonFileUrl;
    private String mJsonFilePath;

    public AsyncJsonLoader(Context _context, Bundle _bundle) {
        super(_context);
        mJsonFileUrl = _bundle.getString(KEY_JSON_URL);
        mJsonFilePath = this.getContext().getFilesDir() + "/currency-cash.json";
        Log.d(MainActivity.LOG_TAG, TAG + " #created");

    }

    @Override
    public String loadInBackground() {
        Log.d(MainActivity.LOG_TAG, TAG + "#loadInBackground#loading");

        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(mJsonFileUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            Log.d(MainActivity.LOG_TAG, "HttpURLConnection#" + connection.getResponseCode());

            // download the file
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                outputStream = new FileOutputStream(mJsonFilePath);

                int len;
                byte data[] = new byte[4096];
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
                if (new File(mJsonFilePath).exists()){
                    Log.d(MainActivity.LOG_TAG, "loadInBackground#file exists");
                    return mJsonFilePath;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

//    private void checkConnection() {
//
//        String status = "";
//
//        ConnectivityManager connMgr = (ConnectivityManager)
//                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected()) {
//            status += "Connected: " + networkInfo.getTypeName() + '\n';
//        } else {
//            status += "No connection" + '\n';
//        }
//
//        NetworkInfo networkInfoWifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo networkInfoMobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//        status += "Wifi: " + (networkInfoWifi != null && networkInfoWifi.isConnected()) + '\n';
//        status += "Mobile: " + (networkInfoMobile != null && networkInfoMobile.isConnected());
//
//        MainActivity mainActivity = (MainActivity) getActivity();
//        mainActivity.getToast().setText(status);
//        mainActivity.getToast().show();
//    }

}
