package com.example.petshop.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
import java.util.HashMap;
import java.util.Locale;

public class GiaoDichAdapter extends RecyclerView.Adapter<GiaoDichAdapter.MyViewHolder> {
    ArrayList<HDCT> cartList;
    Context context;
    CartHDCTAdapter cartAdapter;
    FirebaseUser firebaseUser;
    ArrayList<Order> orderArrayList;
    private DaoStore daoStore;
    String tokkenstore = "";
    DatabaseReference databaseReference;

    public GiaoDichAdapter(ArrayList<HDCT> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_don_hang, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        orderArrayList = new ArrayList<>();
        daoStore = new DaoStore(context);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("HDCT");
        final HDCT cart = cartList.get(position);
        final String idHdct = cart.getIdHD();
        holder.txtxacnhan_row_don_hang.setText("Chưa Xác Nhận");
        holder.txtday_row_don_hang.setText(cart.getNgay());
        holder.txttime_row_don_hang.setText(cart.getThoigian());

        holder.cardview1_row_don_hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.dulieusach);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView txttongtien = (TextView) myDialog.findViewById(R.id.txttongtien);
                TextView txtnguoimua = myDialog.findViewById(R.id.txtnguoimua);
                final TextView txtnguoiban = myDialog.findViewById(R.id.txtnguoiban);
                final RecyclerView recyclerViewsp = myDialog.findViewById(R.id.recyclesanpham);
                cartAdapter = new CartHDCTAdapter(cart.getOrderArrayList(), context);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                recyclerViewsp.setLayoutManager(linearLayoutManager);
                recyclerViewsp.setAdapter(cartAdapter);
                daoStore.getAll(new StoreCallback() {
                    @Override
                    public void onSuccess(ArrayList<Store> lists) {
                        for (int i = 0; i < lists.size(); i++) {
                            if (lists.get(i).getTokenStore().matches(firebaseUser.getUid())) {
                                txtnguoiban.setText(lists.get(i).getEmail());
                            }
                        }
                    }

                    @Override
                    public void onError(String message) {

                    }
                });

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("HDCT");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderArrayList.clear();
                        double tongtien1 = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            HDCT hdct = dataSnapshot.getValue(HDCT.class);
                            if (hdct.getIdHDCT().equalsIgnoreCase(cart.getIdHDCT())) {
                                orderArrayList.addAll(hdct.getOrderArrayList());

                                for (Order order : orderArrayList) {
                                    tongtien1 += order.getSoLuong() * order.getProducts().getPrice();
                                    tokkenstore = order.getUser().getEmail();
                                }
                                txttongtien.setText("Tổng Tiền: \t" + decimalFormat.format(tongtien1) + "VNĐ");
                                txtnguoimua.setText(tokkenstore);
                                Log.d("thien", "onDataChange: " + tokkenstore);
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

        holder.btnxacnhan_row_don_hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            HDCT hdct = dataSnapshot.getValue(HDCT.class);
                            if (hdct.getIdHDCT().equalsIgnoreCase(idHdct)) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("check", true);
                                dataSnapshot.getRef().updateChildren(hashMap);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
        ;
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardview1_row_don_hang;
        TextView txtxacnhan_row_don_hang, txttime_row_don_hang, txtday_row_don_hang;
        Button btnxacnhan_row_don_hang, btnhuy_row_don_hang;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtxacnhan_row_don_hang = itemView.findViewById(R.id.txtxacnhan_row_don_hang);
            btnxacnhan_row_don_hang = itemView.findViewById(R.id.btnxacnhan_row_don_hang);
            btnhuy_row_don_hang = itemView.findViewById(R.id.btnhuy_row_don_hang);
            cardview1_row_don_hang = itemView.findViewById(R.id.cardview1_row_don_hang);
            txttime_row_don_hang = itemView.findViewById(R.id.txttime_row_don_hang);
            txtday_row_don_hang = itemView.findViewById(R.id.txtday_row_don_hang);
        }
    }
}
