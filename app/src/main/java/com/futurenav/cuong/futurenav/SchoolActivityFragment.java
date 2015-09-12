package com.futurenav.cuong.futurenav;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.futurenav.cuong.futurenav.data.DBContract;
import com.futurenav.cuong.futurenav.service.PlaceAutocompleteAdapter;
import com.futurenav.cuong.futurenav.service.SchoolAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class SchoolActivityFragment extends MyFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener
        , SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String LOG_TAG = SchoolActivityFragment.class.getSimpleName();

    private TextView mEmptyView;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private RecyclerView mRecyclerView;
    private SchoolAdapter mAdapter;
    private android.support.v7.widget.LinearLayoutManager mLayoutManager;
    private FloatingActionButton mFAB;
    private static final int SEARCH_LOADER = 2;

    private Double mSearchLat;
    private Double mSearchLong;

    private PlaceAutocompleteAdapter mPlaceAdapter;
    protected GoogleApiClient mGoogleApiClient;
    private AutoCompleteTextView mAutocompleteView;
    private ImageView mGoogleLogo;

    private boolean mSearchMode = true;

    private Map<Integer, Marker> mMapMarkers = new HashMap<Integer, Marker>();


    private static final LatLngBounds BOUNDS_US = new LatLngBounds(
            new LatLng(28.410307, -123.922802), new LatLng(49.454750, -66.900840));


    public SchoolActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        if (savedInstanceState != null){
            Log.d(LOG_TAG, "saved instant");
            mSearchMode = savedInstanceState.getBoolean(Util.SAVE_INSTANT_SEARCH_MODE, false);
            mSearchLat = savedInstanceState.getDouble(Util.SAVE_INSTANT_LAT);
            mSearchLong = savedInstanceState.getDouble(Util.SAVE_INSTANT_LONG);
            if (!mSearchMode || (mSearchMode && mSearchLat != null && mSearchLong != null))
                getLoaderManager().restartLoader(SEARCH_LOADER, null, SchoolActivityFragment.this);
        }

        View rootView = inflater.inflate(R.layout.fragment_school, container, false);

        initToolBar(rootView);

        initSearchTextview(rootView);

        initRecyclerview(rootView);

        initMap();

        initFAB(rootView);

        return rootView;
    }

    private void initToolBar(View root) {

        Toolbar b = (Toolbar) root.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(b);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);

    }

    @Override
    public void onResume() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    private void initSearchTextview(View root) {

        mGoogleLogo = (ImageView) root.findViewById(R.id.powerbygoogle);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                //.enableAutoManage(getActivity(), 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();


        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView) root.findViewById(R.id.autocomplete_places);
        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

