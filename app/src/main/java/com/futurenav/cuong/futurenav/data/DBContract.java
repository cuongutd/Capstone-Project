package com.futurenav.cuong.futurenav.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Cuong on 8/26/2015.
 */
public class DBContract {
    public static final String CONTENT_AUTHORITY = "com.futurenav.cuong.futurenav";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SCHOOL = "school";
    public static final String PATH_FAVORITE = "favorite";
    public static final String QUERY_PARAM_KEY_SCHOOL_ZIPCODE = "zipcode";
    public static final String QUERY_PARAM_KEY_SCHOOL_WEBSITE = "website";
    public static final String QUERY_PARAM_KEY_SCHOOL_CORD_LAT = "lat";
    public static final String QUERY_PARAM_KEY_SCHOOL_CORD_LONG = "lon";


    public static final class SchoolEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SCHOOL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHOOL;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHOOL;

        // Table name
        public static final String TABLE_NAME = "school";
        public static final String COLUMN_SCHOOL_NAME = "name";
        public static final String COLUMN_WEBSITE = "website";
        public static final String COLUMN_LEVEL = "levels";
        public static final String COLUMN_FORMAT = "format";
        public static final String COLUMN_FORMAT_DESC = "format_description";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_LANGUAGE = "languages";
        public static final String COLUMN_SCHOOL_TYPE="money_needed";
        public static final String COLUMN_ONLINE_ONLY = "online_only";
        public static final String COLUMN_NO_STUDENT = "number_of_student";
        public static final String COLUMN_CONTACT_NAME = "contact_name";
        public static final String COLUMN_CONTACT_NUMBER = "contact_number";
        public static final String COLUMN_CONTACT_EMAIL = "contact_email";
        public static final String COLUMN_LAT = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_STREET = "street";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_ZIP = "zip";
        public static final String COLUMN_PUBLISHED = "published";
        public static final String COLUMN_UPDATED_AT = "updated_at";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_SOURCE = "source";

        public static Uri buildSchoolUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildSchoolUriWithZip(String zip) {
            return CONTENT_URI.buildUpon().appendQueryParameter(QUERY_PARAM_KEY_SCHOOL_ZIPCODE, zip).build();
        }

        public static Uri buildSchoolUriWithLatLong(Double lat, Double lon) {
            return CONTENT_URI.buildUpon().appendPath("QUERY").appendQueryParameter(QUERY_PARAM_KEY_SCHOOL_CORD_LAT, lat.toString())
                    .appendQueryParameter(QUERY_PARAM_KEY_SCHOOL_CORD_LONG, lon.toString()).build();
        }
        public static Uri buildSchoolUriWithWebsite(String website) {
            return CONTENT_URI.buildUpon().appendQueryParameter(QUERY_PARAM_KEY_SCHOOL_WEBSITE, website).build();
        }

        public static String getZip(Uri uri){
            return uri.getQueryParameter(QUERY_PARAM_KEY_SCHOOL_ZIPCODE);
        }

        public static String getWebsite(Uri uri){
            return uri.getQueryParameter(QUERY_PARAM_KEY_SCHOOL_WEBSITE);

        }
        public static String getLat(Uri uri){
            return uri.getQueryParameter(QUERY_PARAM_KEY_SCHOOL_CORD_LAT);
        }
        public static String getLong(Uri uri){
            return uri.getQueryParameter(QUERY_PARAM_KEY_SCHOOL_CORD_LONG);
        }

    }

    public static final class FavoriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;

        // Table name
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_SCHOOL_NAME = "name";
        public static final String COLUMN_WEBSITE = "website";
        public static final String COLUMN_LEVEL = "levels";
        public static final String COLUMN_FORMAT = "format";
        public static final String COLUMN_FORMAT_DESC = "format_description";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_LANGUAGE = "languages";
        public static final String COLUMN_SCHOOL_TYPE="money_needed";
        public static final String COLUMN_ONLINE_ONLY = "online_only";
        public static final String COLUMN_NO_STUDENT = "number_of_student";
        public static final String COLUMN_CONTACT_NAME = "contact_name";
        public static final String COLUMN_CONTACT_NUMBER = "contact_number";
        public static final String COLUMN_CONTACT_EMAIL = "contact_email";
        public static final String COLUMN_LAT = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_STREET = "street";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_ZIP = "zip";
        public static final String COLUMN_PUBLISHED = "published";
        public static final String COLUMN_UPDATED_AT = "updated_at";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_SOURCE = "source";

        public static Uri buildFavoriteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}
