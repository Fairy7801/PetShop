package com.example.petshop.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.petshop.R;
import com.example.petshop.callback.StoreCallback;
import com.example.petshop.dao.DaoStore;
import com.example.petshop.fragment.FragmentCategory;
import com.example.petshop.fragment.FragmentManager;
import com.example.petshop.fragment.FragmentProfile;
import com.example.petshop.fragment.FragmentStatistical;
import com.example.petshop.model.Store;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static Toolbar toolbar;
    public static BottomNavigationView bnv;
    private FirebaseUser firebaseUser;
    TextView titletoolbar;
    public static String idstore = "";
    // Đặt tên cho SharedPreferences
    private static final String PREF_NAME = "store_info";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        bnv = findViewById(R.id.bnv);
        toolbar = findViewById(R.id.toolbar);
        titletoolbar = findViewById(R.id.toolbar_title);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titletoolbar.setText("Trang Chủ");
        titletoolbar.setTextSize(30);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        // Khởi tạo đối tượng SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        DaoStore daoStore = new DaoStore(MainActivity.this);
        daoStore.getAll(new StoreCallback() {
            @Override
            public void onSuccess(ArrayList<Store> lists) {
                if (!lists.isEmpty()) {
                    Store firstStore = lists.get(0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("nameStore", firstStore.getName());
                    editor.putString("tokenStore", firstStore.getTokenStore());
                    editor.putString("emailStore", firstStore.getEmail());
                    editor.putString("addressStore", firstStore.getAddress());
                    editor.apply();
                }
            }

            @Override
            public void onError(String message) {

            }
        });

        if (savedInstanceState == null) {
            bnv.setSelectedItemId(R.id.thongke);
            loadFragment(new FragmentStatistical());
        }

        if (intent != null) {
            String fragmentToShow = intent.getStringExtra("fragment_to_show");
            if ("FragmentB".equals(fragmentToShow)) {
                bnv.setSelectedItemId(R.id.category);
                titletoolbar.setText("Sản Phẩm");
                loadFragment(new FragmentCategory());
            }
        }

        toolbar.setNavigationIcon(R.drawable.ic_baseline_sort_);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.thongke:
                        titletoolbar.setText("Thống Kê");
                        fragment = new FragmentStatistical();
                        loadFragment(fragment);
                        return true;
                    case R.id.category:
                        titletoolbar.setText("Sản Phẩm");
                        fragment = new FragmentCategory();
                        loadFragment(fragment);
                        return true;
                    case R.id.manager:
                        titletoolbar.setText("Nhắn Tin");
                        fragment = new FragmentManager();
                        loadFragment(fragment);
                        return true;
                    case R.id.profile:
                        titletoolbar.setText("Thông Tin Cá Nhân");
                        fragment = new FragmentProfile();
                        loadFragment(fragment);
                        return true;
                }

                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fr_l, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}