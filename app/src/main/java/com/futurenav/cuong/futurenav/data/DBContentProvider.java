package com.futurenav.cuong.futurenav.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Cuong on 8/26/2015.
 */
public class DBContentProvider extends ContentProvider {

    private DBHelper mDBHelper;

    //URL matcher
    //app calls provider passing in URI, needs to know what action the uri going to do
    public static final int SCHOOL = 100;
    public static final int SCHOOL_WITH_ZIPCODE = 101;
    public static final int SCHOOL_WITH_ZIPCODE_WEBSITE = 102;
    public static final int FAVORITE = 200;

    private UriMatcher mUriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DBContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, DBContract.PATH_SCHOOL, SCHOOL);
        matcher.addURI(authority, DBContract.PATH_SCHOOL + "/*", SCHOOL_WITH_ZIPCODE);
        matcher.addURI(authority, DBContract.PATH_SCHOOL + "/*/#", SCHOOL_WITH_ZIPCODE_WEBSITE);

        matcher.addURI(authority, DBContract.PATH_FAVORITE, FAVORITE);

        return matcher;
    }

    private static final String ZIPCODE_SELECTION = DBContract.SchoolEntry.COLUMN_ZIP + " = ? ";
    private static final String ZIPCODE_WEBSITE_SELECTION = DBContract.SchoolEntry.COLUMN_ZIP + " = ? AND " + DBContract.SchoolEntry.COLUMN_WEBSITE + " = ? ";

    private Cursor buildSchoolCursor(Uri uri, String[] projection, String sortOrder) {

        String zip = DBContract.SchoolEntry.getZip(uri);
        String website = DBContract.SchoolEntry.getWebsite(uri);
        String[] selectionArgs;
        String selection;

        if (TextUtils.isEmpty(website)) {
            selectionArgs = new String[]{zip};
            selection = ZIPCODE_SELECTION;
        } else {
            selectionArgs = new String[]{zip, website};
            selection = ZIPCODE_WEBSITE_SELECTION;

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

        int match = mUriMatcher.match(uri);
        switch (match) {
            case FAVORITE:
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
                retCursor = mDBHelper.getReadableDatabase().query(
                        DBContract.SchoolEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case SCHOOL_WITH_ZIPCODE:
                retCursor = buildSchoolCursor(uri, projection, sortOrder);
                break;
            case SCHOOL_WITH_ZIPCODE_WEBSITE:
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
            case SCHOOL_WITH_ZIPCODE:
                return DBContract.SchoolEntry.CONTENT_TYPE;
            case SCHOOL_WITH_ZIPCODE_WEBSITE:
                return DBContract.SchoolEntry.CONTENT_ITEM_TYPE;
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
