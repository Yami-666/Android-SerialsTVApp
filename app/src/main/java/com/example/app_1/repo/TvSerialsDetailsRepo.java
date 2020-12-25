package com.example.app_1.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_1.models.TvSerialsDetails;
import com.example.app_1.network.ApiClient;
import com.example.app_1.network.ApiService;
import com.example.app_1.responses.TvSerialsDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TvSerialsDetailsRepo {
    private ApiService apiService;

    public TvSerialsDetailsRepo() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvSerialsDetailsResponse> getTvSerialsDetails(String tvSerialId) {
        MutableLiveData<TvSerialsDetailsResponse> data = new MutableLiveData<>();
        apiService.getTvSerialsDetails(tvSerialId).enqueue(new Callback<TvSerialsDetailsResponse>() {
            @Override
            public void onResponse(Call<TvSerialsDetailsResponse> call, Response<TvSerialsDetailsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TvSerialsDetailsResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}
