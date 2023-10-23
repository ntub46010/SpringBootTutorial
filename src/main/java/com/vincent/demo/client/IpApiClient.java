package com.vincent.demo.client;

import com.vincent.demo.model.IpInfoResponse;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class IpApiClient {
    private final RestTemplate restTemplate;

    public IpApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public IpInfoResponse getIpInfo(String ipAddress) {
        return restTemplate.getForObject(
                "/{ip}/json",
                IpInfoResponse.class,
                Map.of("ip", ipAddress)
        );
    }
}
