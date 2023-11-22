package com.example.petshop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.adapter.AcceptAdapter;
import com.example.petshop.callback.HDCTCallback;
import com.example.petshop.dao.Da0HDCT;
import com.example.petshop.databinding.FragmentFailedTransactionBinding;
import com.example.petshop.model.HDCT;

import java.util.ArrayList;

public class FragmentFailedTransaction extends Fragment {
    private FragmentFailedTransactionBinding binding;
    private AcceptAdapter xacnhanAdapter;
    private Da0HDCT daoHDCT;
    private ArrayList<HDCT> arrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFailedTransactionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        daoHDCT = new Da0HDCT(requireActivity());
        arrayList=new ArrayList<>();
        initView();
        updateView();
        return view;
    }

    public void initView() {
        xacnhanAdapter = new AcceptAdapter(arrayList, getActivity());
        binding.rcvthanhcong.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));
        binding.rcvthanhcong.setHasFixedSize(true);
        binding.rcvthanhcong.setAdapter(xacnhanAdapter);
    }

    public void updateView() {
        daoHDCT.getAll(new HDCTCallback() {
            @Override
            public void onSuccess(ArrayList<HDCT> lists) {
                ArrayList<HDCT> filteredList = new ArrayList<>();
                for (int k = lists.size() - 1; k >= 0; k--) {
                    if (!lists.get(k).isCheck()) {
                        filteredList.add(lists.get(k));
                    }
                }
                requireActivity().runOnUiThread(() -> {
                    arrayList.clear();
                    arrayList.addAll(filteredList);
                    xacnhanAdapter.notifyDataSetChanged();
                });
            }
            @Override
            public void onError(String message) {
            }
        });
    }
}
