package com.futurenav.cuong.futurenav;

import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.futurenav.cuong.futurenav.data.DBContract;
import com.futurenav.cuong.futurenav.model.School;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A placeholder fragment containing a simple view.
 */
public class SchoolDetailActivityFragment extends MyFragment {

    private static final String LOG_TAG = SchoolDetailActivityFragment.class.getSimpleName();
    private School mSchool;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ShareActionProvider mShareActionProvider;

    public SchoolDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_school_detail, container, false);

        Toolbar b = (Toolbar) root.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(b);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) root.findViewById(R.id.collapsing_toolbar);
        if (collapsingToolbar != null)
            collapsingToolbar.setTitle(getString(R.string.title_activity_school_detail));

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getActivity().getIntent();
        mSchool = intent.getParcelableExtra(Util.EXTRA_SCHOOL);

        if (savedInstanceState!= null &&
                savedInstanceState.containsKey(Util.SAVE_INSTANT_SCHOOL) &&
                savedInstanceState.getParcelable(Util.SAVE_INSTANT_SCHOOL) != null)
            mSchool = savedInstanceState.getParcelable(Util.SAVE_INSTANT_SCHOOL);


        TextView schoolName = (TextView) root.findViewById(R.id.schoolname);
        schoolName.setText(mSchool.getName());

        TextView website = (TextView) root.findViewById(R.id.website);
        website.setText(mSchool.getWebsite());

        TextView level = (TextView) root.findViewById(R.id.levels);
        level.setText(mSchool.getsLevels());

        TextView format = (TextView) root.findViewById(R.id.format);
        format.setText(mSchool.getFormat());

        TextView gender = (TextView) root.findViewById(R.id.gender);
        gender.setText(mSchool.getGender());

        TextView language = (TextView) root.findViewById(R.id.language);
        language.setText(mSchool.getsLanguages());

        TextView online = (TextView) root.findViewById(R.id.online);
        if ("true".equalsIgnoreCase(mSchool.getOnline_only()))
            online.setText("Yes");
        else
            online.setText("No");
        TextView nostudent = (TextView) root.findViewById(R.id.nostudent);
        nostudent.setText(mSchool.getNumber_of_students());
        TextView contactnumber = (TextView) root.findViewById(R.id.contactnumber);
        contactnumber.setText(mSchool.getContact_number());
        TextView email = (TextView) root.findViewById(R.id.email);
        //email.setText(Html.fromHtml("<a href=\"mailto:" + mSchool.getContact_email()+"\">"+mSchool.getContact_email()+"</a>"));
        email.setText(mSchool.getContact_email());
        TextView address = (TextView) root.findViewById(R.id.address);
        address.setText(Util.formAddress(mSchool.getStreet(), mSchool.getCity(), mSchool.getState(), mSchool.getZip()));
        TextView desc = (TextView) root.findViewById(R.id.description);
        desc.setText(mSchool.getDescription());

        setupFAB(root);
        setupMap();

        return root;
    }

    private boolean isSchoolSaved() {
        return ((MyApplication) getActivity().getApplication()).getmFavoriteSchoolList()
                .containsKey(mSchool.getWebsite());
    }

    private void setupFAB(View root) {

        final ImageView addToFav = (ImageView) root.findViewById(R.id.addtofav);

        if (isSchoolSaved())
            addToFav.setImageResource(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
        else
            addToFav.setImageResource(R.drawable.bookmark);

        addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSchoolSaved()) {
                    //add to the saved list and change button to white
                    ContentValues cv = Util.convertFromSchoolToCV(mSchool);

                    getActivity().getContentResolver().insert(DBContract.FavoriteEntry.CONTENT_URI, cv);
                    ((MyApplication) getActivity().getApplication()).getmFavoriteSchoolList().put(mSchool.getWebsite(), mSchool);
                    Toast toast = Toast.makeText(getActivity(), R.string.school_msg_add, Toast.LENGTH_SHORT);
                    toast.show();

                    addToFav.setImageResource(R.drawable.abc_btn_rating_star_on_mtrl_alpha);

                } else {
                    String[] args = {mSchool.getWebsite()};
                    //delete from table
                    getActivity().getContentResolver().delete(DBContract.FavoriteEntry.CONTENT_URI, DBContract.FavoriteEntry.COLUMN_WEBSITE + " = ? ", args);
                    //remove from list
                    ((MyApplication) getActivity().getApplication()).getmFavoriteSchoolList().remove(mSchool.getWebsite());

                    Toast.makeText(getActivity(),
                            R.string.school_msg_remove,
                            Toast.LENGTH_SHORT).show();

                    addToFav.setImageResource(R.drawable.bookmark);
                }
            }
        });

    }


    private void setupMap() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
            mMap.setMyLocationEnabled(true);
        }
        if (mMap != null) {

            String schoolName = mSchool.getName();
            String sLat = mSchool.getLatitude();
            Log.d(LOG_TAG, "lat: " + sLat);
            String sLong = mSchool.getLongitude();
            Log.d(LOG_TAG, "long: " + sLong);

            if (!TextUtils.isEmpty(sLat) && !TextUtils.isEmpty(sLong)) {
                Double lat = Double.valueOf(sLat);
                Double lon = Double.valueOf(sLong);
                LatLng l = new LatLng(lat, lon);
                mMap.addMarker(new MarkerOptions().position(l).title(schoolName));
                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(l, 12F);
                mMap.animateCamera(cu);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_school_fragment_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mSchool.getName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<a href=\"" + mSchool.getWebsite() + "\">" + mSchool.getWebsite() + "</a>") + "\n" + mSchool.getDescription());
        mShareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(Util.SAVE_INSTANT_SCHOOL, mSchool);

        super.onSaveInstanceState(outState);
    }
}
