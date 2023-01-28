package com.vincent.demo.model;

import java.util.List;

public class DeleteByIdRequest {
    private List<String> ids = List.of();

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
