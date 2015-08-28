package com.futurenav.cuong.futurenav;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futurenav.cuong.futurenav.data.DBContract;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = SearchActivityFragment.class.getSimpleName();

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private RecyclerView mRecyclerView;
    private FavoriteAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final int SEARCH_LOADER = 2;
    private String mSearchText;

    public SearchActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        Toolbar b = (Toolbar) rootView.findViewById(R.id.toolbar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(b);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        SearchView search = (SearchView) rootView.findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mSearchText = query;

                getLoaderManager().restartLoader(SEARCH_LOADER, null, SearchActivityFragment.this);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //search.setSuggestionsAdapter();

                return false;
            }
        });

        TextView emptyView = (TextView)rootView.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.schoollist);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new FavoriteAdapter(emptyView, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        if (mMap == null) {
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
            mMap.setMyLocationEnabled(true);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        if (mSearchText != null)
            getLoaderManager().initLoader(SEARCH_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    private void setUpMap(Cursor data) {

        if (mMap != null & data != null && data.moveToFirst()) {
            do {
                String schoolName = data.getString(data.getColumnIndex(DBContract.SchoolEntry.COLUMN_SCHOOL_NAME));
                LatLng l = new LatLng(Double.valueOf(data.getString(data.getColumnIndex(DBContract.SchoolEntry.COLUMN_LAT)))
                        , Double.valueOf(data.getString(data.getColumnIndex(DBContract.SchoolEntry.COLUMN_LAT))));
                mMap.addMarker(new MarkerOptions().position(l).title(schoolName));
            } while(data.moveToNext());

        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                DBContract.SchoolEntry.buildSchoolUriWithZip(mSearchText),
                null,
                null,
                null,
                DBContract.SchoolEntry.COLUMN_SCHOOL_NAME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

        updateEmptyView();

        setUpMap(data);

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
