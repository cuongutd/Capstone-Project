package com.futurenav.cuong.futurenav;

import android.app.Application;
import android.database.Cursor;

import com.futurenav.cuong.futurenav.model.School;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cuong on 8/28/2015.
 */
public class MyApplication extends Application {

    public Map<String, School> getmFavoriteSchoolList() {
        if (mFavoriteSchoolList == null)
            mFavoriteSchoolList = new HashMap<String, School>();
        return mFavoriteSchoolList;
    }

    public void setmFavoriteSchoolList(Map<String, School> mFavoriteSchoolList) {
        this.mFavoriteSchoolList = mFavoriteSchoolList;
    }

    private Map<String, School> mFavoriteSchoolList;

    public void setSchoolListFromCursor(Cursor c){
        if (c.moveToFirst())
            do{
                School school = Util.convertFromCursorToSchool(c);
                getmFavoriteSchoolList().put(school.getWebsite(), school);

            } while  (c.moveToNext());

    }

}
