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
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TvSerialsListener {

    private ActivityMainBinding mActivityMainBinding;
    private MostPopularTvSerialsViewModel mViewModel;
    private List<TvSerials> mTvSerialsList = new ArrayList<>();
    private TvSerialAdapter mTvSerialAdapter;
    private static int sCurrentPage = 1;
    private int mTotalAvailablePages = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInInit();
    }

    private void doInInit() {
        mActivityMainBinding.tvSerialsRecycleView.setHasFixedSize(true);
        mViewModel =
                new ViewModelProvider(this).get(MostPopularTvSerialsViewModel.class);
        mTvSerialAdapter = new TvSerialAdapter(this, mTvSerialsList, this);
        mActivityMainBinding.tvSerialsRecycleView.setAdapter(mTvSerialAdapter);
        mActivityMainBinding.tvSerialsRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mActivityMainBinding.tvSerialsRecycleView.canScrollVertically(1)) {
                    if (sCurrentPage < mTotalAvailablePages) {
                        sCurrentPage++;
                        getMostPopularTvSerials();
                    }
                }
            }
        });
        getMostPopularTvSerials();
    }

    private void getMostPopularTvSerials() {
        toggleLoading(sCurrentPage);
        mViewModel.getMostPopularTvSerials(sCurrentPage).observe(this,
                mostPopularTvSerials -> {
                    toggleLoading(sCurrentPage);
                    mActivityMainBinding.setIsLoading(false);
                    if (mostPopularTvSerials != null && mostPopularTvSerials.getTvSerials() != null) {
                        int oldCount = mTvSerialsList.size();
                        mTotalAvailablePages = mostPopularTvSerials.getPages();
                        mTvSerialsList.addAll(mostPopularTvSerials.getTvSerials());
                        mTvSerialAdapter.notifyItemRangeInserted(oldCount, mTvSerialsList.size());
                    }
                });
    }

    private void toggleLoading(int currentPage) {
        if(currentPage == 1) {
            if (mActivityMainBinding.getIsLoading() != null && mActivityMainBinding.getIsLoading()) {
                mActivityMainBinding.setIsLoading(false);
            } else {
                mActivityMainBinding.setIsLoading(true);
            }
        } else {
            if (mActivityMainBinding.getIsLoadingMore() != null && mActivityMainBinding.getIsLoadingMore()) {
                mActivityMainBinding.setIsLoadingMore(false);
            } else {
                mActivityMainBinding.setIsLoadingMore(true);
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