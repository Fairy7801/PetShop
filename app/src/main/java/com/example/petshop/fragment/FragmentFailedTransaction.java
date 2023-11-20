package com.example.petshop.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.petshop.R;
import com.example.petshop.adapter.AcceptAdapter;
import com.example.petshop.callback.HDCTCallback;
import com.example.petshop.dao.Da0HDCT;
import com.example.petshop.model.HDCT;
import com.example.petshop.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentFailedTransaction extends Fragment {
    AcceptAdapter xacnhanAdapter;
    Da0HDCT daoHDCT;
    RecyclerView rcvthanhcong;
    ArrayList<HDCT> arrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_failed_transaction,container,false);
        rcvthanhcong = view.findViewById(R.id.rcvthanhcong);

        daoHDCT = new Da0HDCT(getActivity());
        arrayList=new ArrayList<>();

        initView();
        updateView();

        return view;
    }

    public void initView() {
        xacnhanAdapter = new AcceptAdapter(arrayList, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rcvthanhcong.setLayoutManager(linearLayoutManager);
        rcvthanhcong.setHasFixedSize(true);
        rcvthanhcong.setAdapter(xacnhanAdapter);
    }
    public void updateView() {
        daoHDCT = new Da0HDCT(getActivity());
        daoHDCT.getAll(new HDCTCallback() {
            @Override
            public void onSuccess(ArrayList<HDCT> lists) {
                arrayList.clear();
                for (int k = 0; k < lists.size(); k++) {
                    if (!lists.get(k).isCheck()) {
                        arrayList.add(lists.get(k));
                    }
                }
                Collections.reverse(arrayList);
                xacnhanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {

            }
        });
    }
}
