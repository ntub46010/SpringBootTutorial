package com.vincent.demo.entity.product;

import io.swagger.v3.oas.annotations.Hidden;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ProductRequest {

    @NotEmpty(message = "Product name is undefined.")
    private String name;

    @NotNull
    @Min(value = 0, message = "Price should be greater or equal to 0.")
    private Integer price;

    @Hidden
    private boolean softDelete;

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

    public boolean isSoftDelete() {
        return softDelete;
    }

    public void setSoftDelete(boolean softDelete) {
        this.softDelete = softDelete;
    }
}
