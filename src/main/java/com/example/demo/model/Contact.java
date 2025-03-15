package com.example.demo.model;

public class Contact {
    private String email;
    private String phone;

    public static Contact of(String email, String phone) {
        Contact c = new Contact();
        c.email = email;
        c.phone = phone;
        return c;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}