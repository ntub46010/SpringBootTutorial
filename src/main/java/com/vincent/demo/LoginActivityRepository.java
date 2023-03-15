package com.vincent.demo;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class LoginActivityRepository {
    private final List<LoginActivity> activities = new ArrayList<>();

    public void insert(String name) {
        var activity = LoginActivity.of(name, new Date());
        activities.add(activity);
    }

    public List<LoginActivity> findByNotNotified() {
        return activities.stream()
                .filter(Predicate.not(LoginActivity::isNotified))
                .collect(Collectors.toList());
    }
}
