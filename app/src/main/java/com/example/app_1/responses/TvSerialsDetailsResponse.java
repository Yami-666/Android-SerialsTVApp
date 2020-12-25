package com.example.app_1.responses;

import com.example.app_1.models.TvSerialsDetails;
import com.google.gson.annotations.SerializedName;

public class TvSerialsDetailsResponse {
    @SerializedName("tvShow")
    private TvSerialsDetails tvSerialsDetails;

    public TvSerialsDetails getTvSerialsDetails() {
        return tvSerialsDetails;
    }
}
