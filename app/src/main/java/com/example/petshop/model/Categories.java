package com.example.petshop.model;

public class Categories {
    private String id;
    private String name;
    private String moTa;
    private String image;
    private Boolean trangthai;
    private String token;
    public Categories (){
    }

    public Categories(String id, String name, String moTa, String image, Boolean trangthai, String token) {
        this.id = id;
        this.name = name;
        this.moTa = moTa;
        this.image = image;
        this.trangthai = trangthai;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(Boolean trangthai) {
        this.trangthai = trangthai;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return getName();
    }
}
