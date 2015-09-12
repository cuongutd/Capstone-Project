package com.futurenav.cuong.futurenav.service;


import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.futurenav.cuong.futurenav.MyApplication;
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

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this); //getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        //read pref check if this is first time running app then load data into sqlite from code.org
        String initialLoad = sharedPref.getString(Util.INITIAL_LOAD_PREF_KEY, Util.INITIAL_LOAD_NOT_STARTED);

        Log.d(LOG_TAG, "Initial Load Pref: " + initialLoad);

        if (Util.INITIAL_LOAD_NOT_STARTED.equals(initialLoad) ||
                Util.INITIAL_LOAD_FAILED.equals(initialLoad)) {

            //lock it
            Log.d(LOG_TAG, "lock pref: ");
            final SharedPreferences.Editor editor = sharedPref.edit();
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

                    for (School s : schoolDirectory.values()) {

                        ContentValues schoolValue = Util.convertFromSchoolToCV(s);

                        cVVector.add(schoolValue);

                    }

                    if (cVVector.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                        cVVector.toArray(cvArray);
                        getContentResolver().bulkInsert(DBContract.SchoolEntry.CONTENT_URI, cvArray);
                        Log.d(LOG_TAG, "rows inserted: " + cVVector.size());

                    }

                    Log.d(LOG_TAG, "pref done: ");
                    editor.putString(Util.INITIAL_LOAD_PREF_KEY, Util.INITIAL_LOAD_DONE);
                    editor.commit();
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                    Log.d(LOG_TAG, "pref failed: ");
                    editor.putString(Util.INITIAL_LOAD_PREF_KEY, Util.INITIAL_LOAD_FAILED);
                    editor.commit();

                    if (retrofitError.getResponse() != null) {
                        Log.e(LOG_TAG, "Error getting school from code.org: " + retrofitError.getCause().toString());
                    }

                }
            });

        }

        loadFav();

    }


    private void loadFav(){

        Cursor c = getContentResolver().query(DBContract.FavoriteEntry.CONTENT_URI, null, null, null, null);
        Log.d(LOG_TAG, "Fav School count: " + c.getCount());
        ((MyApplication) getApplication()).setSchoolListFromCursor(c);
    }

}
