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
import com.example.petshop.model.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class DangNhapActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    Button mloginBtn, btnLoginPhone, btnLoginGoogle, btnLoginFacebok;
    TextView btndangky, btnquenmatkhau;
    EditText email, pass;
    ProgressBar progressBar;
    DaoStore databaseStore;
    ArrayList<Store> datastore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        btndangky = findViewById(R.id.btndangky);
        email = findViewById(R.id.Email);
        pass = findViewById(R.id.password);
        mloginBtn = findViewById(R.id.loginBtn);
        btnquenmatkhau = findViewById(R.id.btnquenmatkhau);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndLogin();
            }
        });

        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DangKyActivity.class);
                startActivity(i);
            }
        });
//        btnquenmatkhau.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Quên mật khẩu kệ mẹ mày, đéo thân! :v", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void validateAndLogin() {
        final String username = email.getText().toString().trim();
        final String password = pass.getText().toString().trim();

        if (validateInput(username, password)) {
            performLogin(username, password);
        }
    }

    private boolean validateInput(String username, String password) {
        boolean isValid = true;

        if (!ValidationHelper.isNotEmpty(username) || !ValidationHelper.isNotEmpty(password)) {
            email.setError("Bắt buộc");
            pass.setError("Bắt buộc");
            Toast.makeText(getApplicationContext(), "Vui Lòng Nhập Đầy Đủ 2 Trường", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (!ValidationHelper.isPasswordValid(password)) {
            pass.setError("Mật khẩu phải lớn hơn 6 ký tự");
            isValid = false;
        } else if (!ValidationHelper.isValidEmail(username)) {
            Toast.makeText(getApplicationContext(), "Email Không Hợp Lệ", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }


    private void performLogin(String username, String password) {
        // Thực hiện đăng nhập ở đây, sử dụng username và password đã được kiểm tra tính hợp lệ.
        databaseStore = new DaoStore(getApplicationContext());
        datastore = new ArrayList<>();
        databaseStore.getAll(new StoreCallback() {
            @Override
            public void onSuccess(ArrayList<Store> lists) {
                datastore.clear();
                datastore.addAll(lists);
            }

            @Override
            public void onError(String message) {
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            for (int i = 0; i < datastore.size(); i++) {
                                if (datastore.get(i).getEmail().equalsIgnoreCase(email.getText().toString()) && datastore.get(i).getPass().equalsIgnoreCase(pass.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "Login Thành Công", Toast.LENGTH_SHORT).show();
                                    Intent is = new Intent(getApplicationContext(), MainActivity.class);
                                    is.putExtra("email", email.getText().toString());
                                    startActivity(is);
                                    break;
                                } else {
                                    Toast.makeText(getApplicationContext(), "Login Thất Bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Thất Bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
            Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
            startActivity(intent);
        }
        ;
    }
}