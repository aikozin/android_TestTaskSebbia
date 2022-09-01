package com.sebbia.testtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sebbia.testtask.api.RetrofitClient;
import com.sebbia.testtask.api.model.AllCategories;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCategoriesActivity extends AppCompatActivity {

    RecyclerView rvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_categories);

        rvList = findViewById(R.id.rvList);

        Call<AllCategories> call = RetrofitClient.getInstance().getMyApi().getAllCategories();
        call.enqueue(new Callback<AllCategories>() {
            @Override
            public void onResponse(Call<AllCategories> call, Response<AllCategories> response) {
                if (response.isSuccessful()) {
                    MyAdapter myAdapter = new MyAdapter(response.body().list);
                    rvList.setLayoutManager(new LinearLayoutManager(ListCategoriesActivity.this));
                    rvList.setAdapter(myAdapter);
                }
            }

            @Override
            public void onFailure(Call<AllCategories> call, Throwable t) {
                Toast.makeText(ListCategoriesActivity.this, "Проверьте соединение с интернетом", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<AllCategories.ListCategories> localDataSet = null;

        MyAdapter(List<AllCategories.ListCategories> dataSet) {
            localDataSet = dataSet;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(View view) {
                super(view);

                view.setOnClickListener(v -> {
                    Intent intent = new Intent(ListCategoriesActivity.this, ListNewsActivity.class);
                    intent.putExtra("id", localDataSet.get(getAdapterPosition()).id);
                    startActivity(intent);
                });

                textView = view.findViewById(R.id.textView);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_list_categories, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.textView.setText(localDataSet.get(position).name);
        }

        @Override
        public int getItemCount() {
            if (localDataSet == null)
                return 0;
            return localDataSet.size();
        }
    }
}