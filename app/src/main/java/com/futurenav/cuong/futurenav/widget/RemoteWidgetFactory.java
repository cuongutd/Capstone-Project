package com.futurenav.cuong.futurenav.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.futurenav.cuong.futurenav.R;
import com.futurenav.cuong.futurenav.Util;
import com.futurenav.cuong.futurenav.data.DBContract;
import com.futurenav.cuong.futurenav.model.School;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Cuong on 7/30/2015.
 */
public class RemoteWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private int mAppWidgetId;
    private List<School> mWidgetItems = new ArrayList<School>();
    private static final String LOG_TAG = RemoteWidgetFactory.class.getSimpleName();

    public RemoteWidgetFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

        Cursor cursor = mContext.getContentResolver().query(
                DBContract.FavoriteEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        //add query result to list
        if (cursor.moveToFirst()) {
            do {
                School holder = Util.convertFromCursorToSchool(cursor);
                mWidgetItems.add(holder);
            } while (cursor.moveToNext());
            cursor.close();
        }
        cursor.close();
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        // Construct a remote views item based on the app widget item XML file,
        // and set the text based on the position.
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        rv.setTextViewText(R.id.schoolname, mWidgetItems.get(position).getName());
        rv.setTextViewText(R.id.city, mWidgetItems.get(position).getCity() + Util.TEXT_SPLITTER + mWidgetItems.get(position).getState());
        rv.setTextViewText(R.id.description, mWidgetItems.get(position).getDescription());

        Intent fillInIntent = new Intent();
        Bundle extras = new Bundle();
        extras.putParcelable(Util.EXTRA_SCHOOL, mWidgetItems.get(position));
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.schoolname, fillInIntent);
        rv.setOnClickFillInIntent(R.id.city, fillInIntent);
        rv.setOnClickFillInIntent(R.id.description, fillInIntent);
        // Return the remote views object.
        return rv;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


}