package com.futurenav.cuong.futurenav.service;


import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.futurenav.cuong.futurenav.Util;
import com.futurenav.cuong.futurenav.data.DBContract;
import com.futurenav.cuong.futurenav.model.CodeOrg;
import com.futurenav.cuong.futurenav.model.School;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Cuong on 8/26/2015.
 */
public class LoadSchoolIntentService extends IntentService {

    private static final String LOG_TAG = LoadSchoolIntentService.class.getSimpleName();

    private SharedPreferences sharedPref;

    public static final String APP_PREF = "futureNavPrefs";

    public LoadSchoolIntentService() {
        super("LoadSchoolIntentService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public LoadSchoolIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sharedPref = getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        //read pref check if this is first time running app then load data into sqlite from code.org
        String initialLoad = sharedPref.getString(Util.INITIAL_LOAD_PREF_KEY, Util.INITIAL_LOAD_NOT_STARTED);

        Log.d(LOG_TAG, "Initial Load Pref: " + initialLoad);

        if (Util.INITIAL_LOAD_NOT_STARTED.equals(initialLoad)) {

            //lock it
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Util.INITIAL_LOAD_PREF_KEY, Util.INITIAL_LOAD_INPROGRESS);
            editor.commit();

            new RestClient().getApiService().getJson(new Callback<CodeOrg>() {

                @Override
                public void success(CodeOrg json, Response response) {

                    Log.d(LOG_TAG, response.toString());
                    Log.d(LOG_TAG, "school size: " + json.getSchools().size());

                    //remove duplicate schools with same website
                    Map<String, School> schoolDirectory = new HashMap<String, School>();
                    for (School s : json.getSchools())
                        if (!(TextUtils.isEmpty(s.getName()) || TextUtils.isEmpty(s.getWebsite())))
                            schoolDirectory.put(s.getWebsite(), s);


                    Vector<ContentValues> cVVector = new Vector<ContentValues>(schoolDirectory.size());

                    int i = 0;
                    for (School s : schoolDirectory.values()) {

                        ContentValues schoolValue = new ContentValues();

                        schoolValue.put(DBContract.SchoolEntry.COLUMN_SCHOOL_NAME, s.getName());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_WEBSITE, s.getWebsite());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_LEVEL, TextUtils.join(Util.TEXT_SPLITTER, s.getLevels())); //list of string to string comma seperated
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_FORMAT, s.getFormat());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_FORMAT_DESC, s.getFormat_description());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_GENDER, s.getGender());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_DESC, s.getDescription());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_LANGUAGE, TextUtils.join(Util.TEXT_SPLITTER, s.getLanguages()));
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_SCHOOL_TYPE, s.getMoney_needed());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_ONLINE_ONLY, s.getOnline_only());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_NO_STUDENT, s.getNumber_of_students());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_CONTACT_NAME, s.getContact_name());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_CONTACT_NUMBER, s.getContact_number());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_CONTACT_EMAIL, s.getContact_email());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_LAT, s.getLatitude());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_LONGITUDE, s.getLongitude());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_STREET, s.getStreet());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_CITY, s.getCity());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_STATE, s.getState());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_ZIP, s.getZip());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_PUBLISHED, s.getPublished());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_UPDATED_AT, s.getUpdated_at());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_COUNTRY, s.getCountry());
                        schoolValue.put(DBContract.SchoolEntry.COLUMN_SOURCE, s.getSource());

                        //test
                        if (i < 2)
                            getContentResolver().insert(DBContract.FavoriteEntry.CONTENT_URI, schoolValue);
                        i++;
                        cVVector.add(schoolValue);

                    }

                    if (cVVector.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                        cVVector.toArray(cvArray);
                        getContentResolver().bulkInsert(DBContract.SchoolEntry.CONTENT_URI, cvArray);
                        Log.d(LOG_TAG, "rows inserted: " + cVVector.size());

                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                    if (retrofitError.getResponse() != null) {
                        Log.e(LOG_TAG, "Error getting school from code.org: " + retrofitError.getCause().toString());
                    }

                }
            });
            editor.putString(Util.INITIAL_LOAD_PREF_KEY, Util.INITIAL_LOAD_DONE);
            editor.commit();
        }
    }

}
