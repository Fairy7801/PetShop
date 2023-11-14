package com.example.petshop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
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

public class FragmentDetailProduct extends Fragment {
    String idP, nameStore, idCate;
    TextView tv_detail_rating,tv_detail_release_date,tv_detail_vote_count,txtsoluong,txtdiachi,txtmota;
    Toolbar toolbar;
    ImageView iv_backdrop,iv_detail_poster;
    ProgressBar progressviewcard, progressbackdrop;
    DaoProducts daoProducts;
    ArrayList<Products> productsArrayList;
    RecyclerView rv_reviews;
    ProductAdapter productAdapter;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idP = getArguments().getString("idP");
            idCate = getArguments().getString("idCate");
            nameStore = getArguments().getString("nameStore");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_product, container, false);
        iv_backdrop = view.findViewById(R.id.iv_backdrop);
        iv_detail_poster = view.findViewById(R.id.iv_detail_poster);
        progressbackdrop = view.findViewById(R.id.progressbackdrop);
        progressviewcard = view.findViewById(R.id.progressviewcard);
        tv_detail_rating = view.findViewById(R.id.tv_detail_rating);
        tv_detail_vote_count = view.findViewById(R.id.tv_detail_vote_count);
        tv_detail_release_date = view.findViewById(R.id.tv_detail_release_date);
        rv_reviews = view.findViewById(R.id.rv_reviews);
        txtsoluong = view.findViewById(R.id.txtsoluong);
        txtdiachi = view.findViewById(R.id.txtdiachi);
        txtmota = view.findViewById(R.id.txtmota);
        toolbar = view.findViewById(R.id.toolbar);

        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        collapsingToolbarLayout = view.findViewById(R.id.collapsing);
        daoProducts = new DaoProducts(getContext());
        daoProducts.getAll(new ProductsCallback() {
            @Override
            public void onSuccess(ArrayList<Products> lists) {
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).getIdP().equalsIgnoreCase(idP)) {
                        if (lists.get(i).getImage() == null) {
                            Picasso.get().load("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg").into(iv_backdrop);
                            progressbackdrop.setVisibility(View.GONE);
                            Picasso.get().load("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg").into(iv_detail_poster);
                            progressviewcard.setVisibility(View.GONE);
                        } else {
                            Picasso.get().load(lists.get(i).getImage()).into(iv_backdrop);
                            progressbackdrop.setVisibility(View.GONE);
                            Picasso.get().load(lists.get(i).getImage()).into(iv_detail_poster);
                            progressviewcard.setVisibility(View.GONE);
                        }
                        collapsingToolbarLayout.setTitle(lists.get(i).getNameP());
                        tv_detail_rating.setText("Đơn giá: " + String.valueOf(lists.get(i).getPrice()));
                        tv_detail_release_date.setText("Tên thể loại: " + lists.get(i).getId());
                        txtdiachi.setText("Địa chỉ: " + lists.get(i).getAddress());
                        txtmota.setText("Mô tả: " + lists.get(i).getDescription());
                        tv_detail_vote_count.setText("đăng bởi@ " + lists.get(i).getIdStore());
                        txtsoluong.setText("Số lượng: " + String.valueOf(lists.get(i).getQuantity()));
                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        });

        daoProducts = new DaoProducts(getContext());
        productsArrayList = new ArrayList<>();
        daoProducts.getAll(new ProductsCallback() {
            @Override
            public void onSuccess(ArrayList<Products> lists) {
                productsArrayList.clear();
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).getId().equals(idCate)){
                        productsArrayList.add(lists.get(i));
                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        });
        productAdapter = new ProductAdapter(productsArrayList,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rv_reviews.setLayoutManager(linearLayoutManager);
        rv_reviews.setHasFixedSize(true);
        rv_reviews.setAdapter(productAdapter);
        return view;
    }
}