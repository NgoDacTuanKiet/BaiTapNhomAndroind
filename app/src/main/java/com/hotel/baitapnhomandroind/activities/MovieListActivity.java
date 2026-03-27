package com.hotel.baitapnhomandroind.activities;

import android.os.Bundle;
import android.widget.Button;

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
    private Button btnBack; // Đổi từ ImageButton sang Button để khớp với XML mới

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        btnBack = findViewById(R.id.btnBack);
        rvMovies = findViewById(R.id.rvMovies);
        
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(new ArrayList<>());
        rvMovies.setAdapter(movieAdapter);

        // Xử lý sự kiện nút Back (giờ là Button ở cuối màn hình)
        btnBack.setOnClickListener(v -> finish());

        db = AppDB.getInstance(this);
        
        // Quan sát dữ liệu từ LiveData
        db.movieDao().getAll().observe(this, movies -> {
            if (movies != null) {
                movieAdapter.setMovies(movies);
            }
        });
    }
}
