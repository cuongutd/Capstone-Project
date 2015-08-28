package com.futurenav.cuong.futurenav;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futurenav.cuong.futurenav.data.DBContract;
import com.futurenav.cuong.futurenav.model.School;

/**
 * Created by Cuong on 8/26/2015.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private static final String LOG_TAG = FavoriteAdapter.class.getSimpleName();

    private Cursor mCursor;
    private TextView mEmptyView;
    private Context mContext;


    public FavoriteAdapter(TextView emptyView, Context contect){
        mEmptyView = emptyView;
        mContext = contect;
    }



    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mSchoolName;
        public TextView mAddress;
        public TextView mType;
        public TextView mLevel;
        public TextView mLanguage;
        public TextView mGender;

        public FavoriteViewHolder(View view) {
            super(view);
            mSchoolName = (TextView) view.findViewById(R.id.schoolname);
            mAddress = (TextView) view.findViewById(R.id.address);
            mType = (TextView) view.findViewById(R.id.type);
            mLevel = (TextView) view.findViewById(R.id.level);
            mLanguage = (TextView) view.findViewById(R.id.language);
            mGender = (TextView) view.findViewById(R.id.gender);
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
                                            School school = new School(mCursor);
                                            Intent intent = new Intent(mContext, SchoolDetailActivity.class);
                                            intent.putExtra(Util.EXTRA_SCHOOL, school);
                                            mContext.startActivity(intent);
                                        }
                                    }
            );

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
        String moneyNeeded = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_SCHOOL_TYPE));
        if ("true".equalsIgnoreCase(moneyNeeded))
            viewHolder.mType.setText("Private");
        else
            viewHolder.mType.setText("Public");
        viewHolder.mLevel.setText(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_LEVEL)));
        viewHolder.mLanguage.setText(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_LANGUAGE)));
        viewHolder.mGender.setText(mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_GENDER)));

    }

    public void swapCursor(Cursor c){
        mCursor = c;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        //mCursor.moveToPosition(position);
        return super.getItemId(position);
    }
}
