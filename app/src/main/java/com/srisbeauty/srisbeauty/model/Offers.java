package com.srisbeauty.srisbeauty.model;

import java.util.Date;

/**
 * Created by ishan on 19-12-2017.
 */

public class Offers {

    String offerId;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Offers() {
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public Offers(String offerId, String offerName, String offerDescription, String offerPrice, Date offerCreated) {

        this.offerId = offerId;
        this.offerName = offerName;
        this.offerDescription = offerDescription;
        this.offerPrice = offerPrice;
        this.offerCreated = offerCreated;
    }

    String offerName;
    String offerDescription;
    String offerPrice;
    Date offerCreated;

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public Date getOfferCreated() {
        return offerCreated;
    }

    public void setOfferCreated(Date offerCreated) {
        this.offerCreated = offerCreated;
    }

    public Offers(String offerName, String offerDescription, String offerPrice, Date offerCreated) {

        this.offerName = offerName;
        this.offerDescription = offerDescription;
        this.offerPrice = offerPrice;
        this.offerCreated = offerCreated;
    }
}
