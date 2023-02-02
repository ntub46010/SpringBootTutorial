package com.vincent.demo.model;

import java.util.HashMap;
import java.util.Map;

public class ExceptionResponse {
    private Map<String, Object> info = new HashMap<>();

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }
}
