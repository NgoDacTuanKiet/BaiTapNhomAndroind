package com.hotel.baitapnhomandroind.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hotel.baitapnhomandroind.R;
import com.hotel.baitapnhomandroind.dal.AppDB;
import com.hotel.baitapnhomandroind.entities.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MovieAtShowtimeActivity extends AppCompatActivity {

    private RecyclerView rvMovies;
    private TextView tvShowtimeHeader;
    private Button btnBack;
    private int theaterId;
    private String showDate;
    private String showTime;
    private MovieAdapter adapter;
    private List<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_at_showtime);

        theaterId = getIntent().getIntExtra("THEATER_ID", -1);
        showDate = getIntent().getStringExtra("SHOW_DATE");
        showTime = getIntent().getStringExtra("SHOW_TIME");

        tvShowtimeHeader = findViewById(R.id.tvShowtimeHeader);
        rvMovies = findViewById(R.id.rvMoviesAtShowtime);
        btnBack = findViewById(R.id.btnBackMovieAtShowtime);

        tvShowtimeHeader.setText("Phim chiếu vào: " + showDate + " " + showTime);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter(movieList);
        rvMovies.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        loadMovies();
    }

    private void loadMovies() {
        AppDB db = AppDB.getInstance(this);
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Movie> movies = db.showtimeDao().getMoviesByShowtime(theaterId, showDate, showTime);
            runOnUiThread(() -> {
                movieList.clear();
                movieList.addAll(movies);
                adapter.notifyDataSetChanged();
            });
        });
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
        private List<Movie> movies;

        public MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_with_book, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Movie movie = movies.get(position);
            holder.tvTitle.setText(movie.title);
            holder.tvDuration.setText("Thời lượng: " + movie.duration + " phút");
            holder.btnBook.setOnClickListener(v -> {
                // Ở đây bạn có thể mở màn hình thanh toán hoặc xác nhận đặt vé
                Toast.makeText(MovieAtShowtimeActivity.this, "Đã chọn đặt vé phim: " + movie.title, Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDuration;
            Button btnBook;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvMovieTitleBook);
                tvDuration = itemView.findViewById(R.id.tvMovieDurationBook);
                btnBook = itemView.findViewById(R.id.btnBookTicket);
            }
        }
    }
}
