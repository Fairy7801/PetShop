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
import com.example.petshop.model.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DangKyActivity extends AppCompatActivity {
    EditText emailsignup, passsignup, nhaplaipass;
    Button btnsignup;
    private DaoStore databaseStore;
    ArrayList<Store> datastore;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);


        emailsignup = findViewById(R.id.emailsignup);
        passsignup = findViewById(R.id.passsignup);
        nhaplaipass = findViewById(R.id.nhaplaipass);
        btnsignup = findViewById(R.id.signup);

        database =FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Store");
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.profile_progressBar);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailsignup.getText().toString().trim();
                String pass = passsignup.getText().toString().trim();
                String nhappass = nhaplaipass.getText().toString().trim();

                if (validateRegistrationInput(email, pass, nhappass)) {
                    performRegistration(email, pass);
                }
            }
        });
    }

    private boolean validateRegistrationInput(String email, String pass, String nhappass) {
        boolean isValid = true;

        if (!ValidationHelper.isValidEmail(email)) {
            emailsignup.setError("Email không hợp lệ.");
            isValid = false;
        }

        if (!ValidationHelper.isNotEmpty(email)) {
            emailsignup.setError("Bắt buộc");
            isValid = false;
        }

        if (!ValidationHelper.isNotEmpty(pass)) {
            passsignup.setError("Bắt buộc");
            isValid = false;
        }

        if (!ValidationHelper.isNotEmpty(nhappass)) {
            nhaplaipass.setError("Bắt buộc");
            isValid = false;
        }

        if (!ValidationHelper.isPasswordValid(pass)) {
            passsignup.setError("Mật khẩu phải lớn hơn 6 ký tự");
            isValid = false;
        }

        if (!pass.equals(nhappass)) {
            nhaplaipass.setError("Mật khẩu không khớp");
            isValid = false;
        }

        return isValid;
    }


    private void performRegistration(String email, String pass) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            databaseStore = new DaoStore(getApplicationContext());
                            Store store = new Store(mAuth.getUid(), email, pass, null, null, null, null, null);
                            databaseStore.insert(store);
                            startActivity(new Intent(getApplicationContext(), DangNhapActivity.class));
                            finish();
                        }
                    }
                });
    }
}