package com.hotel.baitapnhomandroind.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hotel.baitapnhomandroind.R;
import com.hotel.baitapnhomandroind.dal.AppDB;
import com.hotel.baitapnhomandroind.entities.User;

import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword, edtConfirmPassword;
    Button btnRegister, btnBackToLogin;

    AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        db = AppDB.getInstance(this);

        btnRegister.setOnClickListener(v -> register());
        
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void register() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirm = edtConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            // Kiểm tra username đã tồn tại
            int count = db.userDao().countUsername(username);

            if (count > 0) {
                runOnUiThread(() -> 
                    Toast.makeText(this, "Username đã tồn tại", Toast.LENGTH_SHORT).show()
                );
            } else {
                User user = new User();
                user.username = username;
                user.password = password;
                db.userDao().insert(user);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại màn hình Login
                });
            }
        });
    }
}
