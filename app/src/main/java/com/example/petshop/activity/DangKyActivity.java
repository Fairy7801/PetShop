package com.example.petshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.petshop.Helper.ValidationHelper;
import com.example.petshop.R;
import com.example.petshop.dao.DaoStore;
import com.example.petshop.databinding.ActivityDangKyBinding;
import com.example.petshop.model.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DangKyActivity extends AppCompatActivity {
    private ActivityDangKyBinding binding;
    private DaoStore databaseStore;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDangKyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database =FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Store");
        mAuth = FirebaseAuth.getInstance();

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = binding.emailsignup.getText().toString().trim();
                String pass = binding.passsignup.getText().toString();
                String nhappass = binding.nhaplaipass.getText().toString();

                if (validateRegistrationInput(email, pass, nhappass)) {
                    performRegistration(email, pass);
                }
            }
        });
    }

    private boolean validateRegistrationInput(String email, String pass, String nhappass) {
        boolean isValid = true;

        if (!ValidationHelper.isValidEmail(email)) {
            binding.emailsignup.setError("Email không hợp lệ.");
            isValid = false;
        }

        if (!ValidationHelper.isNotEmpty(email)) {
            binding.emailsignup.setError("Bắt buộc");
            isValid = false;
        }

        if (!ValidationHelper.isNotEmpty(pass)) {
            binding.passsignup.setError("Bắt buộc");
            isValid = false;
        }

        if (!ValidationHelper.isNotEmpty(nhappass)) {
            binding.nhaplaipass.setError("Bắt buộc");
            isValid = false;
        }

        if (!ValidationHelper.isPasswordValid(pass)) {
            binding.passsignup.setError("Mật khẩu phải lớn hơn 6 ký tự");
            isValid = false;
        }

        if (!pass.equals(nhappass)) {
            binding.nhaplaipass.setError("Mật khẩu không khớp");
            isValid = false;
        }

        return isValid;
    }


    private void performRegistration(String email, String pass) {
        binding.profileProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    binding.profileProgressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        databaseStore = new DaoStore(getApplicationContext());
                        Store store = new Store(mAuth.getUid(), email, pass, null, null, null, null, null);
                        databaseStore.insert(store);
                        startActivity(new Intent(getApplicationContext(), DangNhapActivity.class));
                        finish();
                    }
                });
    }
}