package com.hotel.baitapnhomandroind;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hotel.baitapnhomandroind.dal.AppDB;
import com.hotel.baitapnhomandroind.entities.Theater;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class TheaterActivity extends AppCompatActivity {

    private RecyclerView rvTheaters;
    private TheaterAdapter adapter;
    private Button btnBack;
    private List<Theater> theaterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater);

        rvTheaters = findViewById(R.id.rvTheaters);
        btnBack = findViewById(R.id.btnBack);

        rvTheaters.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TheaterAdapter(theaterList);
        rvTheaters.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        loadTheaters();
    }

    private void loadTheaters() {
        AppDB db = AppDB.getInstance(this);
        Executors.newSingleThreadExecutor().execute(() -> {
            // Lấy danh sách từ DB
            List<Theater> theaters = db.theaterDao().getAll();
            
            // Nếu DB trống (do callback onCreate chưa kịp chạy hoặc lỗi), thêm tạm dữ liệu cứng
            if (theaters == null || theaters.isEmpty()) {
                Theater t1 = new Theater();
                t1.name = "CGV Vincom Center";
                t1.location = "Bà Triệu, Hà Nội";
                db.theaterDao().insert(t1);

                Theater t2 = new Theater();
                t2.name = "Lotte Cinema Landmark";
                t2.location = "Phạm Hùng, Hà Nội";
                db.theaterDao().insert(t2);

                Theater t3 = new Theater();
                t3.name = "BHD Star Discovery";
                t3.location = "Cầu Giấy, Hà Nội";
                db.theaterDao().insert(t3);
                
                theaters = db.theaterDao().getAll();
            }

            List<Theater> finalTheaters = theaters;
            runOnUiThread(() -> {
                theaterList.clear();
                theaterList.addAll(finalTheaters);
                adapter.notifyDataSetChanged();
            });
        });
    }
}
