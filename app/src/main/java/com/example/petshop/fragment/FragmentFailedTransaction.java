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

public class FragmentFailedTransaction extends Fragment {
    AcceptAdapter xacnhanAdapter;
    Da0HDCT daoHDCT;
    RecyclerView rcvthanhcong;
    ArrayList<HDCT> arrayList;
    FirebaseUser firebaseUser;
    public static String uidstore1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_failed_transaction,container,false);
        rcvthanhcong= view.findViewById(R.id.rcvthanhcong);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rcvthanhcong.setLayoutManager(linearLayoutManager);
        daoHDCT = new Da0HDCT(getActivity());
        arrayList=new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        daoHDCT.getAll(new HDCTCallback() {
            @Override
            public void onSuccess(ArrayList<HDCT> lists) {
                String uidstore = "";
                arrayList.clear();
                ArrayList<Order> orderArrayList = new ArrayList<>();
                orderArrayList.clear();
                for (int i =0;i<lists.size();i++){
                    if ( lists.get(i).isCheck() == false) {

                        orderArrayList.addAll(lists.get(i).getOrderArrayList());
                    }

                }
                for (int k = 0; k < lists.size(); k++) {
                    if (uidstore.equalsIgnoreCase(firebaseUser.getUid()) && lists.get(k).isCheck() == false) {
                        arrayList.add(lists.get(k));
                        xacnhanAdapter = new AcceptAdapter(arrayList, getActivity());
                        rcvthanhcong.setAdapter(xacnhanAdapter);
                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        });
        return view;
    }
}
