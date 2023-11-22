package com.example.petshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshop.Helper.ValidationHelper;
import com.example.petshop.R;
import com.example.petshop.callback.StoreCallback;
import com.example.petshop.dao.DaoStore;
import com.example.petshop.databinding.ActivityDangNhapBinding;
import com.example.petshop.model.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class DangNhapActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private ActivityDangNhapBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDangNhapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.loginBtn.setOnClickListener(v -> validateAndLogin());

        binding.btndangky.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), DangKyActivity.class)));
//        btnquenmatkhau.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Quên mật khẩu kệ mẹ mày, đéo thân! :v", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void validateAndLogin() {
        final String username = binding.edtEmail.getText().toString().trim();
        final String password = binding.edtPassword.getText().toString();

        if (validateInput(username, password)) {
            handleLogin(username, password);
        }
    }

    private boolean validateInput(String username, String password) {
        boolean isValid = true;
        if (!ValidationHelper.isNotEmpty(username) || !ValidationHelper.isNotEmpty(password)) {
            binding.edtEmail.setError("Bắt buộc");
            binding.edtPassword.setError("Bắt buộc");
            Toast.makeText(getApplicationContext(), "Vui Lòng Nhập Đầy Đủ 2 Trường", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (!ValidationHelper.isPasswordValid(password)) {
            binding.edtPassword.setError("Mật khẩu phải lớn hơn 6 ký tự");
            isValid = false;
        } else if (!ValidationHelper.isValidEmail(username)) {
            Toast.makeText(getApplicationContext(), "Email Không Hợp Lệ", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void handleLogin(String username, String password) {
        binding.progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        handleSuccessfulLogin();
                    } else {
                        handleFailedLogin();
                    }
                });
    }

    private void handleSuccessfulLogin() {
        Toast.makeText(getApplicationContext(), "Login Thành Công", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("email", binding.edtEmail.getText().toString());
        startActivity(intent);
    }

    private void handleFailedLogin() {
        Toast.makeText(getApplicationContext(), "Login Thất Bại", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(DangNhapActivity.this, MainActivity.class));
        }
    }
}