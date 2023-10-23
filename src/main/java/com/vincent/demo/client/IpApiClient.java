package com.vincent.demo.client;

import com.vincent.demo.model.IpApiResponse;
import com.vincent.demo.model.IpInfoClientResponse;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class IpApiClient implements IpInfoClient {
    private final RestTemplate restTemplate;

    public IpApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public IpInfoClientResponse getIpInfo(String ipAddress) {
        return restTemplate.getForObject(
                "/{ip}/json",
                IpApiResponse.class,
                Map.of("ip", ipAddress)
        );
    }
}
