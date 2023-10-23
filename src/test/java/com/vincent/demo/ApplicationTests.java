package com.vincent.demo;

import com.vincent.demo.client.CurrencyLayerClient;
import com.vincent.demo.client.IpApiClient;
import com.vincent.demo.model.IpInfoResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private IpApiClient ipApiClient;

	@Autowired
	private CurrencyLayerClient currencyLayerClient;

	@Test
	public void testIpApiClient_Public() {
		IpInfoResponse ipInfo = ipApiClient.getIpInfo("208.67.222.222");

		assertFalse(ipInfo.isError());
		assertNull(ipInfo.getReason());
		assertFalse(ipInfo.isReserved());

		assertEquals("San Francisco", ipInfo.getCity());
		assertEquals("USD", ipInfo.getCurrency());
		assertEquals(-122.397966, ipInfo.getLongitude(), 0);
		assertEquals(37.774778, ipInfo.getLatitude(), 0);
		assertEquals("-0700", ipInfo.getUtcOffset());
		assertEquals("+1", ipInfo.getCountryCallingCode());
	}

	@Test
	public void testIpApiClient_Private() {
		IpInfoResponse ipInfo = ipApiClient.getIpInfo("192.168.8.100");

		assertTrue(ipInfo.isError());
		assertEquals("Reserved IP Address", ipInfo.getReason());
		assertTrue(ipInfo.isReserved());
	}

	@Test
	public void testCurrencyLayerClient() {
		var sourceCurrency = "USD";
		var targetCurrencies = List.of("TWD", "JPY", "CNY", "EUR");
		var exchangeRateRes = currencyLayerClient.getLiveExchangeRate(sourceCurrency, targetCurrencies);

		for (var target : targetCurrencies) {
			// USDTWD, USDJPY ...
			var pair = sourceCurrency + target;
			var rate = exchangeRateRes.getQuotes().get(pair);
			assertTrue(rate > 0);
		}
	}
}