//        mAutocompleteView.setOnTouchListener(new RightDrawableOnTouchListener(mAutocompleteView) {
//            @Override
//            public boolean onDrawableTouch(final MotionEvent event) {
//                mAutocompleteView.setText("");
//                return true;
//            }
//        });



        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mPlaceAdapter = new PlaceAutocompleteAdapter(getActivity(), android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_US, null);

        mAutocompleteView.setAdapter(mPlaceAdapter);


    }

    private void initRecyclerview(View root) {
        mEmptyView = (TextView) root.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.schoollist);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new SchoolAdapter(mEmptyView, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        //on favorite list, swiping right item will delete school off the list
        ItemTouchHelper mIth = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return true;// true if moved, false otherwise
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        Log.d(LOG_TAG, "direction: " + direction);
                        Log.d(LOG_TAG, "ItemTouchHelper.RIGHT: " + ItemTouchHelper.RIGHT);
                        if (direction == ItemTouchHelper.RIGHT && !mSearchMode) {
                            // remove from adapter
                            int pos = viewHolder.getAdapterPosition();
                            long id = mAdapter.getItemId(pos);
                            String[] args = {String.valueOf(id)};
                            getActivity().getContentResolver().delete(DBContract.FavoriteEntry.CONTENT_URI, DBContract.FavoriteEntry._ID + " = ? ", args);
                            SchoolActivityFragment.this.getLoaderManager().restartLoader(SEARCH_LOADER, null, SchoolActivityFragment.this);
                            String website = mAdapter.getWebSite(pos);
                            ((MyApplication) getActivity().getApplication()).getmFavoriteSchoolList().remove(website);
                            mMapMarkers.get(Integer.valueOf(pos)).remove();
                            Toast.makeText(getActivity(),
                                    R.string.school_msg_remove,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        if (mSearchMode)
                            return 0;
                        else
                            return super.getSwipeDirs(recyclerView, viewHolder);
                    }

                });
        mIth.attachToRecyclerView(mRecyclerView);
    }


    private void initMap() {
        if (mMap == null) {

            SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;

                    mMap.setMyLocationEnabled(true);
                    mMap.setOnMarkerClickListener(SchoolActivityFragment.this);
                    if (mSearchMode && mSearchLat == null)
                        //default search around user location
                        searchMyLocation();
                }
            });
        }

    }

    private void initFAB(View root) {

        mFAB = (FloatingActionButton) root.findViewById(R.id.fav_fab);

        if (!mSearchMode) {
            mFAB.setImageResource(android.R.drawable.ic_menu_search);
            mAutocompleteView.setVisibility(View.GONE);
            mGoogleLogo.setVisibility(View.GONE);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name) + " - Favorites");
        } else {
            mFAB.setImageResource(R.drawable.bookmark);
            mAutocompleteView.setVisibility(View.VISIBLE);
            mGoogleLogo.setVisibility(View.VISIBLE);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name) + " - Search for School");
        }


        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "mSearchMode: " + mSearchMode, Toast.LENGTH_SHORT).show();
                if (mSearchMode) {
                    mSearchMode = false;//favorite mode
                    mFAB.setImageResource(android.R.drawable.ic_menu_search);
                    //mAutocompleteView.setVisibility(View.GONE);
                    disableSearchText(mAutocompleteView);
                    mGoogleLogo.setVisibility(View.GONE);
                    //requery favorite list, which will remark the map
                    getLoaderManager().restartLoader(SEARCH_LOADER, null, SchoolActivityFragment.this);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name) + " - Favorites");
                } else {
                    mSearchMode = true;
                    mFAB.setImageResource(R.drawable.bookmark);
                    //mAutocompleteView.setVisibility(View.VISIBLE);
                    enableSearchText(mAutocompleteView);
                    mGoogleLogo.setVisibility(View.VISIBLE);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name) + " - Search for School");
                    if (mSearchLat != null)
                        getLoaderManager().restartLoader(SEARCH_LOADER, null, SchoolActivityFragment.this);
                    else {
                        mAdapter.swapCursor(null);
                        mMap.clear();
                    }
                }
            }
        });
    }

    private void enableSearchText(View myView) {
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

    private void disableSearchText(final View myView) {
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


    private void searchMyLocation() {
        LocationManager locMan = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria crit = new Criteria();
        Location loc = locMan.getLastKnownLocation(locMan.getBestProvider(crit, false));

        //search will be kicked off when resume
        if (loc != null){
            mSearchLat = loc.getLatitude();
            mSearchLong = loc.getLongitude();
            getLoaderManager().restartLoader(SEARCH_LOADER, null, SchoolActivityFragment.this);
        }else{
            Log.d(LOG_TAG, "Could not locate phone last location");
        }
//
//        CameraPosition camPos = new CameraPosition.Builder()
//                .target(new LatLng(loc.getLatitude(), loc.getLongitude()))
//                .zoom(12.8f)
//                .build();
//
//        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);
//
//        mMap.moveCamera(camUpdate);

    }

    private void setUpMap(Cursor data) {

        mMapMarkers = new HashMap<Integer, Marker>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (mMap != null){
            mMap.clear();
            if (data != null && data.moveToFirst()) {
                do {
                    String schoolName = data.getString(data.getColumnIndex(DBContract.SchoolEntry.COLUMN_SCHOOL_NAME));
                    String sLat = data.getString(data.getColumnIndex(DBContract.SchoolEntry.COLUMN_LAT));
                    String sLong = data.getString(data.getColumnIndex(DBContract.SchoolEntry.COLUMN_LONGITUDE));

                    if (!TextUtils.isEmpty(sLat) && !TextUtils.isEmpty(sLong)) {
                        Double lat = Double.valueOf(sLat);
                        Double lon = Double.valueOf(sLong);
                        LatLng l = new LatLng(lat, lon);

                        Marker marker = mMap.addMarker(new MarkerOptions().position(l).title(schoolName));

                        mMapMarkers.put(data.getPosition(), marker);

                        builder.include(l);
                    }

                } while (data.moveToNext());

                //re focus map
                LatLngBounds bounds = builder.build();
                int padding = 100; // offset from edges of the map in pixels
                CameraUpdate cu;
                if (mMapMarkers.size() == 1)
                    cu = CameraUpdateFactory.newLatLngZoom(mMapMarkers.values().iterator().next().getPosition(), 12F);
                else
                    cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                mMap.animateCamera(cu);
            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        if (mSearchMode){
            return new CursorLoader(getActivity(),
                    DBContract.SchoolEntry.buildSchoolUriWithLatLong(mSearchLat, mSearchLong),
                    null,
                    null,
                    null,
                    DBContract.SchoolEntry.COLUMN_SCHOOL_NAME + " ASC");
        }
        else {
            return new CursorLoader(getActivity(),
                    DBContract.FavoriteEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    DBContract.FavoriteEntry.COLUMN_SCHOOL_NAME + " ASC");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");

        mAdapter.swapCursor(data);

        updateEmptyView();

        setUpMap(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
        mAdapter.swapCursor(null);
    }

    private void updateEmptyView() {
        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            if (mSearchMode)
                mEmptyView.setText(getString(R.string.empty_data_msg));
            else
                mEmptyView.setText(getString(R.string.empty_saved_school));
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mPlaceAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Log.i(LOG_TAG, "Called getPlaceById to get Place details for " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            mSearchLat = place.getLatLng().latitude;
            mSearchLong = place.getLatLng().longitude;

            getLoaderManager().restartLoader(SEARCH_LOADER, null, SchoolActivityFragment.this);

            Log.i(LOG_TAG, "Place details received: " + place.getName());

            places.release();
        }
    };

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        Toast.makeText(getActivity(),
                getActivity().getString(R.string.googleapi_msg_error) + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        int pos = -1;
        for (Map.Entry<Integer, Marker> entry : mMapMarkers.entrySet())
            //assuming title is unique within a list
            if (marker.getTitle().equals(entry.getValue().getTitle())) {
                pos = entry.getKey().intValue();
                break;
            }
        if (pos >= 0) {
            Log.d(LOG_TAG, "pos: " + pos);
            mLayoutManager.smoothScrollToPosition(mRecyclerView, null, pos);
        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //A better way would be intentservice sending local broadcast message
        Log.d(LOG_TAG, "onSharedPreferenceChanged: " + sharedPreferences.getString(Util.INITIAL_LOAD_PREF_KEY, "NA"));
        if (Util.INITIAL_LOAD_PREF_KEY.equals(key)){

            String loadStatus = sharedPreferences.getString(Util.INITIAL_LOAD_PREF_KEY, Util.INITIAL_LOAD_NOT_STARTED);
            switch (loadStatus) {
                case Util.INITIAL_LOAD_FAILED: {
                    Toast.makeText(getActivity(), R.string.loaddata_msg_failed, Toast.LENGTH_LONG).show();
                    break;
                }
                case Util.INITIAL_LOAD_DONE: {
                    Toast.makeText(getActivity(), R.string.loaddata_msg_done, Toast.LENGTH_SHORT).show();
                    getLoaderManager().restartLoader(SEARCH_LOADER, null, this);
                    break;
                }
                case Util.INITIAL_LOAD_INPROGRESS: {
                    Toast.makeText(getActivity(), "Initial loading data from code.org.", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(Util.SAVE_INSTANT_SEARCH_MODE, mSearchMode);
        if (mSearchLat != null && mSearchLong != null){
            outState.putDouble(Util.SAVE_INSTANT_LAT, mSearchLat);
            outState.putDouble(Util.SAVE_INSTANT_LONG, mSearchLong);
        }
        super.onSaveInstanceState(outState);

    }
}
