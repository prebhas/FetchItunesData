package com.example.preethibhaskar.itunesforandroid.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.util.LruCache;
import android.util.Base64;
import android.widget.ImageView;

import com.example.preethibhaskar.itunesforandroid.data.Itunes;

import java.io.ByteArrayOutputStream;


/**
 * General util class
 */
public class Util {
    // Lru cache to cache the bitmaps
    private static LruCache<String, Bitmap> mMemoryCache;

    /**
     * Asynchronously download the bitmap from the Url and set that to imageview
     *
     * @param data      itunes data with title, description, image URL , collection name and price
     * @param imageView
     */
    public static void download(Context context ,Itunes data, ImageView imageView,int pos) {
        com.example.preethibhaskar.itunesforandroid.loaders.BitmapDownloader task = new com.example.preethibhaskar.itunesforandroid.loaders.BitmapDownloader(context ,imageView, pos);
        task.execute(data);
    }

    /**
     * Util method which checks whether devices is connected
     * @param context
     * @return whether device connected or not
     */
    public static  boolean hasNetworkConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isNetworkConnected =  activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isNetworkConnected;

    }

    /**
     * Implement cache for bitmaps so that we do not have to download the bitmaps multiple times
     * download once and store in cache .
     */
    public static void initializeCache() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

    }

    /**
     * Add
     * @param k      key which is the position in int
     * @param bitmap
     */
    public static void addBitmapToMemoryCache(int k, Bitmap bitmap) {
        String key = Integer.toString(k);
        if (getBitmapFromMemCache(k) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public static  Bitmap getBitmapFromMemCache(int key) {
        //Log.i(Constants.LOG_TAG," storing image in cache "+mMemoryCache.get(key));
        return mMemoryCache.get(Integer.toString(key));
    }

    /**
     * Encode a Bitmap into Base64
     * @param bitmap Bitmap
     * @return String
     */
    public static String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
    }

    /**
     * Decode the encoded string and return the bitmap
     * @param encoded
     * @return bitmap
     */
    public static Bitmap decodeBitmapFromBase64(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
