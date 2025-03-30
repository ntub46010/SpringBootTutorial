package com.example.demo.model;

import com.example.demo.validation.UppercaseAlphabet;
import jakarta.validation.constraints.*;

public class Price {

    @Digits(integer = 6, fraction = 2, message = "最多整數6位，小數2位")
    @Min(0)
    @Max(10000)
    private double amount;

    @UppercaseAlphabet(minLength = 3, maxLength = 3, message = "需為3個大寫字母")
    private String currencyType;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
}
