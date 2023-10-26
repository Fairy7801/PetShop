package com.example.petshop.callback;

import com.example.petshop.model.Products;

import java.util.ArrayList;

public interface ProductsCallback {
    void onSuccess(ArrayList<Products> lists);
    void onError(String message);
}
