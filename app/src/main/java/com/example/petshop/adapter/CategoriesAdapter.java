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
import com.example.petshop.fragment.FragmentManager;
import com.example.petshop.model.Categories;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {
    ArrayList<Categories> categoryList;
    Context context;
    private FragmentManager fragmentManager;

    public CategoriesAdapter(ArrayList<Categories> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categories, parent, false);
        return new CategoriesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Categories categories = categoryList.get(position);
        holder.title.setText(categories.getName());
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
//        holder.cardView1.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View v) {
//                FragmentProducts fragmentProducts = new FragmentProducts();
//                Bundle args = new Bundle();
//                args.putString("idfood", categories.getId());
//                args.putString("tokenstore", mAuth.getUid());
//                fragmentProducts.setArguments(args);
//
//            }
//        });

//        holder.cardView1.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, SPchitietActivity.class);
//                intent.putExtra("img", categories.getImage());
//                intent.putExtra("namefood", categories.getNameP());
//                intent.putExtra("idfood","Id: "+categories.getIdP());
//                intent.putExtra("idstore",categories.getIdStore());
//                intent.putExtra("diachi",categories.getAddress());
//                intent.putExtra("sl",categories.getQuantity()+"");
//                intent.putExtra("matl",categories.getId());
//                intent.putExtra("status",categories.getStatus());
//                intent.putExtra("mota",categories.getDescription());
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        ProgressBar progressBar;
        CardView  cardView1;
        LinearLayout line1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgCategory);
            title = itemView.findViewById(R.id.txtnameCategory);
            progressBar = itemView.findViewById(R.id.progressbarCategory);
            line1 = itemView.findViewById(R.id.lineCategory);
            cardView1 = itemView.findViewById(R.id.cardviewCategory);
        }
    }
}
