package com.vincent.demo.model;

import java.util.List;

public class GetUserListResponse {
    private List<UserResponse> data;

    public List<UserResponse> getData() {
        return data;
    }

    public void setData(List<UserResponse> data) {
        this.data = data;
    }
}
