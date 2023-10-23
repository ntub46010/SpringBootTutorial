package com.vincent.demo.model;

public interface IpInfoClientResponse {
    String getCity();
    String getCurrency();
    Double getLatitude();
    Double getLongitude();
    String getUtcOffset();
    String getCallingCode();
    String getErrorReason();
}
