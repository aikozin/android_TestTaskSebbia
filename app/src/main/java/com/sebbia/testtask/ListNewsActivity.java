package com.sebbia.testtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sebbia.testtask.api.RetrofitClient;
import com.sebbia.testtask.api.model.AllNews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNewsActivity extends AppCompatActivity {

    static int PAGE_SIZE = 10;

    List<AllNews.ListNews> allNews = new ArrayList<>();
    RecyclerView rvList;
    Button btnPrevious, btnQuantPages, btnNext;
    int currentPage = 0, quantityPages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        rvList = findViewById(R.id.rvList);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnQuantPages = findViewById(R.id.btnQuantPages);
        btnNext = findViewById(R.id.btnNext);

        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");

        btnNext.setOnClickListener(v -> {
            currentPage++;

            visualiseData(currentPage);
        });

        btnPrevious.setOnClickListener(v -> {
            currentPage--;

            visualiseData(currentPage);
        });

        getAllNews(id, 0);
    }

    private void visualiseData(int page) {
        quantityPages = (int) Math.ceil((double) allNews.size() / PAGE_SIZE);
        quantityPages = quantityPages == 0 ? 1 : quantityPages;
        btnQuantPages.setText((currentPage + 1) + "/" + quantityPages);

        if (currentPage + 1 < quantityPages)
            btnNext.setEnabled(true);
        else
            btnNext.setEnabled(false);

        if (currentPage > 0)
            btnPrevious.setEnabled(true);
        else
            btnPrevious.setEnabled(false);

        List<AllNews.ListNews> newsForOnePage = new ArrayList<>();
        for (int i = PAGE_SIZE * page; i < PAGE_SIZE * page + PAGE_SIZE && i < allNews.size(); i++) {
            newsForOnePage.add(allNews.get(i));
        }

        MyAdapter myAdapter = new MyAdapter(newsForOnePage);
        rvList.setLayoutManager(new LinearLayoutManager(ListNewsActivity.this));
        rvList.setAdapter(myAdapter);
    }

    private void getAllNews(int id, int page) {
        Call<AllNews> call = RetrofitClient.getInstance().getMyApi().getAllNews(id, page);
        call.enqueue(new Callback<AllNews>() {
            @Override
            public void onResponse(Call<AllNews> call, Response<AllNews> response) {
                if (response.isSuccessful()) {
                    if (response.body().list.size() != 0) {
                        getAllNews(id, page + 1);
                        for (AllNews.ListNews item : response.body().list)
                            allNews.add(item);
                    } else {
                        visualiseData(currentPage);
                    }
                }
            }

            @Override
            public void onFailure(Call<AllNews> call, Throwable t) {
                Toast.makeText(ListNewsActivity.this, "Проверьте соединение с интернетом", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<AllNews.ListNews> localDataSet = null;

        MyAdapter(List<AllNews.ListNews> dataSet) {
            localDataSet = dataSet;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDate, tvDescription;

            public ViewHolder(View view) {
                super(view);

                view.setOnClickListener(v -> {
                    Intent intent = new Intent(ListNewsActivity.this, OneNewsActivity.class);
                    intent.putExtra("id", localDataSet.get(getAdapterPosition()).id);
                    intent.putExtra("title", localDataSet.get(getAdapterPosition()).title);
                    intent.putExtra("description", localDataSet.get(getAdapterPosition()).shortDescription);
                    startActivity(intent);
                });

                tvTitle = view.findViewById(R.id.tvTitle);
                tvDate = view.findViewById(R.id.tvDate);
                tvDescription = view.findViewById(R.id.tvDescription);
            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_list_news, viewGroup, false);

            return new MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder viewHolder, final int position) {
            viewHolder.tvTitle.setText(localDataSet.get(position).title);
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(localDataSet.get(position).date);
                String newDate = new SimpleDateFormat("hh:mm dd.MM.yy", Locale.getDefault()).format(date);
                viewHolder.tvDate.setText(newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            viewHolder.tvDescription.setText(localDataSet.get(position).shortDescription);
        }

        @Override
        public int getItemCount() {
            if (localDataSet == null)
                return 0;
            return localDataSet.size();
        }
    }
}