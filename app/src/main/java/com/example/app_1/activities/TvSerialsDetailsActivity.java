    package com.example.app_1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.app_1.R;
import com.example.app_1.adapters.ImageSliderAdapter;
import com.example.app_1.databinding.ActivityTvSerialsDetailsBinding;
import com.example.app_1.viewmodel.TvSerialsDetailsIViewModel;

import java.nio.InvalidMarkException;

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
            if (tvSerialsDetailsResponse.getTvSerialsDetails() != null) {
                if (tvSerialsDetailsResponse.getTvSerialsDetails().getPictures() != null) {
                    loadImageSlider(tvSerialsDetailsResponse.getTvSerialsDetails().getPictures());
                }
            }
        });
    }

    private void loadImageSlider(String[] sliderImages) {
        activityTvSerialsDetailsBinding.sliderViewPage.setOffscreenPageLimit(1);
        activityTvSerialsDetailsBinding.sliderViewPage.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTvSerialsDetailsBinding.sliderViewPage.setVisibility(View.VISIBLE);
        activityTvSerialsDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupIndicators(sliderImages.length);
        activityTvSerialsDetailsBinding.sliderViewPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setupCurrentIndicator(position);
            }
        });
    }

    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.background_slider_indicator));
            indicators[i].setLayoutParams(layoutParams);
            activityTvSerialsDetailsBinding.layoutIndicators.addView(indicators[i]);
        }
        activityTvSerialsDetailsBinding.layoutIndicators.setVisibility(View.VISIBLE);
        setupCurrentIndicator(0);
    }

    private void setupCurrentIndicator(int position) {
        int childCount = activityTvSerialsDetailsBinding.layoutIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView =
                    (ImageView) activityTvSerialsDetailsBinding.layoutIndicators.getChildAt(i);

            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.background_slider_indicator_active));
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.background_slider_indicator));
            }
        }
    }
}

