package com.maxprof90.redditarticles.api;

import com.maxprof90.redditarticles.models.RootReddit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top.json?limit=4")
    Call<RootReddit> getData();
    @GET("top.json")
    Call<RootReddit> getDataBefore(@Query("before") String before, @Query("limit") int limit);
    @GET("top.json")
    Call<RootReddit> getDataAfter(@Query("after") String before, @Query("limit") int limit);


}

