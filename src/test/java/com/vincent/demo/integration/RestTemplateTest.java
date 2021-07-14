package com.vincent.demo.integration;

import com.vincent.demo.auth.AuthRequest;
import com.vincent.demo.entity.app_user.AppUser;
import com.vincent.demo.entity.app_user.UserAuthority;
import com.vincent.demo.entity.product.Product;
import com.vincent.demo.entity.product.ProductRequest;
import com.vincent.demo.entity.product.ProductResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
public class RestTemplateTest extends BaseTest {

    @LocalServerPort
    private int severPort;

    private String domain;
    private RestTemplate restTemplate;

    @Before
    public void init() {
        domain = "http://localhost:" + severPort;
        restTemplate = new RestTemplate();
    }

    @Test
    public void testGetProduct() {
        Product product = createProduct("Snack", 50);

        String url = domain + "/products/" + product.getId();
        ProductResponse productRes = restTemplate
                .getForObject(url, ProductResponse.class);

        Assert.assertNotNull(productRes);
        Assert.assertEquals(product.getId(), productRes.getId());
        Assert.assertEquals(product.getName(), productRes.getName());
        Assert.assertEquals(product.getPrice(), productRes.getPrice());
    }

    @Test
    public void testGetProducts() {
        Product p1 = createProduct("Operation Management", 350);
        Product p2 = createProduct("Marketing Management", 200);
        Product p3 = createProduct("Financial Statement Analysis", 400);
        Product p4 = createProduct("Human Resource Management", 420);
        Product p5 = createProduct("Enterprise Resource Planning", 440);

        String url = domain + "/products?keyword={name}&orderBy={orderField}&sortRule={sortDirection}";
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", "manage");
        queryParams.put("orderField", "price");
        queryParams.put("sortDirection", "asc");

        HttpEntity<Void> httpEntity = new HttpEntity<>(null, null);
        ResponseEntity<List<ProductResponse>> resEntity =
                restTemplate.exchange(url, HttpMethod.GET, httpEntity,
                        new ParameterizedTypeReference<List<ProductResponse>>() {}, queryParams);

        List<ProductResponse> productResList = resEntity.getBody();
        Assert.assertNotNull(productResList);
        Assert.assertEquals(3, productResList.size());
        Assert.assertEquals(p2.getId(), productResList.get(0).getId());
        Assert.assertEquals(p1.getId(), productResList.get(1).getId());
        Assert.assertEquals(p4.getId(), productResList.get(2).getId());
    }

    @Test
    public void testUserAuthentication() {
        AppUser appUser = createUser("Vincent", Collections.singletonList(UserAuthority.NORMAL));
        obtainAccessToken(appUser.getEmailAddress());
    }

    @Test
    public void testCreateProduct() {
        AppUser appUser = createUser("Vincent", Collections.singletonList(UserAuthority.NORMAL));

        ProductRequest productReq = new ProductRequest();
        productReq.setName("Snack");
        productReq.setPrice(50);

        String token = obtainAccessToken(appUser.getEmailAddress());
        httpHeaders.add(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<ProductRequest> httpEntity = new HttpEntity<>(productReq, httpHeaders);
        String url = domain + "/products";
        ProductResponse productRes = restTemplate
                .postForObject(url, httpEntity, ProductResponse.class);

        Assert.assertNotNull(productRes);
        Assert.assertEquals(productReq.getName(), productRes.getName());
        Assert.assertEquals(productReq.getPrice().intValue(), productRes.getPrice());
        Assert.assertEquals(appUser.getId(), productRes.getCreatorId());
    }

    @Test
    public void testReplaceProduct() {
        AppUser appUser = createUser("Vincent", Collections.singletonList(UserAuthority.NORMAL));
        Product product = createProduct("Snack", 50);

        ProductRequest productReq = new ProductRequest();
        productReq.setName("Fruit");
        productReq.setPrice(80);

        String token = obtainAccessToken(appUser.getEmailAddress());
        httpHeaders.add(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<ProductRequest> httpEntity = new HttpEntity<>(productReq, httpHeaders);
        String url = domain + "/products/" + product.getId();
        ResponseEntity<ProductResponse> resEntity = restTemplate
                .exchange(url, HttpMethod.PUT, httpEntity, ProductResponse.class);

        ProductResponse productRes = resEntity.getBody();
        Assert.assertNotNull(productRes);
        Assert.assertEquals(productReq.getName(), productRes.getName());
        Assert.assertEquals(productReq.getPrice().intValue(), productRes.getPrice());
    }

    @Test
    public void testExchangeRateAPI() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));

        restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .additionalMessageConverters(converter)
                .build();

        String url = "https://www.freeforexapi.com/api/live?pairs=USDTWD,USDEUR";
        HttpEntity<Void> httpEntity = new HttpEntity<>(null, null);
        ResponseEntity<ExchangeRateResponse> resEntity = restTemplate
                .exchange(url, HttpMethod.GET, httpEntity,
                        new ParameterizedTypeReference<ExchangeRateResponse>() {});

        ExchangeRateResponse exRateRes = resEntity.getBody();
        Assert.assertNotNull(exRateRes);
        Assert.assertNotNull(exRateRes.getRates().get("USDEUR"));
        Assert.assertNotNull(exRateRes.getRates().get("USDTWD"));
    }

    private Product createProduct(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return productRepository.insert(product);
    }

    private String obtainAccessToken(String username) {
        AuthRequest authReq = new AuthRequest();
        authReq.setUsername(username);
        authReq.setPassword(USER_PASSWORD);

        String url = domain + "/auth";
        Map tokenRes = restTemplate
                .postForObject(url, authReq, Map.class);

        Assert.assertNotNull(tokenRes);
        String token = (String) tokenRes.get("token");
        Assert.assertNotNull(token);

        return token;
    }

    public static class ExchangeRateResponse {
        private Map<String, RateData> rates;
        private int code;

        public Map<String, RateData> getRates() {
            return rates;
        }

        public void setRates(Map<String,RateData> rates) {
            this.rates = rates;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    public static class RateData {
        private double rate;
        private long timestamp;

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

}
