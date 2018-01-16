package com.srisbeauty.srisbeauty.model;

/**
 * Created by ishan on 01-11-2017.
 */

public class OrderItem {


    String name;
    String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderItem() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    float price;

    public OrderItem(String name, String time, float price) {
        this.name = name;
        this.time = time;
        this.price = price;
    }
}

