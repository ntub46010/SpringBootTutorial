package com.vincent.demo.client;

import com.vincent.demo.model.ExchangeRateClientResponse;

import java.util.Collection;

public interface ExchangeRateClient {
    ExchangeRateClientResponse getLiveExchangeRate(String sourceCurrency, Collection<String> targetCurrencies);
}
