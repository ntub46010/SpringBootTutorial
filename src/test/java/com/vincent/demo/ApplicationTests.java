package com.vincent.demo;

import com.vincent.demo.client.CurrencyLayerClient;
import com.vincent.demo.client.IpInfoClient;
import com.vincent.demo.model.IpInfoClientResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    @Qualifier("ipApiClient")
    private IpInfoClient ipInfoClient;

    @Autowired
    private CurrencyLayerClient currencyLayerClient;

    @Test
    public void testIpApiClient_Public() {
        IpInfoClientResponse ipInfo = ipInfoClient.getIpInfo("208.67.222.222");

        assertNull(ipInfo.getErrorReason());

        assertEquals("San Francisco", ipInfo.getCity());
        assertEquals("USD", ipInfo.getCurrency());
        assertEquals(-122.397966, ipInfo.getLongitude(), 0);
        assertEquals(37.774778, ipInfo.getLatitude(), 0);
        assertEquals("-0700", ipInfo.getUtcOffset());
        assertEquals("+1", ipInfo.getCallingCode());
    }

    @Test
    public void testIpApiClient_Private() {
        IpInfoClientResponse ipInfo = ipInfoClient.getIpInfo("192.168.8.100");
        assertEquals("Reserved IP Address", ipInfo.getErrorReason());
    }

    @Test
    public void testCurrencyLayerClient() {
        var sourceCurrency = "USD";
        var targetCurrencies = List.of("TWD", "JPY", "CNY", "EUR");
        var exchangeRateRes = currencyLayerClient.getLiveExchangeRate(sourceCurrency, targetCurrencies);

        for (var target : targetCurrencies) {
            var pair = sourceCurrency + target;
            var rate = exchangeRateRes.getQuotes().get(pair);
            assertTrue(rate > 0);
        }
    }
}