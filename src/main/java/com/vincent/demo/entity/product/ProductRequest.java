package com.vincent.demo.entity.product;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ProductRequest {

    @NotEmpty(message = "Product name is undefined.")
    private String name;

    @NotNull
    @Min(value = 0, message = "Price should be positive or 0.")
    private Integer price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
