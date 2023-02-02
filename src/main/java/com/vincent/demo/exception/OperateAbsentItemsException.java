package com.vincent.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public class OperateAbsentItemsException extends RuntimeException {
    private final List<String> itemIds;

    public OperateAbsentItemsException(List<String> itemIds) {
        this.itemIds = itemIds;
    }

    public List<String> getItemIds() {
        return itemIds;
    }
}
