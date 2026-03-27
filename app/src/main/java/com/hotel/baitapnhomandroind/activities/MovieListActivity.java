package com.hotel.baitapnhomandroind.activities;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hotel.baitapnhomandroind.R;
import com.hotel.baitapnhomandroind.adapters.MovieAdapter;
import com.hotel.baitapnhomandroind.dal.AppDB;

import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity {

    private RecyclerView rvMovies;
    private MovieAdapter movieAdapter;
    private AppDB db;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        btnBack = findViewById(R.id.btnBack);
        rvMovies = findViewById(R.id.rvMovies);
        
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(new ArrayList<>());
        rvMovies.setAdapter(movieAdapter);

        btnBack.setOnClickListener(v -> finish());

        db = AppDB.getInstance(this);
        
        // Sử dụng LiveData để tự động cập nhật UI khi database có thay đổi
        db.movieDao().getAll().observe(this, movies -> {
            if (movies != null) {
                movieAdapter.setMovies(movies);
            }
        });
    }
}
