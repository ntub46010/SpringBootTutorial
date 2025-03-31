package com.example.demo.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public class Product {

    @Null
    private String id;

    @Pattern(regexp = "^[A-Za-z0-9 ]*$")
    @NotBlank
    private String name;

    @Valid
    @NotNull
    private Price price;

    @Size(min = 1, max = 3)
    @NotNull
    private List<@NotBlank String> categories;

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

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
