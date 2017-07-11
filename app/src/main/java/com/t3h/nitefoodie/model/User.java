package com.t3h.nitefoodie.model;

import java.util.HashMap;

public class User {
    private String uid;
    private String email;
    private String name;
    private String photoUrl;
    private String phone;
    private String address;
    private HashMap<String, String> favoriteIds = new HashMap<>();
    public User() {
    }

    public String getPhone() {
        return phone;
    }

    public HashMap<String, String> getFavoriteIds() {
        return favoriteIds;
    }

    public void setFavoriteIds(HashMap<String, String> favoriteIds) {
        this.favoriteIds = favoriteIds;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
