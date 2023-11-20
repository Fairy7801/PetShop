package com.example.petshop.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.petshop.R;
import com.example.petshop.adapter.AcceptAdapter;
import com.example.petshop.adapter.GiaoDichAdapter;
import com.example.petshop.adapter.ProductAdapter;
import com.example.petshop.callback.HDCTCallback;
import com.example.petshop.callback.ProductsCallback;
import com.example.petshop.dao.Da0HDCT;
import com.example.petshop.model.HDCT;
import com.example.petshop.model.Products;
import com.example.petshop.model.Store;
import com.example.petshop.model.Token;
import com.example.petshop.notification.DataHoaDon;
import com.example.petshop.notification.SenderHoaDon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GiaoDichActivity extends AppCompatActivity {
    TextView titletoolbar;
    Toolbar toolbar;
    TextView txthetdonhang;
    private Da0HDCT daoHDCT;
    private ArrayList<HDCT> hdctArrayList;
    private GiaoDichAdapter giaoDichAdapter;
    private RecyclerView rcvgiaodich;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dich);
        titletoolbar = findViewById(R.id.toolbar_title);
        txthetdonhang = findViewById(R.id.txthetdonhang);
        rcvgiaodich = findViewById(R.id.rcvgiaodich);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        titletoolbar.setText("Giao Dịch");
        titletoolbar.setTextSize(30);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        daoHDCT = new Da0HDCT(GiaoDichActivity.this);
        hdctArrayList = new ArrayList<>();
        updateHDCTList();
        setupRecyclerView();

        txthetdonhang.setVisibility(View.GONE);

    }

    public void updateHDCTList() {
        daoHDCT = new Da0HDCT(GiaoDichActivity.this);
        daoHDCT.getAll(new HDCTCallback() {
            @Override
            public void onSuccess(ArrayList<HDCT> lists) {
                hdctArrayList.clear();
                for (int i = 0; i < lists.size(); i++) {
                    if (!lists.get(i).isCheck()) {
                        hdctArrayList.add(lists.get(i));
                    }
                }
                Collections.reverse(hdctArrayList);
                giaoDichAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
            }
        });
    }

    private void setupRecyclerView() {
        giaoDichAdapter = new GiaoDichAdapter(hdctArrayList, GiaoDichActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GiaoDichActivity.this,RecyclerView.VERTICAL,false);
        rcvgiaodich.setLayoutManager(linearLayoutManager);
        rcvgiaodich.setHasFixedSize(true);
        rcvgiaodich.setAdapter(giaoDichAdapter);
    }
}