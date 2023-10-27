package com.example.petshop.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.adapter.ProductAdapter;
import com.example.petshop.callback.ProductsCallback;
import com.example.petshop.dao.DaoProducts;
import com.example.petshop.model.Products;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SPchitietActivity extends AppCompatActivity {
    TextView tv_detail_rating,tv_detail_release_date,tv_detail_vote_count,txtsoluong,txtdiachi,txtmota,txtstatus,txtmatl;
    Toolbar toolbar;
    ImageView iv_backdrop,iv_detail_poster;
    int vohan=0;
    DaoProducts daoFood;
    ArrayList<Products> foodArrayList;
    RecyclerView rv_reviews;
    ProductAdapter foodAdapter;
    CollapsingToolbarLayout collapsingToolbarLayout;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spchitiet);

        iv_backdrop=findViewById(R.id.iv_backdrop);
        iv_detail_poster=findViewById(R.id.iv_detail_poster);
        tv_detail_rating=findViewById(R.id.tv_detail_rating);
        tv_detail_vote_count=findViewById(R.id.tv_detail_vote_count);
        tv_detail_release_date=findViewById(R.id.tv_detail_release_date);
        rv_reviews=findViewById(R.id.rv_reviews);
        txtsoluong=findViewById(R.id.txtsoluong);
        txtdiachi=findViewById(R.id.txtdiachi);
        txtmota=findViewById(R.id.txtmota);
        txtstatus=findViewById(R.id.txtstatus);
        txtmatl=findViewById(R.id.txtmatl);
        toolbar = findViewById(R.id.toolbar);

        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setStatusBarColor(ContextCompat.getColor(SPchitietActivity.this, R.color.colorPrimaryTransparent));

        Intent intent = getIntent();
        Picasso.get().load(intent.getStringExtra("img")).into(iv_backdrop);
        Picasso.get().load(intent.getStringExtra("img")).into(iv_detail_poster);
        collapsingToolbarLayout.setTitle(intent.getStringExtra("namefood"));
        tv_detail_rating.setText(intent.getStringExtra("gia"));
        tv_detail_release_date.setText(intent.getStringExtra("idfood"));
        txtdiachi.setText("Địa Chỉ:\t"+intent.getStringExtra("diachi"));
        txtsoluong.setText("Số Lượng:\t"+intent.getStringExtra("sl"));
        txtmatl.setText("Loại:\t"+intent.getStringExtra("matl"));
        txtstatus.setText("Trạng Thái:\t"+intent.getStringExtra("status"));
        txtmota.setText("Mô Tả:\t"+intent.getStringExtra("mota"));
        tv_detail_vote_count.setText("đăng bởi@"+intent.getStringExtra("idstore"));

        daoFood = new DaoProducts(SPchitietActivity.this);
        foodArrayList = new ArrayList<>();
        foodAdapter = new ProductAdapter(foodArrayList,SPchitietActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SPchitietActivity.this,LinearLayoutManager.HORIZONTAL,false);
        rv_reviews.setLayoutManager(linearLayoutManager);
        rv_reviews.setHasFixedSize(true);
        rv_reviews.setAdapter(foodAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("TenSharedPreferences", Context.MODE_PRIVATE);
        String giaTri = sharedPreferences.getString("NameStore", "GiaTriMacDinh");

        daoFood.getAll(new ProductsCallback() {
            @Override
            public void onSuccess(ArrayList<Products> lists) {
                foodArrayList.clear();
                for (int i =0;i<lists.size();i++){
                    if (lists.get(i).getIdStore().equalsIgnoreCase(giaTri)){
                        foodArrayList.add(lists.get(i));
                        foodAdapter.notifyDataSetChanged();
                    }
                }

            }
            @Override
            public void onError(String message) {

            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToCate();
            }
        });
    }

    private void backToCate() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fragment_to_show", "FragmentB");
        startActivity(intent);
        finish();
    }


    public void onDefaultToggleClick(View view) {
        Toast.makeText(this, "DefaultToggle", Toast.LENGTH_SHORT).show();
    }

    public void onCustomToggleClick(View view) {
        Toast.makeText(this, "CustomToggle", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}