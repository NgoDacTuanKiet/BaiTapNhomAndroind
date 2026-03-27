package com.hotel.baitapnhomandroind.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hotel.baitapnhomandroind.R;
import com.hotel.baitapnhomandroind.dal.AppDB;
import com.hotel.baitapnhomandroind.dal.dao.ShowtimeDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ShowtimeActivity extends AppCompatActivity {

    private RecyclerView rvShowtimeGroups;
    private TextView tvTheaterNameHeader;
    private Button btnBack;
    private int theaterId;
    private String theaterName;
    private ShowtimeGroupAdapter adapter;
    private List<ShowtimeDao.ShowtimeDateGroup> groupList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime);

        theaterId = getIntent().getIntExtra("THEATER_ID", -1);
        theaterName = getIntent().getStringExtra("THEATER_NAME");

        tvTheaterNameHeader = findViewById(R.id.tvTheaterNameHeader);
        rvShowtimeGroups = findViewById(R.id.rvShowtimeGroups);
        btnBack = findViewById(R.id.btnBackShowtime);

        tvTheaterNameHeader.setText("Rạp: " + theaterName);
        rvShowtimeGroups.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShowtimeGroupAdapter(groupList);
        rvShowtimeGroups.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        loadShowtimeGroups();
    }

    private void loadShowtimeGroups() {
        AppDB db = AppDB.getInstance(this);
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ShowtimeDao.ShowtimeDateGroup> groups = db.showtimeDao().getDistinctDateTimesByTheater(theaterId);
            runOnUiThread(() -> {
                groupList.clear();
                groupList.addAll(groups);
                adapter.notifyDataSetChanged();
            });
        });
    }

    private class ShowtimeGroupAdapter extends RecyclerView.Adapter<ShowtimeGroupAdapter.ViewHolder> {
        private List<ShowtimeDao.ShowtimeDateGroup> groups;

        public ShowtimeGroupAdapter(List<ShowtimeDao.ShowtimeDateGroup> groups) {
            this.groups = groups;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime_group, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ShowtimeDao.ShowtimeDateGroup group = groups.get(position);
            holder.tvInfo.setText("Ngày: " + group.showDate + " | Giờ: " + group.showTime);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(ShowtimeActivity.this, MovieAtShowtimeActivity.class);
                intent.putExtra("THEATER_ID", theaterId);
                intent.putExtra("SHOW_DATE", group.showDate);
                intent.putExtra("SHOW_TIME", group.showTime);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return groups.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvInfo;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvInfo = itemView.findViewById(R.id.tvShowtimeInfo);
            }
        }
    }
}
