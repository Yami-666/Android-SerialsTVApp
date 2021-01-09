package com.example.app_1.repo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_1.models.TvSerialsDetails;
import com.example.app_1.network.ApiClient;
import com.example.app_1.network.ApiService;
import com.example.app_1.responses.TvSerialsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTvSerialsRepo {
    private ApiService apiService;

    public SearchTvSerialsRepo() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvSerialsResponse> searchTvSerials(String query, int page) {
        MutableLiveData<TvSerialsResponse> data = new MutableLiveData<>();
        apiService.searchTvSerials(query, page).enqueue(new Callback<TvSerialsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvSerialsResponse> call,
                                   @NonNull Response<TvSerialsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TvSerialsResponse> call,@NonNull Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}
