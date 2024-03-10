package com.example.demo.model;

import java.time.Instant;

public class ProductPO {
    private String id;
    private String name;
    private int price;
    private String creatorId;
    private long createdTime;
    private long updatedTime;

    public static ProductPO of(String id, String name, int price, String creatorId) {
        var p = new ProductPO();
        p.id = id;
        p.name = name;
        p.price = price;
        p.creatorId = creatorId;
        p.createdTime = Instant.now().getEpochSecond();
        p.updatedTime = p.createdTime;

        return p;
    }

    public static ProductPO of(ProductRequest req) {
        var p = new ProductPO();
        p.name = req.getName();
        p.price = req.getPrice();
        p.creatorId = req.getCreatorId();

        return p;
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

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }
}
