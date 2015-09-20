package com.converterlab;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.converterlab.jsonparser.AsyncJsonParser;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<String>{

    public static final String LOG_TAG = "Samoshkin";
    private static final int JSON_LOADER_ID = 1;
    private static final int JSON_PARSER_ID = 2;
    private Bundle mBundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBundle.putString(AsyncJsonLoader.KEY_JSON_URL, getString(R.string.json_file_uri));
        getLoaderManager().initLoader(JSON_LOADER_ID, mBundle, this).forceLoad();
    }

    @Override
    public Loader<String> onCreateLoader(int _id, Bundle _args) {
        Loader<String> loader = null;
        switch (_id){
            case JSON_LOADER_ID:
                loader = new AsyncJsonLoader(this, _args);
                Log.d(LOG_TAG, "onCreateLoader: AsyncJsonLoader " + loader.hashCode());
                break;
            case JSON_PARSER_ID:
                loader = new AsyncJsonParser(this, _args);
                Log.d(LOG_TAG, "onCreateLoader: AsyncJsonParser " + loader.hashCode());
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> _loader, String _result) {
        switch (_loader.getId()){
            case JSON_LOADER_ID:
                Log.d(LOG_TAG, "onLoadFinished for loader " + _loader.hashCode()
                        + ", result = " + _result);

                mBundle.clear();
                mBundle.putString(AsyncJsonParser.KEY_JSON_FILE_PATH, _result);
                getLoaderManager().initLoader(JSON_PARSER_ID, mBundle, this).forceLoad();
                break;
            case JSON_PARSER_ID:
                Log.d(LOG_TAG, "onLoadFinished for loader " + _loader.hashCode()
                        + ", result = " + _result);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> _loader) {
        Log.d(LOG_TAG, "onLoaderReset for loader " + _loader.hashCode());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
