package com.example.app_1.utilities;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class BindingAdapters {
    @BindingAdapter("android:imageURL")
    public static void setImageUrl(ImageView imageView, String Url) {
        try {
            imageView.setAlpha(0f);
            Picasso.get().load(Url).noFade().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    imageView.animate().setDuration(300).alpha(1f).start();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        } catch (Exception ignored) {

        }

    }
}
