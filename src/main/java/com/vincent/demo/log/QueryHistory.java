package com.vincent.demo.log;

public class QueryHistory {
    private String type;
    private String identifier;

    public static QueryHistory of(String type, String identifier) {
        var history = new QueryHistory();
        history.type = type;
        history.identifier = identifier;
        return history;
    }

    public String getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }
}
