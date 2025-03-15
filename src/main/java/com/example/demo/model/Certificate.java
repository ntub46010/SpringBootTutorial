package com.example.demo.model;

public class Certificate {
    private String type;
    private Integer score;
    private String level;

    public static Certificate of(String type, Integer score, String level) {
        Certificate c = new Certificate();
        c.type = type;
        c.score = score;
        c.level = level;
        return c;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}