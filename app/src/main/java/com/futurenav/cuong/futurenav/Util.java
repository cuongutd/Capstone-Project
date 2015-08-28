package com.futurenav.cuong.futurenav;

/**
 * Created by Cuong on 8/25/2015.
 */
public class Util {

    public static final String EXTRA_SCHOOL = "SCHOOL";
    public static final String TEXT_SPLITTER = ",";
    public static final String INITIAL_LOAD_PREF_KEY = "INITIAL_LOAD";
    public static final String INITIAL_LOAD_NOT_STARTED = "N";
    public static final String INITIAL_LOAD_INPROGRESS = "I";
    public static final String INITIAL_LOAD_DONE = "D";
    public static String formAddress(String street, String city, String state, String zip){
        return street + ", " + city  + ", " + state + " " + zip;
    }


}
