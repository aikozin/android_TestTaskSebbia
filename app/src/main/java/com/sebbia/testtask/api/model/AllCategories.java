package com.sebbia.testtask.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AllCategories {

    @SerializedName("code")
    public int code;
    @SerializedName("list")
    public List<ListCategories> list = null;

    public static class ListCategories {

        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
    }
}
