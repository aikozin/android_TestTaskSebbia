package com.sebbia.testtask.api;

import com.sebbia.testtask.api.model.AllCategories;
import com.sebbia.testtask.api.model.AllNews;
import com.sebbia.testtask.api.model.OneNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "http://testtask.sebbia.com";

    @GET("/v1/news/categories")
    Call<AllCategories> getAllCategories();

    @GET("/v1/news/categories/{id}/news")
    Call<AllNews> getAllNews(
            @Path("id") int id,
            @Query("page") int page
    );

    @GET("/v1/news/details")
    Call<OneNews> getOneNews(
            @Query("id") int id
    );
}
