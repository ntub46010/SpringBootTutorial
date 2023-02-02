package com.vincent.demo.model;

import com.vincent.demo.util.CommonUtil;

import java.util.Date;

public class Product {
    private String id;
    private String name;
    private Date createdTime;

    public static Product of(String id, String name, String createdTime) {
        var product = new Product();
        product.id = id;
        product.name = name;
        product.createdTime = CommonUtil.toDate(createdTime);
        return product;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreatedTime() {
        return createdTime;
    }
}
