package com.futurenav.cuong.futurenav.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cuong on 8/25/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "school.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_SCHOOL_TABLE = "CREATE TABLE " + DBContract.SchoolEntry.TABLE_NAME + "(" +
                DBContract.SchoolEntry._ID + " INTEGER PRIMARY KEY," +
                DBContract.SchoolEntry.COLUMN_SCHOOL_NAME + " TEXT NOT NULL, " +
                DBContract.SchoolEntry.COLUMN_WEBSITE + " TEXT UNIQUE NOT NULL, " +
                DBContract.SchoolEntry.COLUMN_LEVEL + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_FORMAT + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_FORMAT_DESC + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_GENDER + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_DESC + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_LANGUAGE + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_SCHOOL_TYPE + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_ONLINE_ONLY + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_NO_STUDENT + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_CONTACT_NAME + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_CONTACT_NUMBER + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_CONTACT_EMAIL + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_LAT + " REAL, " +
                DBContract.SchoolEntry.COLUMN_LONGITUDE + " REAL, " +
                DBContract.SchoolEntry.COLUMN_STREET + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_CITY + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_STATE + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_ZIP + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_PUBLISHED + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_UPDATED_AT + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_COUNTRY + " TEXT, " +
                DBContract.SchoolEntry.COLUMN_SOURCE + " TEXT " +
                ");";

        final String CREATE_FAVORITE_TABLE = "CREATE TABLE " + DBContract.FavoriteEntry.TABLE_NAME + "(" +
                DBContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY," +
                DBContract.FavoriteEntry.COLUMN_SCHOOL_NAME + " TEXT NOT NULL, " +
                DBContract.FavoriteEntry.COLUMN_WEBSITE + " TEXT UNIQUE NOT NULL, " +
                DBContract.FavoriteEntry.COLUMN_LEVEL + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_FORMAT + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_FORMAT_DESC + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_GENDER + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_DESC + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_LANGUAGE + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_SCHOOL_TYPE + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_ONLINE_ONLY + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_NO_STUDENT + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_CONTACT_NAME + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_CONTACT_NUMBER + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_CONTACT_EMAIL + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_LAT + " REAL, " +
                DBContract.FavoriteEntry.COLUMN_LONGITUDE + " REAL, " +
                DBContract.FavoriteEntry.COLUMN_STREET + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_CITY + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_STATE + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_ZIP + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_PUBLISHED + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_UPDATED_AT + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_COUNTRY + " TEXT, " +
                DBContract.FavoriteEntry.COLUMN_SOURCE + " TEXT " +
                ");";


        db.execSQL(CREATE_SCHOOL_TABLE);
        db.execSQL(CREATE_FAVORITE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.SchoolEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
