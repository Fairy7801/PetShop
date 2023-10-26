package com.example.petshop.fragment;

import static com.example.petshop.activity.MainActivity.toolbar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.petshop.R;
import com.example.petshop.adapter.TabAdapter;
import com.google.android.material.tabs.TabLayout;

public class FragmentStatistical extends Fragment {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistical,container,false);
        toolbar.setVisibility(View.VISIBLE);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager =view.findViewById(R.id.viewPager);
        adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new FragmentThongKeNgay(), "Ngày");
        adapter.addFragment(new FragmentThongKeThang(), "Tháng");
        adapter.addFragment(new FragmentThongKeNam(), "Năm");
//        adapter.addFragment(new FragmentThongKe_TopSanPham(), "Top Sản Phẩm");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}