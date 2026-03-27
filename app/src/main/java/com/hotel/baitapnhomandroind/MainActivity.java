package com.hotel.baitapnhomandroind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hotel.baitapnhomandroind.activities.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin, btnViewMovies, btnViewTheaters, btnLogout;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogin = findViewById(R.id.btnLogin);
        btnViewMovies = findViewById(R.id.btnViewMovies);
        btnViewTheaters = findViewById(R.id.btnViewTheaters);
        btnLogout = findViewById(R.id.btnLogout);

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
            Toast.makeText(this, "Chức năng xem phim đang phát triển", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            logout();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLoginStatus();
    }

    private void checkLoginStatus() {
        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        String username = sp.getString("username", "");

        if (isLogin) {
            tvWelcome.setText("Xin chào, " + username + "!");
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            tvWelcome.setText("Movie Ticket App");
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    private void logout() {
        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
        checkLoginStatus();
    }
}
