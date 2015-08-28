package com.futurenav.cuong.futurenav.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.futurenav.cuong.futurenav.Util;
import com.futurenav.cuong.futurenav.model.CodeOrg;
import com.futurenav.cuong.futurenav.model.School;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Cuong on 8/25/2015.
 */
public class LoadSchoolDataService extends Service {

    private static final String LOG_TAG = LoadSchoolDataService.class.getSimpleName();

    private Map<String, School> mSchoolDirectory;

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate");

        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");

        if (mSchoolDirectory == null)

            new RestClient().getApiService().getJson(new Callback<CodeOrg>() {

                @Override
                public void success(CodeOrg json, Response response) {
                    Log.d(LOG_TAG, response.toString());
                    mSchoolDirectory = new HashMap<String, School>();
                    for (School s : json.getSchools())
                        mSchoolDirectory.put(s.getWebsite(), s);

                    Log.d(LOG_TAG, "school size: " + mSchoolDirectory.values().size());

                }

                @Override
                public void failure(RetrofitError retrofitError) {

                    if (retrofitError.getResponse() != null) {
                        Log.e(LOG_TAG, "Error getting school from code.org: " + retrofitError.getCause().toString());
                    }

                }
            });

        return super.onStartCommand(intent, flags, startId);
    }

    public class MyBinder extends Binder{

        public LoadSchoolDataService getService() {
            Log.d(LOG_TAG, "getService");
            return LoadSchoolDataService.this;
        }
    } ;

    private IBinder mBinder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        //return the service so UI can utilize it
        return mBinder;
    }



    public Map<String, School> searchSchools(String zipCode, String schoolType){
        Log.d(LOG_TAG, "searchSchools");

        //use school website as the key, also to avoid duplicate data
        Map<String, School> result = new HashMap<String, School>();

        if (TextUtils.isEmpty(zipCode))
            return result;

        if (mSchoolDirectory == null)
            return result;


        for (School s : mSchoolDirectory.values())
            if (zipCode.equals(s.getZip()) && (TextUtils.isEmpty(schoolType) || schoolType.equals(s.getMoney_needed())))
                result.put(s.getWebsite(), s);

        return result;
    }


    public School getSchoolByKey(String website){
        return mSchoolDirectory.get(website);
    }


}
