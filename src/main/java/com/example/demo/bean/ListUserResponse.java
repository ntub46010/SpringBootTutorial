package com.example.demo.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListUserResponse {
    private int total;

    @JsonProperty("total_pages")
    private int totalPages;

    private List<UserResponse> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<UserResponse> getData() {
        return data;
    }

    public void setData(List<UserResponse> data) {
        this.data = data;
    }
}
