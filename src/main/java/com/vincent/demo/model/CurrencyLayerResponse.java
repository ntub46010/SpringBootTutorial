package com.vincent.demo.model;

import java.util.Map;

public class CurrencyLayerResponse implements ExchangeRateClientResponse {
    private long timestamp;
    private String source;
    private Map<String, Double> quotes;

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public Map<String, Double> getExchangeRateTable() {
        return quotes;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setQuotes(Map<String, Double> quotes) {
        this.quotes = quotes;
    }
}
