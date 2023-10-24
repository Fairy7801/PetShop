package com.example.petshop.model;

public class User {
    private String email;
    private String pass;
    private String name;
    private String phone;
    private String birthday;
    private String sex;
    private String image;
    public User (){

    }

    public User(String email, String pass, String name, String phone, String birthday, String sex, String image) {
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.sex = sex;
        this.image = image;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
