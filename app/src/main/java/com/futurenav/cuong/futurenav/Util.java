package com.futurenav.cuong.futurenav;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.futurenav.cuong.futurenav.data.DBContract;
import com.futurenav.cuong.futurenav.model.School;

/**
 * Created by Cuong on 8/25/2015.
 */
public class Util {

    public static final String EXTRA_SCHOOL = "SCHOOL";
    public static final String TEXT_SPLITTER = ", ";
    public static final String INITIAL_LOAD_PREF_KEY = "INITIAL_LOAD";
    public static final String INITIAL_LOAD_NOT_STARTED = "N";
    public static final String INITIAL_LOAD_FAILED = "F";
    public static final String INITIAL_LOAD_INPROGRESS = "I";
    public static final String INITIAL_LOAD_DONE = "D";
    public static final String SAVE_INSTANT_SCHOOL = "SI_SCHOOL";
    public static final String SAVE_INSTANT_SEARCH_MODE = "SI_SEARCHMODE";
    public static final String SAVE_INSTANT_LAT = "SI_LAT";
    public static final String SAVE_INSTANT_LONG = "SI_LONG";


    public static String formAddress(String street, String city, String state, String zip){
        return street + TEXT_SPLITTER + city  + TEXT_SPLITTER + state + " " + zip;
    }

    public static ContentValues convertFromSchoolToCV(School source){

        ContentValues dest = new ContentValues();

        dest.put(DBContract.SchoolEntry.COLUMN_SCHOOL_NAME, source.getName());
        dest.put(DBContract.SchoolEntry.COLUMN_WEBSITE, source.getWebsite());

        if (source.getLevels() != null)//happens only when reading json from code.org
            dest.put(DBContract.SchoolEntry.COLUMN_LEVEL, TextUtils.join(Util.TEXT_SPLITTER, source.getLevels()));
        else
            dest.put(DBContract.SchoolEntry.COLUMN_LEVEL, source.getsLevels());

        dest.put(DBContract.SchoolEntry.COLUMN_FORMAT, source.getFormat());
        dest.put(DBContract.SchoolEntry.COLUMN_FORMAT_DESC, source.getFormat_description());
        dest.put(DBContract.SchoolEntry.COLUMN_GENDER, source.getGender());
        dest.put(DBContract.SchoolEntry.COLUMN_DESC, source.getDescription());

        if (source.getLanguages() != null)//happens only when reading json from code.org
            dest.put(DBContract.SchoolEntry.COLUMN_LANGUAGE, TextUtils.join(Util.TEXT_SPLITTER, source.getLanguages()));
        else
            dest.put(DBContract.SchoolEntry.COLUMN_LANGUAGE, source.getsLanguages());

        dest.put(DBContract.SchoolEntry.COLUMN_SCHOOL_TYPE, source.getMoney_needed());
        dest.put(DBContract.SchoolEntry.COLUMN_ONLINE_ONLY, source.getOnline_only());
        dest.put(DBContract.SchoolEntry.COLUMN_NO_STUDENT, source.getNumber_of_students());
        dest.put(DBContract.SchoolEntry.COLUMN_CONTACT_NAME, source.getContact_name());
        dest.put(DBContract.SchoolEntry.COLUMN_CONTACT_NUMBER, source.getContact_number());
        dest.put(DBContract.SchoolEntry.COLUMN_CONTACT_EMAIL, source.getContact_email());
        dest.put(DBContract.SchoolEntry.COLUMN_LAT, source.getLatitude());
        dest.put(DBContract.SchoolEntry.COLUMN_LONGITUDE, source.getLongitude());
        dest.put(DBContract.SchoolEntry.COLUMN_STREET, source.getStreet());
        dest.put(DBContract.SchoolEntry.COLUMN_CITY, source.getCity());
        dest.put(DBContract.SchoolEntry.COLUMN_STATE, source.getState());
        dest.put(DBContract.SchoolEntry.COLUMN_ZIP, source.getZip());
        dest.put(DBContract.SchoolEntry.COLUMN_PUBLISHED, source.getPublished());
        dest.put(DBContract.SchoolEntry.COLUMN_UPDATED_AT, source.getUpdated_at());
        dest.put(DBContract.SchoolEntry.COLUMN_COUNTRY, source.getCountry());
        dest.put(DBContract.SchoolEntry.COLUMN_SOURCE, source.getSource());
        return dest;
    }

    public static School convertFromCursorToSchool(Cursor mCursor){
        School school = new School();
        school.set_id(String.valueOf(mCursor.getLong(mCursor.getColumnIndex(DBContract.SchoolEntry._ID))));
        school.setName(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_SCHOOL_NAME)));
        school.setWebsite(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_WEBSITE)));

        school.setsLevels(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_LEVEL)));

        school.setFormat(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_FORMAT)));
        school.setFormat_description(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_FORMAT_DESC)));
        school.setGender(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_GENDER)));
        school.setDescription(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_DESC)));

        school.setsLanguages(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_LANGUAGE)));
        school.setMoney_needed(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_SCHOOL_TYPE)));
        school.setOnline_only(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_ONLINE_ONLY)));
        school.setNumber_of_students(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_NO_STUDENT)));
        school.setContact_name(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_CONTACT_NAME)));
        school.setContact_number(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_CONTACT_NUMBER)));
        school.setContact_email(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_CONTACT_EMAIL)));
        school.setLatitude(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_LAT)));
        school.setLongitude(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_LONGITUDE)));
        school.setStreet(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_STREET)));
        school.setCity(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_CITY)));
        school.setState(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_STATE)));
        school.setZip(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_ZIP)));
        school.setPublished(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_PUBLISHED)));
        school.setUpdated_at(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_UPDATED_AT)));
        school.setCountry(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_COUNTRY)));
        school.setSource(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_SOURCE)));
        return school;
    }

    public static void enableView(View myView) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            // get the center for the clipping circle
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

            // get the final radius for the clipping circle
            int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

            // create the animator for this view (the start radius is zero)
            Animator anim =
                    null;
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            myView.setVisibility(View.VISIBLE);
        }
    }

    public static void disableView(final View myView) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

            // get the initial radius for the clipping circle
            int initialRadius = myView.getWidth();

            // create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.GONE);
                }
            });

            // start the animation
            anim.start();
        } else {
            myView.setVisibility(View.GONE);
        }
    }
}
