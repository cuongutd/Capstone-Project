package com.futurenav.cuong.futurenav;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futurenav.cuong.futurenav.model.School;

/**
 * A placeholder fragment containing a simple view.
 */
public class SchoolDetailActivityFragment extends Fragment {

    School mSchool;


    public SchoolDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_school_detail, container, false);

        Intent intent = getActivity().getIntent();

        mSchool = intent.getParcelableExtra(Util.EXTRA_SCHOOL);

        TextView schoolName = (TextView) root.findViewById(R.id.schoolname);
        schoolName.setText(mSchool.getName());
        TextView website = (TextView) root.findViewById(R.id.website);
        website.setText(mSchool.getWebsite());
        TextView level = (TextView) root.findViewById(R.id.levels);

        if (mSchool.getLevels() != null)
            level.setText(TextUtils.join(Util.TEXT_SPLITTER, mSchool.getLevels()));

        TextView format = (TextView) root.findViewById(R.id.format);
        format.setText(mSchool.getFormat());
        TextView gender = (TextView) root.findViewById(R.id.gender);
        gender.setText(mSchool.getGender());
        TextView language = (TextView) root.findViewById(R.id.language);

        if (mSchool.getLanguages() != null)
            language.setText(TextUtils.join(Util.TEXT_SPLITTER, mSchool.getLanguages()));

        TextView online = (TextView) root.findViewById(R.id.online);
        online.setText(mSchool.getOnline_only());
        TextView nostudent = (TextView) root.findViewById(R.id.nostudent);
        nostudent.setText(mSchool.getNumber_of_students());
        TextView contactnumber = (TextView) root.findViewById(R.id.contactnumber);
        contactnumber.setText(mSchool.getContact_number());
        TextView email = (TextView) root.findViewById(R.id.email);
        email.setText(mSchool.getContact_email());
        TextView address = (TextView) root.findViewById(R.id.address);
        address.setText(Util.formAddress(mSchool.getStreet(), mSchool.getCity(), mSchool.getState(), mSchool.getZip()));

        return root;
    }
}
