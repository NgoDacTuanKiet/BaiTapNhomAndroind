package com.hotel.baitapnhomandroind.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hotel.baitapnhomandroind.MainActivity;
import com.hotel.baitapnhomandroind.R;
import com.hotel.baitapnhomandroind.dal.AppDB;
import com.hotel.baitapnhomandroind.entities.User;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin, btnGoRegister;

    AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);

        db = AppDB.getInstance(this);

        btnLogin.setOnClickListener(v -> login());

        btnGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void login() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        User user = db.userDao().login(username, password);

        if (user != null) {
            SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("username", user.username);
            editor.putBoolean("isLogin", true);
            editor.apply();

            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        edtUsername.setText("");
        edtPassword.setText("");
    }
}