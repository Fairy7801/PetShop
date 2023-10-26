package com.example.petshop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.activity.SPchitietActivity;
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
    String Tag;

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
                        Log.d("Error : ", e.getMessage());
                    }
                });


//        holder.title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        holder.cardView1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SPchitietActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("img", categories.getImage());
                intent.putExtra("gia", decimalFormat.format(categories.getPrice())+"\t VNĐ");
                intent.putExtra("namefood", categories.getNameP());
                intent.putExtra("idfood","Id: "+categories.getIdP());
                intent.putExtra("idstore",categories.getIdStore());
                intent.putExtra("diachi",categories.getAddress());
                intent.putExtra("sl",categories.getQuantity()+"");
                intent.putExtra("matl",categories.getId());
                intent.putExtra("status",categories.getStatus());
                intent.putExtra("mota",categories.getDescription());

                context.startActivity(intent);
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
        CardView cardView, cardView1, card_view4;
        LinearLayout line1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgfood);
            title = itemView.findViewById(R.id.txtnamefood);
            progressBar = itemView.findViewById(R.id.progressbar);
            txtdiachi = itemView.findViewById(R.id.txtdiachi);
            txtgia = itemView.findViewById(R.id.txtgia);
            line1 = itemView.findViewById(R.id.line1);
            cardView1 = itemView.findViewById(R.id.cardview1);
        }
    }
}
