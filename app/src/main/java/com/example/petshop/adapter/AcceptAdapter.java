package com.example.petshop.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.callback.StoreCallback;
import com.example.petshop.dao.DaoStore;
import com.example.petshop.model.HDCT;
import com.example.petshop.model.Order;
import com.example.petshop.model.Store;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AcceptAdapter extends RecyclerView.Adapter<AcceptAdapter.MyViewHolder> {

    ArrayList<HDCT> cartList;
    Context context;
    CartHDCTAdapter cartAdapter;
    double tongtien=0;
    ArrayList<Order> orderArrayList;

    DaoStore databaseStore;
    FirebaseUser firebaseUser;
    String tokkenstore ="";
    public AcceptAdapter(ArrayList<HDCT> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_xacnhan, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        orderArrayList = new ArrayList<>();
        databaseStore = new DaoStore(context);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final HDCT cart = cartList.get(position);
        if(cart.isCheck()){
            holder.txtxacnhan.setText("Đã Xác Nhận");
        }else{
            holder.txtxacnhan.setText("Đang Xác Nhận");
        }
        holder.txtday.setText(cart.getNgay());
        holder.txttime.setText(cart.getThoigian());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.dulieusach);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView txttongtien = (TextView) myDialog.findViewById(R.id.txttongtien);
                TextView  txtnguoimua =  myDialog.findViewById(R.id.txtnguoimua);
                final TextView   txtnguoiban =  myDialog.findViewById(R.id.txtnguoiban);
                final RecyclerView recyclerViewsp =  myDialog.findViewById(R.id.recyclesanpham);
                cartAdapter = new CartHDCTAdapter(cart.getOrderArrayList(),context);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
                recyclerViewsp.setLayoutManager(linearLayoutManager);
                recyclerViewsp.setAdapter(cartAdapter);
                databaseStore.getAll(new StoreCallback() {
                    @Override
                    public void onSuccess(ArrayList<Store> lists) {
                        for (int i = 0;i<lists.size();i++){
                            if (lists.get(i).getTokenStore().matches(firebaseUser.getUid())){
                                txtnguoiban.setText(lists.get(i).getEmail());
                            }
                        }
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
                final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                decimalFormat.applyPattern("#,###,###,###");

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("HDCT");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderArrayList.clear();
                        double tongtien1 =0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            HDCT hdct = dataSnapshot.getValue(HDCT.class);
                            if (hdct.getIdHDCT().equalsIgnoreCase(cart.getIdHDCT())){
                                orderArrayList.addAll(hdct.getOrderArrayList());

                                for (Order order : orderArrayList){
                                    tongtien1 += order.getSoLuong() * order.getProducts().getPrice();
                                    tokkenstore = order.getUser().getEmail();
                                }
                                txttongtien.setText("Tổng Tiền: \t" + decimalFormat.format(tongtien1)+"VNĐ");
                                txtnguoimua.setText(tokkenstore);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                myDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        ProgressBar progressBar;
        CardView card_view;
        TextView txtxacnhan, txttime, txtday,txtnguoimua,txtnguoiban;
        TextView plus, minus;
        Button delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtxacnhan =  itemView.findViewById(R.id.txtxacnhan);
            txttime =  itemView.findViewById(R.id.txttime);
            txtday =  itemView.findViewById(R.id.txtday);

            card_view =  itemView.findViewById(R.id.card_view);
        }
    }
}
