package com.example.demo.model.request;

import java.util.Set;

public class TakeCourseRequest {
    private Set<Long> courseIds;

    public Set<Long> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(Set<Long> courseIds) {
        this.courseIds = courseIds;
    }
}