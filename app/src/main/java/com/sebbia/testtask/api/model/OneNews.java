package com.sebbia.testtask.api.model;

import com.google.gson.annotations.SerializedName;

public class OneNews {

    @SerializedName("code")
    public Integer code;
    @SerializedName("news")
    public News news;

    public class News {

        @SerializedName("id")
        public Integer id;
        @SerializedName("title")
        public String title;
        @SerializedName("date")
        public String date;
        @SerializedName("shortDescription")
        public String shortDescription;
        @SerializedName("fullDescription")
        public String fullDescription;
    }
}