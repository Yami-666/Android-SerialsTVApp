package com.example.app_1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewOutlineProvider;
import android.widget.Toast;

import com.example.app_1.R;
import com.example.app_1.adapters.TvSerialAdapter;
import com.example.app_1.databinding.ActivityMainBinding;
import com.example.app_1.listeners.TvSerialsListener;
import com.example.app_1.models.TvSerials;
import com.example.app_1.repo.MostPopularTvSerialsRepo;
import com.example.app_1.responses.TvSerialsResponse;
import com.example.app_1.viewmodel.MostPopularTvSerialsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TvSerialsListener {

    private ActivityMainBinding activityMainBinding;
    private MostPopularTvSerialsViewModel viewModel;
    private List<TvSerials> tvSerialsList = new ArrayList<>();
    private TvSerialAdapter tvSerialAdapter;
    private static int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInInitialization();
    }

    private void doInInitialization() {
        activityMainBinding.tvSerialsRecycleView.setHasFixedSize(true);
        viewModel =
                new ViewModelProvider(this).get(MostPopularTvSerialsViewModel.class);
        tvSerialAdapter = new TvSerialAdapter(this, tvSerialsList, this);
        activityMainBinding.tvSerialsRecycleView.setAdapter(tvSerialAdapter);
        activityMainBinding.tvSerialsRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activityMainBinding.tvSerialsRecycleView.canScrollVertically(1)) {
                    if (currentPage < totalAvailablePages) {
                        currentPage++;
                        getMostPopularTvSerials();
                    }
                }
            }
        });
        getMostPopularTvSerials();
    }

    private void getMostPopularTvSerials() {
        toggleLoading(currentPage);
        viewModel.getMostPopularTvSerials(currentPage).observe(this,
                mostPopularTvSerials -> {
                    toggleLoading(currentPage);
                    activityMainBinding.setIsLoading(false);
                    if (mostPopularTvSerials != null && mostPopularTvSerials.getTvSerials() != null) {
                        int oldCount = tvSerialsList.size();
                        totalAvailablePages = mostPopularTvSerials.getPages();
                        tvSerialsList.addAll(mostPopularTvSerials.getTvSerials());
                        tvSerialAdapter.notifyItemRangeInserted(oldCount, tvSerialsList.size());
                    }
                });
    }

    private void toggleLoading(int currentPage) {
        if(currentPage == 1) {
            if (activityMainBinding.getIsLoading() != null && activityMainBinding.getIsLoading()) {
                activityMainBinding.setIsLoading(false);
            } else {
                activityMainBinding.setIsLoading(true);
            }
        } else {
            if (activityMainBinding.getIsLoadingMore() != null && activityMainBinding.getIsLoadingMore()) {
                activityMainBinding.setIsLoadingMore(false);
            } else {
                activityMainBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onTvSerialClicked(TvSerials tvSerials) {
        Intent intent = new Intent(getApplicationContext(), TvSerialsDetailsActivity.class);
        intent.putExtra("id", tvSerials.getId());
        intent.putExtra("name", tvSerials.getName());
        intent.putExtra("country", tvSerials.getCountry());
        intent.putExtra("network", tvSerials.getNetwork());
        intent.putExtra("status", tvSerials.getStatus());
        intent.putExtra("startDate", tvSerials.getStartDate());
        startActivity(intent);
    }
}