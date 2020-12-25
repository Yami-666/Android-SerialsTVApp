    package com.example.app_1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.example.app_1.R;
import com.example.app_1.databinding.ActivityTvSerialsDetailsBinding;
import com.example.app_1.viewmodel.TvSerialsDetailsIViewModel;

public class TvSerialsDetailsActivity extends AppCompatActivity {

    private ActivityTvSerialsDetailsBinding activityTvSerialsDetailsBinding;
    private TvSerialsDetailsIViewModel tvSerialsDetailsIViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvSerialsDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tv_serials_details);
        init();
    }

    private void init() {
        tvSerialsDetailsIViewModel =
                new ViewModelProvider(this).get(TvSerialsDetailsIViewModel.class);
        getTvSerialsDetails();
    }

    private void getTvSerialsDetails() {
        activityTvSerialsDetailsBinding.setIsLoading(true);
        String tvSerialsId = String.valueOf(getIntent().getIntExtra("id", -1));
        tvSerialsDetailsIViewModel.getTvSerialsDetails(tvSerialsId).observe(this, tvSerialsDetailsResponse -> {
            activityTvSerialsDetailsBinding.setIsLoading(false);
            Toast.makeText(this, tvSerialsDetailsResponse.getTvSerialsDetails().getUrl(),
                    Toast.LENGTH_SHORT).show();
        });
    }
}