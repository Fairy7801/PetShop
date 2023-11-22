package com.example.petshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.model.Products;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    ArrayList<Products> categoryList;
    Context context;

    public ProductAdapter(ArrayList<Products> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemfood, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        Products categories = categoryList.get(position);
        holder.title.setText(categories.getNameP());
        holder.txtdiachi.setText(categories.getAddress());
        holder.txtgia.setText(String.valueOf(decimalFormat.format(categories.getPrice())+" VNĐ"));
        Picasso.get()
                .load(categories.getImage())
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, txtdiachi, txtgia;
        ProgressBar progressBar;
        CardView cardView1;
        LinearLayout line1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgfood);
            title = itemView.findViewById(R.id.txtnamefood);
            progressBar = itemView.findViewById(R.id.progressbar);
            txtdiachi = itemView.findViewById(R.id.txtdiachi);
            txtgia = itemView.findViewById(R.id.txtgia);
            line1 = itemView.findViewById(R.id.line1);
            cardView1 = itemView.findViewById(R.id.cardview);
        }
    }
}
