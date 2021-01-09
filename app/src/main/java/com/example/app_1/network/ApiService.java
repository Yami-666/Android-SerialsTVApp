package com.example.app_1.network;

import com.example.app_1.responses.TvSerialsDetailsResponse;
import com.example.app_1.responses.TvSerialsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<TvSerialsResponse> getMostPopularSerials(@Query("page") int page);

    @GET("show-details")
    Call<TvSerialsDetailsResponse> getTvSerialsDetails(@Query("q") String tvSerialsId);

    @GET("search")
    Call<TvSerialsResponse> searchTvSerials(@Query("q") String query, @Query("page") int page);

}
