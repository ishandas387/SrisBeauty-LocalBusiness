package com.srisbeauty.srisbeauty.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan on 01-11-2017.
 */

public class Product implements Serializable {

    String reference;
    String name;
    String description;
    String category;
    String imageUrl;

    float price;

    List<Review> reviewList;
    boolean havingSubCategories;

    public Product(String reference, String name, String description, String category, String imageUrl, float price, List<Review> reviewList, boolean havingSubCategories) {
        this.reference = reference;
        this.name = name;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.price = price;
        this.reviewList = reviewList;
        this.havingSubCategories = havingSubCategories;
    }

    public boolean isHavingSubCategories() {

        return havingSubCategories;
    }

    public void setHavingSubCategories(boolean havingSubCategories) {
        this.havingSubCategories = havingSubCategories;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {

        return imageUrl;
    }

    public Product(String id, String name, String description, String category, String imageUrl, float price, List<Review> reviewList) {
        this.reference = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.price = price;
        this.reviewList = reviewList;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public Product() {
    }

    public void setId(String id) {
        this.reference = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public String getId() {
        return reference;

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public List<Review> getReviewList() {
        if (null == reviewList)
        {
            reviewList = new ArrayList<>();
        }
        return reviewList;
    }


}
