package com.srisbeauty.srisbeauty.model;

import java.util.List;

/**
 * Created by ishan on 04-11-2017.
 */

public class Orders {

    String timeStamp;
    String orderId;
    String productId;
    String userName;
    String uid;
    List<OrderItem> products;
    String serviceTime;
    String total;
    int status;

    String address;
    String userToken;



    String email;
    public Orders(String timeStamp, String orderId, String productId, String userName, String uid, List<OrderItem> products, String serviceTime, String total, int status, String address, String userToken, String userPhoneNumber) {
        this.timeStamp = timeStamp;
        this.orderId = orderId;
        this.productId = productId;
        this.userName = userName;
        this.uid = uid;
        this.products = products;
        this.serviceTime = serviceTime;
        this.total = total;
        this.status = status;
        this.address = address;
        this.userToken = userToken;
        this.userPhoneNumber = userPhoneNumber;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Orders(String timeStamp, String orderId, String productId, String userName, String uid, List<OrderItem> products, String serviceTime, String total, int status, String address, String userPhoneNumber) {
        this.timeStamp = timeStamp;
        this.orderId = orderId;
        this.productId = productId;
        this.userName = userName;
        this.uid = uid;
        this.products = products;
        this.serviceTime = serviceTime;
        this.total = total;
        this.status = status;
        this.address = address;
        this.userPhoneNumber = userPhoneNumber;
    }

    public Orders(String timeStamp, String orderId, String productId, String userName, String uid, List<OrderItem> products, String serviceTime, String total, int status, String userPhoneNumber) {
        this.timeStamp = timeStamp;
        this.orderId = orderId;
        this.productId = productId;
        this.userName = userName;
        this.uid = uid;
        this.products = products;
        this.serviceTime = serviceTime;
        this.total = total;
        this.status = status;
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    String userPhoneNumber;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public String getUid() {
        return uid;
    }

    public List<OrderItem> getProducts() {
        return products;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setProducts(List<OrderItem> products) {
        this.products = products;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }


    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }



    public Orders() {
    }



    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProductId() {
        return productId;
    }
}
