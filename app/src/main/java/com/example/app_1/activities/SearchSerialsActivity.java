package com.example.app_1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.app_1.R;
import com.example.app_1.adapters.TvSerialAdapter;
import com.example.app_1.databinding.ActivitySearchSerialsBinding;
import com.example.app_1.listeners.TvSerialsListener;
import com.example.app_1.models.TvSerials;
import com.example.app_1.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchSerialsActivity extends AppCompatActivity implements TvSerialsListener {

    private ActivitySearchSerialsBinding mActivitySearchBinding;
    private SearchViewModel mSearchViewModel;
    private final List<TvSerials> mTvSerialsList = new ArrayList<>();
    private TvSerialAdapter mTvSerialAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySearchBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_search_serials);
        doInit();
    }

    private void doInit() {
        mActivitySearchBinding.imageBack.setOnClickListener(v -> onBackPressed());
        mActivitySearchBinding.tvSerialsRecycleView.setHasFixedSize(true);
        mSearchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        mTvSerialAdapter = new TvSerialAdapter(this, mTvSerialsList, this);
        mActivitySearchBinding.tvSerialsRecycleView.setAdapter(mTvSerialAdapter);
        mActivitySearchBinding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                currentPage = 1;
                                totalAvailablePages = 1;
                                searchTvSerials(s.toString());
                            });
                        }
                    }, 600);
                } else {
                    mTvSerialsList.clear();
                    mTvSerialAdapter.notifyDataSetChanged();
                }
            }
        });

        mActivitySearchBinding.tvSerialsRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mActivitySearchBinding.tvSerialsRecycleView.canScrollVertically(1)) {
                    if (!mActivitySearchBinding.inputSearch.getText().toString().isEmpty()) {
                        if (currentPage <= totalAvailablePages) {
                            currentPage += 1;
                            searchTvSerials(mActivitySearchBinding.inputSearch.getText().toString());

                        }
                    }
                }
            }
        });

        mActivitySearchBinding.inputSearch.requestFocus();

    }

    private void searchTvSerials(String query) {
        toggleLoading();
        mSearchViewModel.searchTvSerials(query, currentPage).observe(this, tvSerialsResponse -> {
            toggleLoading();
            if (tvSerialsResponse != null) {
                totalAvailablePages = tvSerialsResponse.getPages();
                if (tvSerialsResponse.getTvSerials() != null) {
                    int oldCount = mTvSerialsList.size();
                    mTvSerialsList.addAll(tvSerialsResponse.getTvSerials());
                    mTvSerialAdapter.notifyItemRangeInserted(oldCount, mTvSerialsList.size());
                }

            }
        });
    }

    private void toggleLoading() {
        if (currentPage == 1) {
            mActivitySearchBinding.setIsLoading(mActivitySearchBinding.getIsLoading() == null
                    || !mActivitySearchBinding.getIsLoading());
        } else {
            mActivitySearchBinding.setIsLoadingMore(mActivitySearchBinding.getIsLoadingMore() == null
                    || !mActivitySearchBinding.getIsLoadingMore());
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