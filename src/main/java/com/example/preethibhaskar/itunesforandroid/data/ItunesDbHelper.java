package com.example.preethibhaskar.itunesforandroid.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 10/29/15.
 */
public class ItunesDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "itunes.db";

    public ItunesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ITUNES_TABLE = "CREATE TABLE " + ItunesContract.ItunesEntry.TABLE_NAME + " (" +
                ItunesContract.ItunesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                ItunesContract.ItunesEntry.TITLE + " TEXT  , " +
                ItunesContract.ItunesEntry.DESCRIPTION + " TEXT  , " +
                ItunesContract.ItunesEntry.COLLECTION_NAME + " TEXT  , " +
                ItunesContract.ItunesEntry.COLLECTION_PRICE + " TEXT  , " +
                ItunesContract.ItunesEntry.IMAGE + " TEXT   " + " );";
        db.execSQL(SQL_CREATE_ITUNES_TABLE);

    }
    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        db.execSQL("DROP TABLE IF EXISTS " + ItunesContract.ItunesEntry.TABLE_NAME);
        onCreate(db);
    }
}
