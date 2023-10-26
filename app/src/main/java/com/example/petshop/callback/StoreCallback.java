package com.example.petshop.callback;

import com.example.petshop.model.Store;

import java.util.ArrayList;

public interface StoreCallback {
    void onSuccess(ArrayList<Store> lists);
    void onError(String message);
}