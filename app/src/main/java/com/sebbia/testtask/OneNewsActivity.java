package com.sebbia.testtask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.sebbia.testtask.api.RetrofitClient;
import com.sebbia.testtask.api.model.OneNews;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OneNewsActivity extends AppCompatActivity {

    TextView tvTitle, tvShortDescription;
    WebView wvFullDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        tvTitle = findViewById(R.id.tvTitle);
        tvShortDescription = findViewById(R.id.tvShortDescription);
        wvFullDescription = findViewById(R.id.wvFullDescription);

        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        String title = bundle.getString("title");
        String description = bundle.getString("description");

        tvTitle.setText(title);
        tvShortDescription.setText(description);

        Call<OneNews> call = RetrofitClient.getInstance().getMyApi().getOneNews(id);
        call.enqueue(new Callback<OneNews>() {
            @Override
            public void onResponse(Call<OneNews> call, Response<OneNews> response) {
                if (response.isSuccessful()) {
                    String html = response.body().news.fullDescription;
                    wvFullDescription.loadData(html, "text/html", "en_US");
                }
            }

            @Override
            public void onFailure(Call<OneNews> call, Throwable t) {
                Toast.makeText(OneNewsActivity.this, "Проверьте соединение с интернетом", Toast.LENGTH_SHORT).show();
            }
        });
    }
}