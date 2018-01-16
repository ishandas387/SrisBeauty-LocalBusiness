package com.srisbeauty.srisbeauty.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ishan on 01-11-2017.
 */

public class Review implements Serializable {

    String userName;
    float rating;
    String review;
    String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Review(String userName, float rating, String review, String userEmail, Date date) {
        this.userName = userName;
        this.rating = rating;
        this.review = review;
        this.userEmail = userEmail;
        this.date = date;
    }

    Date date;

    public Review() {
    }

    public String getUserName() {
        return userName;
    }

    public float getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public Date getDate() {
        return date;
    }

    public Review(String userName, float rating, String review, Date date) {
        this.userName = userName;
        this.rating = rating;
        this.review = review;
        this.date = date;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
