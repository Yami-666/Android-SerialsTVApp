package com.example.app_1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.text.TextUtils;
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
import java.util.Locale;

public class TvSerialsDetailsActivity extends AppCompatActivity {

    private ActivityTvSerialsDetailsBinding mActivityTvSerialsDetailsBinding;
    private TvSerialsDetailsIViewModel mTvSerialsDetailsIViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityTvSerialsDetailsBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_tv_serials_details);
        init();
    }

    private void init() {
        mTvSerialsDetailsIViewModel =
                new ViewModelProvider(this).get(TvSerialsDetailsIViewModel.class);
        mActivityTvSerialsDetailsBinding.imageBack.setOnClickListener(v -> {
            onBackPressed();
        });
        getTvSerialsDetails();
    }

    private void getTvSerialsDetails() {
        mActivityTvSerialsDetailsBinding.setIsLoading(true);
        String tvSerialsId = String.valueOf(getIntent().getIntExtra("id", -1));
        mTvSerialsDetailsIViewModel.getTvSerialsDetails(tvSerialsId).observe(this,
                tvSerialsDetailsResponse -> {
                    mActivityTvSerialsDetailsBinding.setIsLoading(false);
                    if (tvSerialsDetailsResponse.getTvSerialsDetails() != null) {
                        if (tvSerialsDetailsResponse.getTvSerialsDetails().getPictures() != null) {
                            loadImageSlider(tvSerialsDetailsResponse.getTvSerialsDetails().getPictures());
                        }
                        mActivityTvSerialsDetailsBinding.setTvSerialsImageUrl(tvSerialsDetailsResponse.getTvSerialsDetails().getImagePath());
                        mActivityTvSerialsDetailsBinding.imageTvSerials.setVisibility(View.VISIBLE);
                        mActivityTvSerialsDetailsBinding.setDescription(
                                String.valueOf(
                                        HtmlCompat.fromHtml(
                                                tvSerialsDetailsResponse.getTvSerialsDetails().getDescription(),
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                        )
                                )
                        );
                        mActivityTvSerialsDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                        mActivityTvSerialsDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                        mActivityTvSerialsDetailsBinding.textReadMore.setOnClickListener(v -> {
                            if (mActivityTvSerialsDetailsBinding.textReadMore.getText().equals(
                                    "Read more")) {
                                mActivityTvSerialsDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE );
                                mActivityTvSerialsDetailsBinding.textDescription.setEllipsize(null);
                                mActivityTvSerialsDetailsBinding.textReadMore.setText("~");
                            } else {
                                mActivityTvSerialsDetailsBinding.textDescription.setMaxLines(4);
                                mActivityTvSerialsDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                mActivityTvSerialsDetailsBinding.textReadMore.setText(R.string.read_more);
                            }
                        });
                        mActivityTvSerialsDetailsBinding.setTvRating(
                                String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble(tvSerialsDetailsResponse.getTvSerialsDetails().getRating())
                                )
                        );
                        if (tvSerialsDetailsResponse.getTvSerialsDetails().getGenres() != null) {
                            mActivityTvSerialsDetailsBinding.setGenre(tvSerialsDetailsResponse.getTvSerialsDetails().getGenres()[0]);
                        } else {
                            mActivityTvSerialsDetailsBinding.setGenre("N/A");
                        }
                        mActivityTvSerialsDetailsBinding.setRuntime(tvSerialsDetailsResponse.getTvSerialsDetails().getRuntime() + "Min");
                        mActivityTvSerialsDetailsBinding.textRuntime.setVisibility(View.VISIBLE);
                        mActivityTvSerialsDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                        mActivityTvSerialsDetailsBinding.viewDiv1.setVisibility(View.VISIBLE);
                        mActivityTvSerialsDetailsBinding.viewDiv2.setVisibility(View.VISIBLE);
                        loadBasicTvSerialsDetails();
                    }
                });
    }

    private void loadImageSlider(String[] sliderImages) {
        mActivityTvSerialsDetailsBinding.sliderViewPage.setOffscreenPageLimit(1);
        mActivityTvSerialsDetailsBinding.sliderViewPage.setAdapter(new ImageSliderAdapter(sliderImages));
        mActivityTvSerialsDetailsBinding.sliderViewPage.setVisibility(View.VISIBLE);
        mActivityTvSerialsDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupIndicators(sliderImages.length);
        mActivityTvSerialsDetailsBinding.sliderViewPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.background_slider_indicator));
            indicators[i].setLayoutParams(layoutParams);
            mActivityTvSerialsDetailsBinding.layoutIndicators.addView(indicators[i]);
        }
        mActivityTvSerialsDetailsBinding.layoutIndicators.setVisibility(View.VISIBLE);
        setupCurrentIndicator(0);
    }

    private void setupCurrentIndicator(int position) {
        int childCount = mActivityTvSerialsDetailsBinding.layoutIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView =
                    (ImageView) mActivityTvSerialsDetailsBinding.layoutIndicators.getChildAt(i);

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

    private void loadBasicTvSerialsDetails() {
        mActivityTvSerialsDetailsBinding.setTvSerialsName(getIntent().getStringExtra("name"));
        mActivityTvSerialsDetailsBinding.setStatus(getIntent().getStringExtra("status"));
        mActivityTvSerialsDetailsBinding.setNetworkCountry(getIntent().getStringExtra(
                "network") + "(" + getIntent().getStringExtra("country") + ")");
        mActivityTvSerialsDetailsBinding.setStartedDate(getIntent().getStringExtra("startDate"));
        mActivityTvSerialsDetailsBinding.textNameSerial.setVisibility(View.VISIBLE);
        mActivityTvSerialsDetailsBinding.textNetworkCountrySerial.setVisibility(View.VISIBLE);
        mActivityTvSerialsDetailsBinding.textStarted.setVisibility(View.VISIBLE);
        mActivityTvSerialsDetailsBinding.textStatusSerial.setVisibility(View.VISIBLE);

    }
}

