package com.vincent.demo.log;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueryHistoryService {
    private final List<QueryHistory> histories = new ArrayList<>();

    public void add(String type, String identifier) {
        histories.add(QueryHistory.of(type, identifier));
    }

    public boolean contains(String type, String identifier) {
        return histories.stream()
                .anyMatch(x -> x.getType().equals(type) && x.getIdentifier().equals(identifier));
    }

    public void deleteAll() {
        histories.clear();
    }
}
