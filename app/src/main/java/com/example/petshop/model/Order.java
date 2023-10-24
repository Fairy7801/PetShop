package com.example.petshop.model;

public class Order {
    private String idOrder;
    private int soLuong;
    Products products;
    Store store;
    User user;

    public Order() {
    }

    public Order(String idOrder, int soLuong, Products products, Store store, User user) {
        this.idOrder = idOrder;
        this.soLuong = soLuong;
        this.products = products;
        this.store = store;
        this.user = user;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
