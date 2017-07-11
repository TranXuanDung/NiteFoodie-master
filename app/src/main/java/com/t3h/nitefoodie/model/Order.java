package com.t3h.nitefoodie.model;

import java.util.HashMap;

public class Order {
    private String id;
    private String storeId;
    private String userId;
    private String state;
    private HashMap<String, FoodOrder> foodOrders = new HashMap<>();
    private long orderTime;

    public Order() {

    }

    public void setFoodOrders(HashMap<String, FoodOrder> foodOrders) {
        this.foodOrders = foodOrders;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }


    public HashMap<String, FoodOrder> getFoodOrders() {
        return foodOrders;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public long getTotalPrice() {
        long totalPrice = 0;
        for (FoodOrder foodOrder : foodOrders.values()) {
            totalPrice = totalPrice + foodOrder.getPrice() * foodOrder.getNumberOfFood();
        }
        return totalPrice;
    }
}
