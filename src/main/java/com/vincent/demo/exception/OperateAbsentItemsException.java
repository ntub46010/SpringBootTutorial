package com.vincent.demo.exception;

import java.util.List;

public class OperateAbsentItemsException extends RuntimeException {
    private final List<String> itemIds;

    public OperateAbsentItemsException(List<String> itemIds) {
        super("Following ids are non-existent: " + itemIds);
        this.itemIds = itemIds;
    }

    public List<String> getItemIds() {
        return itemIds;
    }
}
