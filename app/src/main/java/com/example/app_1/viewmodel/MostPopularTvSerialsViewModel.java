package com.example.app_1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.app_1.models.TvSerials;
import com.example.app_1.repo.MostPopularTvSerialsRepo;
import com.example.app_1.responses.TvSerialsResponse;

public class MostPopularTvSerialsViewModel extends ViewModel {
    private MostPopularTvSerialsRepo mostPopularTvSerialsRepo;

    public MostPopularTvSerialsViewModel() {
        mostPopularTvSerialsRepo = new MostPopularTvSerialsRepo();
    }

    public LiveData<TvSerialsResponse> getMostPopularTvSerials(int page) {
        return mostPopularTvSerialsRepo.getMostPopularTvSerials(page);
    }
}
