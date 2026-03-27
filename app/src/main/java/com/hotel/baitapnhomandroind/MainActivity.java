package com.hotel.baitapnhomandroind;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.hotel.baitapnhomandroind.activities.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin, btnViewMovies, btnViewTheaters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        btnViewMovies = findViewById(R.id.btnViewMovies);
        btnViewTheaters = findViewById(R.id.btnViewTheaters);

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnViewTheaters.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TheaterActivity.class);
            startActivity(intent);
        });

        btnViewMovies.setOnClickListener(v -> {
            // TODO: Implement MovieActivity
        });
    }
}
