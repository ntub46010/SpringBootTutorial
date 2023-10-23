package com.vincent.demo.client;

import com.vincent.demo.model.IpInfoClientResponse;

public interface IpInfoClient {
    IpInfoClientResponse getIpInfo(String ipAddress);
}
