package com.example.preethibhaskar.itunesforandroid.loaders;

/**
 * Created by root on 10/30/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.preethibhaskar.itunesforandroid.data.Itunes;
import com.example.preethibhaskar.itunesforandroid.data.ItunesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.preethibhaskar.itunesforandroid.utils.Constants;
import com.example.preethibhaskar.itunesforandroid.utils.Util;

/**
 * Created by Preethi Bhaskar on 10/28/15.
 */
public  class DownloadItunesDataLoader extends android.support.v4.content.AsyncTaskLoader<List<Itunes>> {

    private Context mContext;

    public DownloadItunesDataLoader(Context context) {
        super(context);
        mContext = context;
        Util.initializeCache();
    }

    @Override
    public List<Itunes> loadInBackground() {
        if(!Util.hasNetworkConnection(mContext)) {
            // No internet connection query the content provider
            Cursor cursor = mContext.getContentResolver().query(ItunesContract.ItunesEntry.CONTENT_URI,null,null,null,null);
            //return the itunes data from cursor
            Log.i(Constants.LOG_TAG, " load in background cursor " + cursor.getCount());
            if(cursor != null && cursor.getCount()>0) {
                return getDataFromCursor(cursor);

            } else {
                // Ask the user to connect to internet to fetch the itunes data
                return null;
            }

        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            // Pass the Url of the itunes api
            URL url = new URL(Constants.URL.toString());

            // Create the request to itunes api , and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            String itunesJsonStr = buffer.toString();
            return getItunesDataFromJson(itunesJsonStr);
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error "+ e);
            e.printStackTrace();
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(Constants.LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

    /*
    *
     This helper  method take the json string and parses the json  and returns list containing
     itune objects
     */
    private List<Itunes> getItunesDataFromJson(String jsonStr) throws JSONException {
        String results = "results";
        ArrayList<Itunes> itunesList = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            // get the json array to get different json objects
            JSONArray jsonarray = obj.getJSONArray(results);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject itunesJson = jsonarray.getJSONObject(i);

                Itunes data = new Itunes();
                data.setTitle(itunesJson.getString(ItunesContract.ItunesEntry.TITLE));
                data.setImageUrl(itunesJson.getString(ItunesContract.ItunesEntry.IMAGE));
                data.setDescription(itunesJson.getString(ItunesContract.ItunesEntry.DESCRIPTION));
                data.setCollectionPrice(itunesJson.getString(ItunesContract.ItunesEntry.COLLECTION_PRICE));
                data.setTrackPrice(itunesJson.getString(ItunesContract.ItunesEntry.TRACK_PRICE));
                data.setCollectionName(itunesJson.getString(ItunesContract.ItunesEntry.COLLECTION_NAME));

                itunesList.add(data);
            }

        } catch (JSONException e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        // insert into the content provider
        insertIntoContentProvider(itunesList);
        return itunesList;

    }

    /**
     * Insert the content values into the content provider
     *
     * @param data itunes data
     */
    private void insertIntoContentProvider(ArrayList<Itunes> data) {
        // First delete whatever data is there in content provider
        int m = mContext.getContentResolver().delete(ItunesContract.ItunesEntry.CONTENT_URI, null, null);
        // Now insert into the content provider
        ContentValues[] bulkToInsert;
        List<ContentValues> mValueList = new ArrayList<>();
        for (Itunes temp : data) {
            ContentValues mNewValues = new ContentValues();
            mNewValues.put(ItunesContract.ItunesEntry.TITLE, temp.getTitle());
            mNewValues.put(ItunesContract.ItunesEntry.DESCRIPTION, temp.getDescription());
            mNewValues.put(ItunesContract.ItunesEntry.COLLECTION_PRICE, temp.getCollectionPrice());
            mNewValues.put(ItunesContract.ItunesEntry.COLLECTION_NAME, temp.getCollectionName());
            mNewValues.put(ItunesContract.ItunesEntry.IMAGE, temp.getImageUrl());
            mValueList.add(mNewValues);
        }

        bulkToInsert = new ContentValues[mValueList.size()];
        mValueList.toArray(bulkToInsert);

        mContext.getContentResolver().bulkInsert(ItunesContract.ItunesEntry.CONTENT_URI,
                bulkToInsert);

    }

    // return the cursor
    private ArrayList<Itunes>getDataFromCursor (Cursor cursor) {
        ArrayList<Itunes> itunesData = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Itunes data = new Itunes();
            data.setTitle(cursor.getString(cursor.getColumnIndex(ItunesContract.ItunesEntry.TITLE)));
            data.setDescription(cursor.getString(cursor.getColumnIndex(ItunesContract.ItunesEntry.DESCRIPTION)));
            data.setCollectionName(cursor.getString(cursor.getColumnIndex(ItunesContract.ItunesEntry.COLLECTION_NAME)));
            data.setCollectionPrice(cursor.getString(cursor.getColumnIndex(ItunesContract.ItunesEntry.COLLECTION_PRICE)));
            data.setImageUrl(cursor.getString(cursor.getColumnIndex(ItunesContract.ItunesEntry.IMAGE)));
            itunesData.add(data);
            cursor.moveToNext();

        }
        return itunesData;
    }
}
