package com.example.petshop.model;

public class Store {
    private String tokenStore;
    private String email;
    private String pass;
    private String name;
    private String phone;
    private String address;
    private String image;
    private String status;

    public Store() {
    }

    public Store(String tokenStore, String email, String pass, String name, String phone, String address, String image, String status) {
        this.tokenStore = tokenStore;
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.image = image;
        this.status = status;
    }

    public String getTokenStore() {
        return tokenStore;
    }

    public void setTokenStore(String tokenStore) {
        this.tokenStore = tokenStore;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
