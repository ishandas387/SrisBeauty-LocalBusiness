package com.srisbeauty.srisbeauty.model;

/**
 * Created by ishan on 12-11-2017.
 */

public class CartItems {

    private String productId;
    private String productName;
    private String price;

    public CartItems() {
    }

    private String serviceTime;


    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getPrice() {
        return price;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public CartItems(String productId, String productName, String price, String serviceTime) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.serviceTime = serviceTime;
    }
}
