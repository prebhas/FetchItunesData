package com.example.preethibhaskar.itunesforandroid.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by root on 10/28/15.
 */
public class ItunesContract {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.preethibhaskar.itunesforandroid";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //Possible paths (appended to base content URI for possible URI's)
    // For instance, com.example.raghavendrashreedhara.list/list/ is a valid path for
    // looking at list data.
    public static final String PATH_ITUNES_DATA = "itunes_data";

    /* Inner class that defines the table contents of the location table */
    public static final class ItunesEntry implements BaseColumns {
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITUNES_DATA;
        public static final String TABLE_NAME = "itunes";
        public static final String _ID = "_id";
        public static final String IMAGE = "artworkUrl100";
        public static final String TITLE = "trackName";
        public static final String DESCRIPTION = "primaryGenreName";
        public static final String COLLECTION_NAME = "collectionName";
        public static final String COLLECTION_PRICE = "collectionPrice";
        public static final String TRACK_PRICE = "trackPrice";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITUNES_DATA).build();

        public static Uri buildList(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}

