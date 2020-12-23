package com.example.app_1.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_1.models.TvSerials;
import com.example.app_1.network.ApiClient;
import com.example.app_1.network.ApiService;
import com.example.app_1.responses.TvSerialsResponse;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTvSerialsRepo {
    private ApiService apiService;

    public MostPopularTvSerialsRepo() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvSerialsResponse> getMostPopularTvSerials(int page) {

        MutableLiveData<TvSerialsResponse> data = new MutableLiveData<>();
        apiService.getMostPopularSerials(page).enqueue(new Callback<TvSerialsResponse>() {
            @Override
            public void onResponse(@NotNull Call<TvSerialsResponse> call,
                                   @NotNull Response<TvSerialsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<TvSerialsResponse> call, @NotNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
