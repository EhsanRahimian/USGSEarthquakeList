package com.nicootech.usgsearthquakelist;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import java.util.List;
import static com.nicootech.usgsearthquakelist.MainActivity.LOG_TAG;

public class MyLoader extends AsyncTaskLoader<List<Earthquake>>
{
    private final String url;

    public MyLoader(Context context, String url) {
        super(context);
        this.url=url;
    }

    @Override
    public void onStartLoading() {
        Log.e(LOG_TAG,"On start Loading Worked");
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.e(LOG_TAG,"Load in background");
        if (url == null) {
            return null;
        }
        return QueryUtils.fetchingFromJson(url);
    }
}