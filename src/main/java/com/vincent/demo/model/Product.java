package com.vincent.demo.model;

import java.util.Date;

public class Product {
    private String id;
    private String name;
    private int price;
    private Date createdTime;

    public Product() {
    }

    public Product(String id, String name, int price, Date createdTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.createdTime = createdTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
