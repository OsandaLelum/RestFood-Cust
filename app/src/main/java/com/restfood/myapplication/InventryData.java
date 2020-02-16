package com.restfood.myapplication;

public class InventryData {
    String name;
    double quantity;
    double price;
    double minQty;

    public InventryData(String name, double quantity, double price, double minQty) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.minQty = minQty;
    }

    public InventryData(String name, double quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public InventryData(double quantity, double price, double minQty) {
        this.quantity = quantity;
        this.price = price;
        this.minQty = minQty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMinQty() {
        return minQty;
    }

    public void setMinQty(double minQty) {
        this.minQty = minQty;
    }
}
