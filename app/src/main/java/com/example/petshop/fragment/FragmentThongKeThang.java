package com.example.petshop.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.petshop.R;
import com.example.petshop.dao.Da0HDCT;
import com.example.petshop.model.HDCT;
import com.example.petshop.model.Order;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;

public class FragmentThongKeThang extends Fragment {

    BarChart thongKeThang;
    ImageButton previous, next;
    TextView month;
    ArrayList<HDCT> arrayList;
    FirebaseUser firebaseUser;
    Da0HDCT daoHDCT;
    DatabaseReference databaseReference;
    Integer currentMonth;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_ke_thang, container, false);
        thongKeThang = view.findViewById(R.id.thongkengay);
        previous = view.findViewById(R.id.btn_previous);
        next = view.findViewById(R.id.btn_next);
        month = view.findViewById(R.id.tv_month);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        daoHDCT = new Da0HDCT(getActivity());
        arrayList = new ArrayList<>();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentMonth = Integer.parseInt(currentDate.substring(3, 5));
        month.setText(currentMonth.toString());
        databaseReference = FirebaseDatabase.getInstance().getReference("HDCT");
        loadData(currentMonth);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentMonth > 1) {
                    currentMonth--;
                    loadData(currentMonth);
                    month.setText(currentMonth.toString());
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentMonth < 12) {
                    currentMonth++;
                    loadData(currentMonth);
                    month.setText(currentMonth.toString());
                }
            }
        });
        return view;
    }

    public void loadData(Integer selectedMonth) {
        databaseReference.addValueEventListener(new ValueEventListener() {

            final String[] labelName = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
                    "25", "26", "27", "28", "29", "30", "31"};

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                ArrayList<Order> orderArrayList = new ArrayList<>();
                ArrayList<BarEntry> entryArrayList = new ArrayList<>();
                HashMap<String, ArrayList<Order>> mapOder = new HashMap<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    HDCT hdct = dataSnapshot.getValue(HDCT.class);
                    assert hdct != null;
                    if (Integer.parseInt(hdct.getNgay().substring(3, 5)) == selectedMonth)
                        arrayList.add(hdct);
                }
                for (int positionLabel = 0; positionLabel < labelName.length; positionLabel++) {
                    ArrayList<Order> listOrder = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        orderArrayList.clear();
                        String day = arrayList.get(i).getNgay().substring(0, 2);
                        if (arrayList.get(i).isCheck() && day.equals(labelName[positionLabel])) {
                            orderArrayList.addAll(arrayList.get(i).getOrderArrayList());
                            for (int j = 0; j < orderArrayList.size(); j++) {
                                if (orderArrayList.get(j).getProducts().getTokenStore().equalsIgnoreCase(firebaseUser.getUid())) {
                                    listOrder.add(orderArrayList.get(j));
                                }
                            }
                        }
                    }
                    mapOder.put(labelName[positionLabel], listOrder);
                }
                // Copy all data from hashMap into TreeMap
                TreeMap<String, ArrayList<Order>> sorted = new TreeMap<>(mapOder);
                for (String key : sorted.keySet()) {
                    int total = 0;
                    for (Order order : Objects.requireNonNull(sorted.get(key))) {
                        total += order.getSoLuong() * order.getProducts().getPrice();
                    }
                    entryArrayList.add(new BarEntry(Integer.parseInt(key), total));
                }
                BarDataSet barDataSet = new BarDataSet(entryArrayList, "Ngày");
                thongKeThang.notifyDataSetChanged();
                barDataSet.setColors(Color.CYAN);
                Description des = new Description();
                des.setText("");
                thongKeThang.setDescription(des);
                BarData barData = new BarData(barDataSet);
                thongKeThang.setData(barData);
                thongKeThang.setTouchEnabled(true);
                thongKeThang.resetViewPortOffsets();
                thongKeThang.setFitBars(true);
                thongKeThang.setVisibleXRange(0, 7);
                YAxis rightAxis = thongKeThang.getAxisRight();
                rightAxis.setEnabled(false);
                XAxis xAxis = thongKeThang.getXAxis();
                xAxis.setLabelCount(7);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawAxisLine(false);
                xAxis.setDrawGridLines(false);
                thongKeThang.setDrawBarShadow(false);
                thongKeThang.setDrawGridBackground(false);
                thongKeThang.setDrawValueAboveBar(false);
                thongKeThang.animateY(1000);
                thongKeThang.setDrawValueAboveBar(true);
                thongKeThang.invalidate();
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
