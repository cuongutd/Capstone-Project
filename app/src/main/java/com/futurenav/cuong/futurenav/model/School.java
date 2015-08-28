package com.futurenav.cuong.futurenav.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.futurenav.cuong.futurenav.Util;
import com.futurenav.cuong.futurenav.data.DBContract;
import com.futurenav.cuong.futurenav.service.LoadSchoolIntentService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Cuong on 8/25/2015.
 */

public class School implements Parcelable {

    private String name;
    private String website;
    private List<String> levels;
    private String format;//": "In School",
    private String format_description;//": "Daily programming course",
    private String gender;//": "Both",
    private String description;//": "Introduction to Java Programming",
    private List<String> languages;//
    private String money_needed;//": true,
    private String online_only;//": false,
    private String number_of_students;//": null,
    private String contact_name;//": "Mullen High School",
    private String contact_number;//": "3037611764",
    private String contact_email;//": "mcguire@mullenhigh.com",
    private String latitude;//": 39.6515,
    private String longitude;//": -105.035,
    private String street;//": "3602 S. Lowell Blvd",
    private String city;//": "Denver",
    private String state;//": "CO",
    private String zip;//": "50236",
    private String published;//": 1,
    private String updated_at;//": "2013-02-26T13:31:23Z",
    private String country;//": "United States",
    private String source;//": "user"

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<String> getLevels() {
        return levels;
    }

    public void setLevels(List<String> levels) {
        this.levels = levels;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat_description() {
        return format_description;
    }

    public void setFormat_description(String format_description) {
        this.format_description = format_description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getMoney_needed() {
        return money_needed;
    }

    public void setMoney_needed(String money_needed) {
        this.money_needed = money_needed;
    }

    public String getOnline_only() {
        return online_only;
    }

    public void setOnline_only(String online_only) {
        this.online_only = online_only;
    }

    public String getNumber_of_students() {
        return number_of_students;
    }

    public void setNumber_of_students(String number_of_students) {
        this.number_of_students = number_of_students;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    protected School(Parcel in) {
        name = in.readString();
        website = in.readString();
        if (in.readByte() == 0x01) {
            levels = new ArrayList<String>();
            in.readList(levels, String.class.getClassLoader());
        } else {
            levels = null;
        }
        format = in.readString();
        format_description = in.readString();
        gender = in.readString();
        description = in.readString();
        if (in.readByte() == 0x01) {
            languages = new ArrayList<String>();
            in.readList(languages, String.class.getClassLoader());
        } else {
            languages = null;
        }
        money_needed = in.readString();
        online_only = in.readString();
        number_of_students = in.readString();
        contact_name = in.readString();
        contact_number = in.readString();
        contact_email = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        street = in.readString();
        city = in.readString();
        state = in.readString();
        zip = in.readString();
        published = in.readString();
        updated_at = in.readString();
        country = in.readString();
        source = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(website);
        if (levels == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(levels);
        }
        dest.writeString(format);
        dest.writeString(format_description);
        dest.writeString(gender);
        dest.writeString(description);
        if (languages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(languages);
        }
        dest.writeString(money_needed);
        dest.writeString(online_only);
        dest.writeString(number_of_students);
        dest.writeString(contact_name);
        dest.writeString(contact_number);
        dest.writeString(contact_email);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zip);
        dest.writeString(published);
        dest.writeString(updated_at);
        dest.writeString(country);
        dest.writeString(source);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<School> CREATOR = new Parcelable.Creator<School>() {
        @Override
        public School createFromParcel(Parcel in) {
            return new School(in);
        }

        @Override
        public School[] newArray(int size) {
            return new School[size];
        }
    };

    public School(Cursor mCursor){
        name = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_SCHOOL_NAME));
        website = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_WEBSITE));

        String levelText = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_LEVEL));
        if (TextUtils.isEmpty(levelText))
            levels = Arrays.asList(TextUtils.split(levelText, Util.TEXT_SPLITTER));

        format = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_FORMAT));
        format_description = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_FORMAT_DESC));
        gender = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_GENDER));
        description = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_DESC));

        String langs = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_LANGUAGE));
        if (TextUtils.isEmpty(langs))
            languages = Arrays.asList(TextUtils.split(langs, Util.TEXT_SPLITTER));

        money_needed = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_SCHOOL_TYPE));
        online_only = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_ONLINE_ONLY));
        number_of_students = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_NO_STUDENT));
        contact_name = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_CONTACT_NAME));
        contact_number = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_CONTACT_NUMBER));
        contact_email = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_CONTACT_EMAIL));
        latitude = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_LAT));
        longitude = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_LONGITUDE));
        street = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_STREET));
        city = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_CITY));
        state = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_STATE));
        zip = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_ZIP));
        published = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_PUBLISHED));
        updated_at = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_UPDATED_AT));
        country = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_COUNTRY));
        source = mCursor.getString(mCursor.getColumnIndex(DBContract.SchoolEntry.COLUMN_SOURCE));

    }

}
