package com.example.app_1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.app_1.repo.SearchTvSerialsRepo;
import com.example.app_1.responses.TvSerialsResponse;

public class SearchViewModel extends ViewModel {
    private SearchTvSerialsRepo searchTvSerialsRepo;

    public SearchViewModel() {
        searchTvSerialsRepo = new SearchTvSerialsRepo();
    }

    public LiveData<TvSerialsResponse> searchTvSerials(String query, int page) {
        return searchTvSerialsRepo.searchTvSerials(query, page);
    }
}
