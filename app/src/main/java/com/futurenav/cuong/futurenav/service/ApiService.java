package com.futurenav.cuong.futurenav.service;

import com.futurenav.cuong.futurenav.model.CodeOrg;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Cuong on 8/25/2015.
 */
public interface ApiService {
    @GET("/schools.json")
    public void getJson(Callback<CodeOrg> callback);

}
