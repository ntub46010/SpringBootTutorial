package com.example.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Product {

    @Schema(description = "產品 id；建立資料時為必填，更新資料時會被忽略", example = "101")
    private String id;

    @Schema(description = "產品名稱", example = "Juice", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String name;

    @Schema(description = "價格", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(0)
    private int price;

    public static Product of(String id, String name, int price) {
        var p = new Product();
        p.id = id;
        p.name = name;
        p.price = price;

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
}
