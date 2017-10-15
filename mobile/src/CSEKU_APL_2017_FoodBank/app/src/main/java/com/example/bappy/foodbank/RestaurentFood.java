package com.example.bappy.foodbank;

import java.io.Serializable;

/**
 * Created by hbapp on 9/28/2017.
 */

public class RestaurentFood implements Serializable{
    private String name,type,price,quantity;

    public RestaurentFood(String name, String type, String price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }
    public RestaurentFood(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }
}
