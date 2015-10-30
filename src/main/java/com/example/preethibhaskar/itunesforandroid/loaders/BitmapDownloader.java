package com.example.preethibhaskar.itunesforandroid.loaders;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.preethibhaskar.itunesforandroid.data.Itunes;
import com.example.preethibhaskar.itunesforandroid.data.ItunesContract;
import com.example.preethibhaskar.itunesforandroid.utils.Constants;
import com.example.preethibhaskar.itunesforandroid.utils.Util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class downloads the bitmap from the given string image URL
 */
public class BitmapDownloader extends AsyncTask<Itunes, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    final int mPosition;
    final Context mContext;


    public BitmapDownloader(Context context, ImageView imageView, int position) {
        imageViewReference = new WeakReference<>(imageView);
        mPosition = position;
        mContext = context;
    }

    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(Itunes... params) {
        // params comes from the execute() call: params[0] is the url.
        if (params != null && params.length == 0) {
            return null;
        }

        return downloadBitmap(params[0].getImageUrl());
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * This method downloads the image as bitmap from the given URL
     *
     * @param stringUrl
     * @return
     */
    private Bitmap downloadBitmap(String stringUrl) {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            // pass the utl of the bitmap to be downloaded
            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            inputStream = urlConnection.getInputStream();
            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            // Add the bitmap into the content provider
            Util.addBitmapToMemoryCache(mPosition, bitmap);
            updateTheContentProviderwithImageBitmap(bitmap);
            return bitmap;


        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error " + e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * This method updates the content provider with blob of bitmap
     *
     * @param bitmap
     */
    private void updateTheContentProviderwithImageBitmap(Bitmap bitmap) {
        ContentValues values = new ContentValues();

        values.put(ItunesContract.ItunesEntry.IMAGE, Util.encodeBitmapToBase64(bitmap));
        int i = mContext.getContentResolver().update(
                ItunesContract.ItunesEntry.CONTENT_URI,
                values,
                ItunesContract.ItunesEntry._ID + " =? ",
                new String[]{Integer.toString(mPosition)}
        );
    }


}



