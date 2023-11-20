package com.example.petshop.fragment;

import static com.example.petshop.activity.MainActivity.toolbar;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petshop.R;
import com.example.petshop.adapter.TabAdapter;
import com.google.android.material.tabs.TabLayout;

public class FragmentHistory extends Fragment {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView back;
    TextView titletoolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,container,false);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        tabLayout = view.findViewById(R.id.tabLayout);
        back =  view.findViewById(R.id.back);
        toolbar.setVisibility(View.GONE);
        titletoolbar =  view.findViewById(R.id.toolbar_title);
        viewPager =view.findViewById(R.id.viewPager);
        adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new FragmentSuccessfulTransaction(), "Giao Dịch Thành Công");
        adapter.addFragment(new FragmentFailedTransaction(), "Giao Dịch Chờ Xác Nhận");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        titletoolbar.setText("Lịch Sử Order");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fr_l, new FragmentProfile()).commit();
                toolbar.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
}
