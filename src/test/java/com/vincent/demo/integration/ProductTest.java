package com.vincent.demo.integration;

import com.vincent.demo.entity.app_user.AppUser;
import com.vincent.demo.entity.app_user.UserAuthority;
import com.vincent.demo.entity.product.Product;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class ProductTest extends BaseTest {

    @Test
    public void testCreateProduct() throws Exception {
        AppUser user = createUser("Vincent", Collections.singletonList(UserAuthority.NORMAL));
        login(user.getEmailAddress());

        JSONObject request = new JSONObject()
                .put("name", "Harry Potter")
                .put("price", 450);

        RequestBuilder requestBuilder =
                MockMvcRequestBuilders
                        .post("/products")
                        .headers(httpHeaders)
                        .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.price").value(request.getInt("price")))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void testGetProduct() throws Exception {
        Product product = createProduct("Economics", 450);
        productRepository.insert(product);

        mockMvc.perform(get("/products/" + product.getId())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    public void testReplaceProduct() throws Exception {
        AppUser user = createUser("Vincent", Collections.singletonList(UserAuthority.NORMAL));
        Product product = createProduct("Economics", 450);
        productRepository.insert(product);

        JSONObject request = new JSONObject()
                .put("name", "Macroeconomics")
                .put("price", 550);

        login(user.getEmailAddress());
        mockMvc.perform(put("/products/" + product.getId())
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.price").value(request.getInt("price")));
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteProduct() throws Exception {
        AppUser user = createUser("Vincent", Collections.singletonList(UserAuthority.NORMAL));
        Product product = createProduct("Economics", 450);
        productRepository.insert(product);

        login(user.getEmailAddress());
        mockMvc.perform(delete("/products/" + product.getId())
                .headers(httpHeaders))
                .andExpect(status().isNoContent());

        productRepository.findById(product.getId())
                .orElseThrow(RuntimeException::new);
    }

    @Test
    public void testSearchProductsSortByPriceAsc() throws Exception {
        Product p1 = createProduct("Operation Management", 350);
        Product p2 = createProduct("Marketing Management", 200);
        Product p3 = createProduct("Human Resource Management", 420);
        Product p4 = createProduct("Finance Management", 400);
        Product p5 = createProduct("Enterprise Resource Planning", 440);
        productRepository.insert(Arrays.asList(p1, p2, p3, p4, p5));

        MvcResult result = mockMvc.perform(get("/products")
                .headers(httpHeaders)
                .param("keyword", "Manage")
                .param("orderBy", "price")
                .param("sortRule", "asc"))
                .andReturn();

        MockHttpServletResponse mockHttpResponse = result.getResponse();
        String responseJSONStr = mockHttpResponse.getContentAsString();
        JSONArray productJSONArray = new JSONArray(responseJSONStr);

        List<String> productIds = new ArrayList<>();
        for (int i = 0; i < productJSONArray.length(); i++) {
            JSONObject productJSON = productJSONArray.getJSONObject(i);
            productIds.add(productJSON.getString("id"));
        }

        Assert.assertEquals(4, productIds.size());
        Assert.assertEquals(p2.getId(), productIds.get(0));
        Assert.assertEquals(p1.getId(), productIds.get(1));
        Assert.assertEquals(p4.getId(), productIds.get(2));
        Assert.assertEquals(p3.getId(), productIds.get(3));

        Assert.assertEquals(HttpStatus.OK.value(), mockHttpResponse.getStatus());
        Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE,
                mockHttpResponse.getHeader(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    public void get400WhenCreateProductWithEmptyName() throws Exception {
        AppUser user = createUser("Vincent", Collections.singletonList(UserAuthority.NORMAL));

        JSONObject request = new JSONObject()
                .put("name", "")
                .put("price", 350);

        login(user.getEmailAddress());
        mockMvc.perform(post("/products")
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get400WhenReplaceProductWithNegativePrice() throws Exception {
        AppUser user = createUser("Vincent", Collections.singletonList(UserAuthority.NORMAL));
        Product product = createProduct("Computer Science", 350);
        productRepository.insert(product);

        JSONObject request = new JSONObject()
                .put("name", "Computer Science")
                .put("price", -100);

        login(user.getEmailAddress());
        mockMvc.perform(put("/products/" + product.getId())
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isBadRequest());
    }

    private Product createProduct(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }
}
