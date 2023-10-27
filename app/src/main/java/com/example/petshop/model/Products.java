package com.example.petshop.model;

public class Products {
    private String nameP;
    private String idP;
    private double price;
    private int quantity;
    private String image;
    private String address;
    private String description;
    private String id;
    private String idStore;
    private String tokenStore;
    private String status;
    public Products(){

    }

    public Products(String nameP, String idP, double price, int quantity, String image, String address, String description, String id, String idStore, String tokenStore, String status) {
        this.nameP = nameP;
        this.idP = idP;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.address = address;
        this.description = description;
        this.id = id;
        this.idStore = idStore;
        this.tokenStore = tokenStore;
        this.status = status;
    }

    public String getNameP() {
        return nameP;
    }

    public void setNameP(String nameP) {
        this.nameP = nameP;
    }

    public String getIdP() {
        return idP;
    }

    public void setIdP(String idP) {
        this.idP = idP;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdStore() {
        return idStore;
    }

    public void setIdStore(String idStore) {
        this.idStore = idStore;
    }

    public String getTokenStore() {
        return tokenStore;
    }

    public void setTokenStore(String tokenStore) {
        this.tokenStore = tokenStore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Products{" +
                "nameP='" + nameP + '\'' +
                ", idP='" + idP + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", image='" + image + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", idStore='" + idStore + '\'' +
                ", tokenStore='" + tokenStore + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
