package com.futurenav.cuong.futurenav.service;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.futurenav.cuong.futurenav.MyAppCompatActivity;
import com.futurenav.cuong.futurenav.MyApplication;
import com.futurenav.cuong.futurenav.R;
import com.futurenav.cuong.futurenav.SchoolDetailActivity;
import com.futurenav.cuong.futurenav.Util;
import com.futurenav.cuong.futurenav.data.DBContract;
import com.futurenav.cuong.futurenav.model.School;

/**
 * Created by Cuong on 8/26/2015.
 */

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.FavoriteViewHolder> {

    private static final String LOG_TAG = SchoolAdapter.class.getSimpleName();

    private Cursor mCursor;
    private TextView mEmptyView;
    private Context mContext;


    public SchoolAdapter(TextView emptyView, Context context) {
        mEmptyView = emptyView;
        mContext = context;
    }


    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mSchoolName;
        public TextView mAddress;
        public TextView mType;
        //public TextView mLevel;
        public TextView mLanguage;
        //public TextView mGender;
        public ImageView mAddToFav;

        public FavoriteViewHolder(View view) {
            super(view);
            mSchoolName = (TextView) view.findViewById(R.id.schoolname);
            mAddress = (TextView) view.findViewById(R.id.address);
            mType = (TextView) view.findViewById(R.id.type);
            //mLevel = (TextView) view.findViewById(R.id.level);
            mLanguage = (TextView) view.findViewById(R.id.language);
            //mGender = (TextView) view.findViewById(R.id.gender);
            mAddToFav = (ImageView) view.findViewById(R.id.addtofav);

        }

        public void print(){
            Log.d("mSchoolName: ", mSchoolName.getText().toString());
            Log.d("mAddress: ", mAddress.getText().toString());
            Log.d("mType: ", mType.getText().toString());
            //Log.d("mLevel: ", mLevel.getText().toString());
            Log.d("mLanguage: ", mLanguage.getText().toString());
            //Log.d("mGender: ", mGender.getText().toString());
        }
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewGroup instanceof RecyclerView) {

            int layoutId = R.layout.schoolcard;
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);

            final FavoriteViewHolder holder = new FavoriteViewHolder(view);

            view.setFocusable(true);

            view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            int position = holder.getAdapterPosition();
                                            mCursor.moveToPosition(position);
                                            Log.d(LOG_TAG, "position: " + position);
                                            School school = Util.convertFromCursorToSchool(mCursor);
                                            Intent intent = new Intent(mContext, SchoolDetailActivity.class);
                                            intent.putExtra(Util.EXTRA_SCHOOL, school);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                ActivityOptionsCompat options = ActivityOptionsCompat.
                                                        makeSceneTransitionAnimation((MyAppCompatActivity)mContext, view, mContext.getString(R.string.activity_trans));
                                                mContext.startActivity(intent, options.toBundle());
                                            }
                                            else {
                                                mContext.startActivity(intent);
                                            }


                                        }
                                    }
            );

            holder.mAddToFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = holder.getAdapterPosition();
                    mCursor.moveToPosition(position);
                    Log.d(LOG_TAG, "position: " + position);
                    School school = Util.convertFromCursorToSchool(mCursor);

                    ContentValues cv = Util.convertFromSchoolToCV(school);

                    mContext.getContentResolver().insert(DBContract.FavoriteEntry.CONTENT_URI, cv);

                    v.setVisibility(View.GONE);

                    ((MyApplication)((Activity) mContext).getApplication()).getmFavoriteSchoolList().put(school.getWebsite(), school);

                }
            });

            return holder;
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder viewHolder, int i) {
        mCursor.moveToPosition(i);

        viewHolder.mSchoolName.setText(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_SCHOOL_NAME)));
        viewHolder.mAddress.setText(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_CITY)) + ", " +
                mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_STATE)));

        StringBuffer type = new StringBuffer("School Type: ");

        String moneyNeeded = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_SCHOOL_TYPE));
        if ("true".equalsIgnoreCase(moneyNeeded))
            type.append("Private");
        else
            type.append("Public");
        type.append(". Gender: ");
        type.append(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_GENDER)));

        viewHolder.mType.setText(type.toString());

        viewHolder.mLanguage.setText("Languages: " + mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_LANGUAGE)));

        if (((MyApplication) (((Activity) mContext).getApplication())).getmFavoriteSchoolList()
                .containsKey(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_WEBSITE))))
            viewHolder.mAddToFav.setVisibility(View.GONE);
        else
            viewHolder.mAddToFav.setVisibility(View.VISIBLE);

    }

    public void swapCursor(Cursor c) {
        mCursor = c;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getLong(mCursor.getColumnIndex(DBContract.SchoolEntry._ID));
    }

    public String getWebSite(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getString(mCursor.getColumnIndex(DBContract.FavoriteEntry.COLUMN_WEBSITE));
    }



}
