package com.example.petshop.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

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
import com.example.petshop.model.HDCT;
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

import java.util.HashMap;
import java.util.Map;

public class GiaoDichActivity extends AppCompatActivity {
    TextView titletoolbar;
    Toolbar toolbar;
    TextView txtxacnhan,txttime,txtday,txthetdonhang;
    Button btnxacnhan,btnhuy;
    public static  String getidhdct ="";
    Intent intent;
    DatabaseReference databaseReference;
    boolean checkxacnhan = false;
    CardView cardview1;
    FirebaseUser user;
    String sented="";
    String emailstore ="";
    private RequestQueue requestQueue;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dich);
        titletoolbar = findViewById(R.id.toolbar_title);
        txtxacnhan = findViewById(R.id.txtxacnhan);
        txtday = findViewById(R.id.txtday);
        cardview1 = findViewById(R.id.cardview1);
        btnxacnhan = findViewById(R.id.btnxacnhan);
        txttime = findViewById(R.id.txttime);
        txthetdonhang = findViewById(R.id.txthetdonhang);
        btnhuy = findViewById(R.id.btnhuy);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        titletoolbar.setText("Giao Dịch");
        titletoolbar.setTextSize(30);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        intent = getIntent();
        btnhuy.setVisibility(View.VISIBLE);
        txthetdonhang.setVisibility(View.GONE);
        txttime.setVisibility(View.VISIBLE);
        btnxacnhan.setVisibility(View.VISIBLE);
        cardview1.setVisibility(View.VISIBLE);
        txtday.setVisibility(View.VISIBLE);
        txtxacnhan.setVisibility(View.VISIBLE);
        user = FirebaseAuth.getInstance().getCurrentUser();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        getidhdct = intent.getStringExtra("idhdct");
        sented  = intent.getStringExtra("sented");
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Store");
        mRef .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Store store = dataSnapshot.getValue(Store.class);
                    if (store.getTokenStore().equalsIgnoreCase(user.getUid())){
                        emailstore = store.getEmail();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("HDCT");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for ( DataSnapshot dataSnapshot : snapshot.getChildren()){
                    HDCT hdct = dataSnapshot.getValue(HDCT.class);
                    if (hdct.getIdHDCT().equalsIgnoreCase(getidhdct)){
                        checkxacnhan = hdct.isCheck();
                        Toast.makeText(GiaoDichActivity.this, "check"+checkxacnhan, Toast.LENGTH_SHORT).show();
                        txtday.setText(hdct.getNgay());
                        txttime.setText(hdct.getThoigian());
                    }
                }
                if (checkxacnhan == false){
                    txtxacnhan.setText("Đang Xác Nhận");
                }else {
                    txtxacnhan.setText("Đã Xác Nhận");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for ( DataSnapshot dataSnapshot : snapshot.getChildren()){
                            HDCT hdct = dataSnapshot.getValue(HDCT.class);
                            if (hdct.getIdHDCT().equalsIgnoreCase(getidhdct)){
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("check", true);
                                dataSnapshot.getRef().updateChildren(hashMap);
                                sendNotifiaction(sented,emailstore,"Đơn Hàng Đã Xác Nhận",
                                        user.getUid(),hdct.getIdHDCT());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                btnhuy.setVisibility(View.GONE);
                txthetdonhang.setVisibility(View.VISIBLE);
                txttime.setVisibility(View.GONE);
                btnxacnhan.setVisibility(View.GONE);
                cardview1.setVisibility(View.GONE);
                txtday.setVisibility(View.GONE);
                txtxacnhan.setVisibility(View.GONE);
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnhuy.setVisibility(View.GONE);
                txthetdonhang.setVisibility(View.VISIBLE);
                txttime.setVisibility(View.GONE);
                btnxacnhan.setVisibility(View.GONE);
                cardview1.setVisibility(View.GONE);
                txtday.setVisibility(View.GONE);
                txtxacnhan.setVisibility(View.GONE);
            }
        });
    }
    private void sendNotifiaction(String receiver, final String username, final String message, final String tokensStore, String listhdct){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Toast.makeText(GiaoDichActivity.this, "Đã gửi"+receiver, Toast.LENGTH_SHORT).show();
                    DataHoaDon data = new DataHoaDon(user.getUid(), R.mipmap.ic_launcher_round, username+": "+message, "Đơn Đặt Hàng",
                            tokensStore,listhdct);
                    SenderHoaDon sender = new SenderHoaDon(data,token.getToken());
                    try {
                        JSONObject senderJsonObj = new JSONObject(new Gson().toJson(sender));
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", senderJsonObj,
                                new com.android.volley.Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //response of the request
                                        Log.d("JSON_RESPONSE", "onResponse: " + response.toString());

                                    }
                                }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("JSON_RESPONSE", "onResponse: " + error.toString());
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                //put params
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Content-Type", "application/json");
                                headers.put("Authorization", "key=AAAA7eCn6jI:APA91bH79oi6N8HNy09qNO5JEFRaYgq5r4bxwRsopAOnVQ8__lbN03pSgCxfD00Y2a8QvubahIhvab_7CLSGVDtPtOee1x0GvcTd9S07uBouSjoBU6kL3_SI1RXr-6ogmBy8sfbQv111");


                                return headers;
                            }
                        };

                        //add this request to queue
                        requestQueue.add(jsonObjectRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}