package com.example.petshop.callback;

import com.example.petshop.model.Categories;

import java.util.ArrayList;

public interface CategoriesCallback {
    void onSuccess(ArrayList<Categories> lists);
    void onError(String message);
}
