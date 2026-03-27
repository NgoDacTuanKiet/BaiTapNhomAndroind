package com.hotel.baitapnhomandroind.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hotel.baitapnhomandroind.R;
import com.hotel.baitapnhomandroind.dal.AppDB;
import com.hotel.baitapnhomandroind.entities.User;

public class RegisterActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnRegister;

    AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);

        db = AppDB.getInstance(this);

        btnRegister.setOnClickListener(v -> register());
    }

    private void register() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        // Kiểm tra username đã tồn tại
        boolean exists = db.userDao().getAll().stream()
                .anyMatch(u -> u.username.equals(username));

        if (exists) {
            Toast.makeText(this, "Username đã tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.username = username;
        user.password = password;

        db.userDao().insert(user);

        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}