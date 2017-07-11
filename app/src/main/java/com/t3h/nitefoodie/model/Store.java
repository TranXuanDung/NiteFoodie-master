package com.t3h.nitefoodie.model;

import java.util.HashMap;

public class Store {
    private String sId;
    public String name;
    private String address;
    private String phone;
    private double rate;
    private int numberRating;
    private int openTime;
    private int closeTime;
    private HashMap<String, Food> menu = new HashMap<>();
    private HashMap<String, String> listOrderIds = new HashMap<>();
    private String tag;
    private String photoUrl;

    public Store() {
    }


    public Store(String sId, String name, String address, String phone) {
        this.sId = sId;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }


    public String getTag() {
        return tag;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getNumberRating() {
        return numberRating;
    }

    public void setNumberRating(int numberRating) {
        this.numberRating = numberRating;
    }

    public int getOpenTime() {
        return openTime;
    }

    public void setOpenTime(int openTime) {
        this.openTime = openTime;
    }

    public int getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(int closeTime) {
        this.closeTime = closeTime;
    }

    public HashMap<String, Food> getMenu() {
        return menu;
    }

    public void setMenu(HashMap<String, Food> menu) {
        this.menu = menu;
    }

    public HashMap<String, String> getListOrderIds() {
        return listOrderIds;
    }

    public void setListOrderIds(HashMap<String, String> listOrders) {
        this.listOrderIds = listOrders;
    }
}
