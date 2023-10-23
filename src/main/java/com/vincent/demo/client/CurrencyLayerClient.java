package com.vincent.demo.client;

import com.vincent.demo.model.CurrencyLayerResponse;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Map;

public class CurrencyLayerClient {
    private final RestTemplate restTemplate;
    private final String accessKey;

    public CurrencyLayerClient(RestTemplate restTemplate, String accessKey) {
        this.restTemplate = restTemplate;
        this.accessKey = accessKey;
    }

    public CurrencyLayerResponse getLiveExchangeRate(String sourceCurrency, Collection<String> targetCurrencies) {
        var uriVariables = Map.of(
                "source", sourceCurrency,
                "currencies", String.join(",", targetCurrencies),
                "access_key", accessKey
        );

        return restTemplate.getForObject(
                "/live?format=1&source={source}&currencies={currencies}&access_key={access_key}",
                CurrencyLayerResponse.class,
                uriVariables
        );
    }
}
