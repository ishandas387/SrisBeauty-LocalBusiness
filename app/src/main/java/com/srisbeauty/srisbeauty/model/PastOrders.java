package com.srisbeauty.srisbeauty.model;

/**
 * Created by ishan on 20-01-2018.
 */

public class PastOrders {
    String uId;

    public PastOrders(String uId, String pastOrders) {
        this.uId = uId;
        this.pastOrders = pastOrders;
    }

    String pastOrders;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getPastOrders() {
        return pastOrders;
    }

    public void setPastOrders(String pastOrders) {
        this.pastOrders = pastOrders;
    }
}
