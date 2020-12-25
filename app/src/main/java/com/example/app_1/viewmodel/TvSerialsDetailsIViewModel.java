package com.example.app_1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.app_1.repo.TvSerialsDetailsRepo;
import com.example.app_1.responses.TvSerialsDetailsResponse;

public class TvSerialsDetailsIViewModel extends ViewModel {
    private TvSerialsDetailsRepo detailsRepo;

    public TvSerialsDetailsIViewModel() {
        this.detailsRepo = new TvSerialsDetailsRepo();
    }

    public LiveData<TvSerialsDetailsResponse> getTvSerialsDetails(String tvSerialsId) {
        return detailsRepo.getTvSerialsDetails(tvSerialsId);
    }
}
