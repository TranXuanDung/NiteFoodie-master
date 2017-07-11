package com.t3h.nitefoodie.model;

public class FoodOrder {
    private int numberOfFood;
    private String name;
    private String foodId;
    private long price;
    private String photoUrl;


    public FoodOrder() {

    }

    public int getNumberOfFood() {
        return numberOfFood;
    }

    public void setNumberOfFood(int numberOfFood) {
        this.numberOfFood = numberOfFood;
    }



    public FoodOrder(String foodId, String name, long price) {
        this.name = name;
        this.foodId = foodId;
        this.price = price;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
}
