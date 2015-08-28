package com.futurenav.cuong.futurenav;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futurenav.cuong.futurenav.data.DBContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavoriteActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView mRecyclerView;
    private FavoriteAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final int FAVORITE_LOADER = 1;



    public FavoriteActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favorite, container, false);

        TextView emptyView = (TextView)root.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.favorite_recycleview);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new FavoriteAdapter(emptyView, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return root;



    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        getLoaderManager().initLoader(FAVORITE_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(),
                DBContract.FavoriteEntry.CONTENT_URI,
                null,
                null,
                null,
                DBContract.FavoriteEntry.COLUMN_SCHOOL_NAME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        updateEmptyView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private void updateEmptyView() {
        if ( mAdapter.getItemCount() == 0 ) {
            TextView tv = (TextView) getView().findViewById(R.id.empty_view);
            tv.setText("No data available");
        }
    }
}
