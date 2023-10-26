package com.example.petshop.callback;

import com.example.petshop.model.HDCT;

import java.util.ArrayList;

public interface HDCTCallback {
    void onSuccess(ArrayList<HDCT> lists);
    void onError(String message);
}
