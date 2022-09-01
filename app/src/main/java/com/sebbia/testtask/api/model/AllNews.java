package com.sebbia.testtask.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllNews {

    @SerializedName("code")
    public int code;
    @SerializedName("list")
    public List<ListNews> list = null;

    public class ListNews {

        @SerializedName("id")
        public int id;
        @SerializedName("title")
        public String title;
        @SerializedName("date")
        public String date;
        @SerializedName("shortDescription")
        public String shortDescription;
    }
}
