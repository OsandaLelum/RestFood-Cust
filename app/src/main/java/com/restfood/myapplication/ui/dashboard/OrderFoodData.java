package com.restfood.myapplication.ui.dashboard;

public class OrderFoodData {
    private String foodId;
    private String image;
    private String name;
    private int price;
    private int qty;
    private String shopId;

    public OrderFoodData(String foodId, String image, String name, int price, int qty, String shopId) {
        this.foodId = foodId;
        this.image = image;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.shopId = shopId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {

        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
