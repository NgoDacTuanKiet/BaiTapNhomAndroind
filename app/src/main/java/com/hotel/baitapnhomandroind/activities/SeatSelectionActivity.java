package com.hotel.baitapnhomandroind.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hotel.baitapnhomandroind.R;
import com.hotel.baitapnhomandroind.dal.AppDB;
import com.hotel.baitapnhomandroind.entities.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SeatSelectionActivity extends AppCompatActivity {

    private GridView gvSeats;
    private Button btnConfirm;
    private TextView tvTitle;
    private int movieId, theaterId, showtimeId, userId;
    private String showDate, showTime, movieTitle;
    private List<String> allSeats = new ArrayList<>();
    private List<String> bookedSeats = new ArrayList<>();
    private String selectedSeat = "";
    private AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        db = AppDB.getInstance(this);
        
        // Get data from intent
        movieId = getIntent().getIntExtra("MOVIE_ID", -1);
        theaterId = getIntent().getIntExtra("THEATER_ID", -1);
        showDate = getIntent().getStringExtra("SHOW_DATE");
        showTime = getIntent().getStringExtra("SHOW_TIME");
        movieTitle = getIntent().getStringExtra("MOVIE_TITLE");

        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        userId = sp.getInt("userId", -1);

        tvTitle = findViewById(R.id.tvSeatTitle);
        gvSeats = findViewById(R.id.gvSeats);
        btnConfirm = findViewById(R.id.btnConfirmSeat);

        tvTitle.setText("Chọn ghế: " + movieTitle);

        // Generate 25 seats (A1-A5, B1-B5, ..., E1-E5)
        String[] rows = {"A", "B", "C", "D", "E"};
        for (String r : rows) {
            for (int i = 1; i <= 5; i++) {
                allSeats.add(r + i);
            }
        }

        loadData();

        btnConfirm.setOnClickListener(v -> {
            if (selectedSeat.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ghế", Toast.LENGTH_SHORT).show();
            } else {
                bookTicket();
            }
        });
    }

    private void loadData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // 1. Get Showtime ID
            showtimeId = db.showtimeDao().getShowtimeId(theaterId, movieId, showDate, showTime);
            
            // 2. Get booked seats
            bookedSeats = db.ticketDao().getBookedSeats(showtimeId);

            runOnUiThread(() -> {
                SeatAdapter adapter = new SeatAdapter(this, allSeats, bookedSeats);
                gvSeats.setAdapter(adapter);
            });
        });
    }

    private void bookTicket() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Ticket ticket = new Ticket();
            ticket.userId = userId;
            ticket.showtimeId = showtimeId;
            ticket.seatNumber = selectedSeat;

            long ticketId = db.ticketDao().insert(ticket);

            runOnUiThread(() -> {
                Toast.makeText(this, "Đặt vé thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, TicketDetailActivity.class);
                intent.putExtra("TICKET_ID", (int) ticketId);
                startActivity(intent);
                finish();
            });
        });
    }

    private class SeatAdapter extends BaseAdapter {
        private Context context;
        private List<String> seats;
        private List<String> booked;

        public SeatAdapter(Context context, List<String> seats, List<String> booked) {
            this.context = context;
            this.seats = seats;
            this.booked = booked;
        }

        @Override
        public int getCount() {
            return seats.size();
        }

        @Override
        public Object getItem(int position) {
            return seats.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Button btnSeat;
            if (convertView == null) {
                btnSeat = new Button(context);
                btnSeat.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));
                btnSeat.setPadding(8, 8, 8, 8);
            } else {
                btnSeat = (Button) convertView;
            }

            String seat = seats.get(position);
            btnSeat.setText(seat);

            if (booked.contains(seat)) {
                btnSeat.setBackgroundColor(Color.GRAY);
                btnSeat.setEnabled(false);
            } else if (selectedSeat.equals(seat)) {
                btnSeat.setBackgroundColor(Color.GREEN);
            } else {
                btnSeat.setBackgroundColor(Color.LTGRAY);
            }

            btnSeat.setOnClickListener(v -> {
                selectedSeat = seat;
                notifyDataSetChanged();
            });

            return btnSeat;
        }
    }
}
