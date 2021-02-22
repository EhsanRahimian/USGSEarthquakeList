package com.nicootech.usgsearthquakelist;

import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import static com.nicootech.usgsearthquakelist.MainActivity.LOG_TAG;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import  android.util.Log;

public class QueryUtils {

    private QueryUtils() {
    }
    
    public static ArrayList<Earthquake> extractEarthquakesData(String JSON) {

        if(TextUtils.isEmpty(JSON))
        {
            return null;
        }
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        try {


            JSONObject mJson = new JSONObject(JSON);
            JSONArray jsonArray = mJson.getJSONArray("features");
            
            for(int i=0;i<jsonArray.length();i++) {
                
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject jsonObject1 = jsonObject.getJSONObject("properties");
                
                double mag = Double.parseDouble(jsonObject1.getString("mag"));
                String place = jsonObject1.getString("place");
                long time = Long.parseLong(jsonObject1.getString("time"));
                String url = jsonObject1.getString("url");
                Earthquake earthquake = new Earthquake(mag, place, time,url);
                earthquakes.add(earthquake);
            }

        }
        catch (JSONException e) {
            Log.e("QueryUtils", "Error parsing JSON");
            e.printStackTrace();
        }

        // Return the list of earthquakes
        return earthquakes;
    }
    private static URL createUrl(String stringurl)
    {
        URL url = null;
        try
        {
            url = new URL (stringurl);
        }
        catch(MalformedURLException e)
        {
            Log.e(LOG_TAG,"Problem in URL",e);
        }
        return url;
    }
    private static String makeHTTPRequest(URL url)throws IOException
    {
        String JSONRes="";

        if(url == null)
        {
            return JSONRes;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200)
            {
                inputStream=urlConnection.getInputStream();
                JSONRes=readingStream(inputStream);
            }
            else
            {
                Log.e(LOG_TAG,"Error response Code: "+urlConnection.getResponseCode());
            }
        }
        catch(IOException e)
        {
            Log.e(LOG_TAG,"Error retrieving the JSON Response",e);
        }
        finally
        {
            if(urlConnection!=null)
            {
                urlConnection.disconnect();
            }
            if(inputStream!=null)
            {
                inputStream.close();
            }
        }
        return JSONRes;
    }
    private static String readingStream(InputStream inputStream)throws IOException
    {
        StringBuilder stringBuilder=new StringBuilder();
        if(inputStream!=null)
        {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line!=null)
            {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }
    public static List<Earthquake> fetchingFromJson(String url)
    {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url1 = createUrl(url);
        String JSONResponse = null;

        try
        {
            JSONResponse = makeHTTPRequest(url1);
        }
        catch(IOException e)
        {
            Log.e(LOG_TAG,"Error  HTTP request",e);
        }
        return extractEarthquakesData(JSONResponse);
    }


}