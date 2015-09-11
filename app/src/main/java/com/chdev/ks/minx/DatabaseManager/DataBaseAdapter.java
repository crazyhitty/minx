package com.chdev.ks.minx.DatabaseManager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Kartik_ch on 8/29/2015.
 */
public class DataBaseAdapter {
    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public DataBaseAdapter(Context context) {
        mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public DataBaseAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e("Error", mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DataBaseAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e("OpenDb", "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    public Cursor selectQuery(String query) {
        Cursor c1 = null;
        try {

            if (mDb.isOpen()) {
                mDb.close();

            }
            mDb = mDbHelper.getWritableDatabase();
            c1 = mDb.rawQuery(query, null);

        } catch (Exception e) {

            System.out.println("DATABASE ERROR " + e);

        }
        return c1;

    }

    public void executeQuery(String query) {
        try {

            if (mDb.isOpen()) {
                mDb.close();
            }

            mDb = mDbHelper.getWritableDatabase();
            mDb.execSQL(query);

        } catch (Exception e) {

            System.out.println("DATABASE ERROR " + e);
        }
    }
}
