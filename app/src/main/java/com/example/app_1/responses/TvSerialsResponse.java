package com.example.app_1.responses;

import com.example.app_1.models.TvSerials;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvSerialsResponse {
    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int pages;

    @SerializedName("tv_shows")
    private List<TvSerials> tvSerials;

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public List<TvSerials> getTvSerials() {
        return tvSerials;
    }
}
