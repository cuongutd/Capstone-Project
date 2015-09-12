package com.futurenav.cuong.futurenav.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Cuong on 8/26/2015.
 */
public class DBContentProvider extends ContentProvider {

    private DBHelper mDBHelper;

    //URL matcher
    //app calls provider passing in URI, needs to know what action the uri going to do
    public static final int SCHOOL = 100;
    public static final int SCHOOL_WITH_QUERY_PARAMS = 101;
    public static final int FAVORITE = 200;


    private String[] favColumns = {
    DBContract.FavoriteEntry.COLUMN_SCHOOL_NAME,
    DBContract.FavoriteEntry.COLUMN_WEBSITE,
    DBContract.FavoriteEntry.COLUMN_LEVEL,
    DBContract.FavoriteEntry.COLUMN_FORMAT,
    DBContract.FavoriteEntry.COLUMN_FORMAT_DESC,
    DBContract.FavoriteEntry.COLUMN_GENDER,
    DBContract.FavoriteEntry.COLUMN_DESC,
    DBContract.FavoriteEntry.COLUMN_LANGUAGE,
    DBContract.FavoriteEntry.COLUMN_SCHOOL_TYPE,
    DBContract.FavoriteEntry.COLUMN_ONLINE_ONLY,
    DBContract.FavoriteEntry.COLUMN_NO_STUDENT,
    DBContract.FavoriteEntry.COLUMN_CONTACT_NAME,
    DBContract.FavoriteEntry.COLUMN_CONTACT_NUMBER,
    DBContract.FavoriteEntry.COLUMN_CONTACT_EMAIL,
    DBContract.FavoriteEntry.COLUMN_LAT,
    DBContract.FavoriteEntry.COLUMN_LONGITUDE,
    DBContract.FavoriteEntry.COLUMN_STREET,
    DBContract.FavoriteEntry.COLUMN_CITY,
    DBContract.FavoriteEntry.COLUMN_STATE,
    DBContract.FavoriteEntry.COLUMN_ZIP,
    DBContract.FavoriteEntry.COLUMN_PUBLISHED,
    DBContract.FavoriteEntry.COLUMN_UPDATED_AT,
    DBContract.FavoriteEntry.COLUMN_COUNTRY,
    DBContract.FavoriteEntry.COLUMN_SOURCE,
            BaseColumns._ID
    };

    private UriMatcher mUriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DBContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, DBContract.PATH_SCHOOL, SCHOOL);
        matcher.addURI(authority, DBContract.PATH_SCHOOL + "/*/", SCHOOL_WITH_QUERY_PARAMS);

        matcher.addURI(authority, DBContract.PATH_FAVORITE, FAVORITE);

        return matcher;
    }

    private static final String ZIPCODE_SELECTION = DBContract.SchoolEntry.COLUMN_ZIP + " = ? ";
    private static final String WEBSITE_SELECTION = DBContract.SchoolEntry.COLUMN_WEBSITE + " = ?";
    private static final String CORD_SELECTION = DBContract.SchoolEntry.COLUMN_LAT + " >= ? AND " +
            DBContract.SchoolEntry.COLUMN_LAT + " <= ? AND " +
            DBContract.SchoolEntry.COLUMN_LONGITUDE + " >= ? AND " +
            DBContract.SchoolEntry.COLUMN_LONGITUDE + " <= ?";

    private Cursor buildSchoolCursor(Uri uri, String[] projection, String sortOrder) {

        String zip = DBContract.SchoolEntry.getZip(uri);
        String website = DBContract.SchoolEntry.getWebsite(uri);
        String lat = DBContract.SchoolEntry.getLat(uri);
        String lon = DBContract.SchoolEntry.getLong(uri);

        String[] selectionArgs = null;
        String selection = null;

        if (!TextUtils.isEmpty(zip)) {
            selectionArgs = new String[]{zip};
            selection = ZIPCODE_SELECTION;
        }
        if (!TextUtils.isEmpty(website)) {
            selectionArgs = new String[]{website};
            selection = WEBSITE_SELECTION;
        }

        if (!TextUtils.isEmpty(lat)) {
            double dLat = Double.valueOf(lat);
            double dLong = Double.valueOf(lon);


            selectionArgs = new String[]{String.valueOf(dLat - 0.1d), String.valueOf(dLat + 0.1d), String.valueOf(dLong - 0.1d), String.valueOf(dLong + 0.1d)};
            selection = CORD_SELECTION;
        }

        return mDBHelper.getReadableDatabase().query(
                DBContract.SchoolEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public boolean onCreate() {

        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor = null;

        Log.d("URI: ", uri.toString());

        int match = mUriMatcher.match(uri);
        switch (match) {
            case FAVORITE:
                Log.d("Match: ", " favorite");
                if (projection == null)
                    projection = favColumns;
                retCursor = mDBHelper.getReadableDatabase().query(
                        DBContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case SCHOOL:
                Log.d("Match: ", " SCHOOL");
                retCursor = mDBHelper.getReadableDatabase().query(
                        DBContract.SchoolEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case SCHOOL_WITH_QUERY_PARAMS:
                Log.d("Match: ", " SCHOOL_WITH_QUERY_PARAMS");
                retCursor = buildSchoolCursor(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {

        int match = mUriMatcher.match(uri);

        switch (match) {
            case FAVORITE:
                return DBContract.FavoriteEntry.CONTENT_TYPE;
            case SCHOOL:
                return DBContract.SchoolEntry.CONTENT_TYPE;
            case SCHOOL_WITH_QUERY_PARAMS:
                return DBContract.SchoolEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mDBHelper.getWritableDatabase();

        final int match = mUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case FAVORITE: {
                long _id = db.insert(DBContract.FavoriteEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DBContract.FavoriteEntry.buildFavoriteUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SCHOOL: {
                long _id = db.insert(DBContract.SchoolEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DBContract.SchoolEntry.buildSchoolUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int rowsDeleted;

        if (selection == null)
            selection = "1"; //where condition on first column which is id

        switch (match) {
            case FAVORITE: {
                rowsDeleted = db.delete(DBContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case SCHOOL: {
                rowsDeleted = db.delete(DBContract.SchoolEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case FAVORITE: {
                rowsUpdated = db.update(DBContract.FavoriteEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case SCHOOL: {
                rowsUpdated = db.update(DBContract.SchoolEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case SCHOOL:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DBContract.SchoolEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
