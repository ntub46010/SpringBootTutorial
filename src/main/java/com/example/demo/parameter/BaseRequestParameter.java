package com.example.demo.parameter;

import io.swagger.v3.oas.annotations.media.Schema;

public class BaseRequestParameter {

    @Schema(description = "排序欄位", example = "price")
    private String sortField;

    @Schema(description = "遞增或遞減", example = "ASC")
    private SortDirection sortDirection;

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }
}
