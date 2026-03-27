package com.hotel.baitapnhomandroind.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hotel.baitapnhomandroind.MainActivity;
import com.hotel.baitapnhomandroind.R;
import com.hotel.baitapnhomandroind.dal.AppDB;
import com.hotel.baitapnhomandroind.entities.Movie;
import com.hotel.baitapnhomandroind.entities.Showtime;
import com.hotel.baitapnhomandroind.entities.Theater;
import com.hotel.baitapnhomandroind.entities.Ticket;

import java.util.concurrent.Executors;

public class TicketDetailActivity extends AppCompatActivity {

    private TextView tvMovie, tvTheater, tvTime, tvSeat;
    private Button btnHome;
    private AppDB db;
    private int ticketId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        db = AppDB.getInstance(this);
        ticketId = getIntent().getIntExtra("TICKET_ID", -1);

        tvMovie = findViewById(R.id.tvDetailMovieTitle);
        tvTheater = findViewById(R.id.tvDetailTheater);
        tvTime = findViewById(R.id.tvDetailTime);
        tvSeat = findViewById(R.id.tvDetailSeat);
        btnHome = findViewById(R.id.btnBackToHome);

        loadTicketInfo();

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadTicketInfo() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Ticket ticket = db.ticketDao().getById(ticketId);
            if (ticket != null) {
                Showtime showtime = db.showtimeDao().getById(ticket.showtimeId);
                Movie movie = db.movieDao().getById(showtime.movieId);
                Theater theater = db.theaterDao().getById(showtime.theaterId);

                runOnUiThread(() -> {
                    tvMovie.setText(movie.title);
                    tvTheater.setText("Rạp: " + theater.name + " (" + theater.location + ")");
                    tvTime.setText("Thời gian: " + showtime.showDate + " " + showtime.showTime);
                    tvSeat.setText("Ghế: " + ticket.seatNumber);
                });
            }
        });
    }
}
