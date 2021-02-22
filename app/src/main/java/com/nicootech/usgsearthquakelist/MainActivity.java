package com.nicootech.usgsearthquakelist;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;

import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    //URL for earthquake data from the USGS dataSet
    private static final String URL_JSON = "https://earthquake.usgs.gov/fdsnws/event/1";

    public static final String LOG_TAG = MainActivity.class.getName();

    //Adapter for the list of earthquakes
    private MyAdapter adapter;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView myListEarthquake = findViewById(R.id.list);

        TextView empty = findViewById(R.id.noeq);
        myListEarthquake.setEmptyView(empty);

        adapter = new MyAdapter(this, new ArrayList<>());

        myListEarthquake.setAdapter(adapter);

        myListEarthquake.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent specific = new Intent(Intent.ACTION_VIEW, Uri.parse(adapter.getItem(position).getUrl()));
                startActivity(specific);
            }
        });

        ConnectivityManager connectManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Getting details on current data
        NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();

        //fetch data if you have internet
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(1, null, this).forceLoad();
            Log.v("my app" , "init Loader");
        } else {
            // Otherwise, display error
            // Error message will be visible
            View loadingIndicator = findViewById(R.id.progress);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            empty.setText("Check your Internet Connection!");
        }


    }




    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd"); // set the format
        Date date = new Date(System.currentTimeMillis()); // current date
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, -30);
        Date today30 = c.getTime();
        String formatURL = String.format("/query?format=geojson&eventtype=earthquake&orderby=time&minmag=0.2&starttime=%s",formatter.format(today30));
        return new MyLoader(this,URL_JSON+formatURL);



    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        ProgressBar pBar = (ProgressBar)findViewById(R.id.progress);
        pBar.setVisibility(View.GONE);

        adapter.clear();
        // checking valid list 
        if(data != null && !data.isEmpty())
        {
            adapter.addAll(data);
        }
        Log.e(LOG_TAG,"On Load Finished");

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.e(LOG_TAG,"On LoaderReset");
        adapter.clear();
    }
}