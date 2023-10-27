package com.vincent.demo.model;

import java.util.Map;

public interface ExchangeRateClientResponse {
    long getTimestamp();

    String getSource();

    /**
     * <pre>
     *     {
     *         "USDJPY": 151.1169,
     *         "USDCNY": 7.3671,
     *         "USDCHF": 0.9047
     *     }
     * </pre>
     */
    Map<String, Double> getExchangeRateTable();

    default Double getRate(String currency) {
        var key = getSource() + currency;
        return getExchangeRateTable().get(key);
    }
}
