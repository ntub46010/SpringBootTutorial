package com.vincent.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IpApiResponse implements IpInfoClientResponse {
    private boolean error;
    private String reason;
    private boolean reserved;

    private String city;
    private String currency;
    private Double latitude;
    private Double longitude;

    @JsonProperty("utc_offset")
    private String utcOffset;

    @JsonProperty("country_calling_code")
    private String countryCallingCode;

    public String getCity() {
        return city;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getUtcOffset() {
        return utcOffset;
    }

    public String getCallingCode() {
        return countryCallingCode;
    }

    @Override
    public String getErrorReason() {
        if (!error) {
            return null;
        } else if (reserved) {
            return "Reserved IP Address";
        } else {
            return reason;
        }
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setUtcOffset(String utcOffset) {
        this.utcOffset = utcOffset;
    }

    public void setCountryCallingCode(String countryCallingCode) {
        this.countryCallingCode = countryCallingCode;
    }
}
