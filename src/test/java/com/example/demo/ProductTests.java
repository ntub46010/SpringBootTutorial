package com.example.demo;

import com.example.demo.model.Product;
import com.example.demo.repository.IProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductTests {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IProductRepository productRepository;

    private static int productCount = 0;

    @BeforeEach
    public void clearRepo() {
        productRepository.deleteAll();
    }

    @AfterAll
    public static void printProductCount() {
        System.out.println("Inserted product amount: " + productCount);
    }

    @Test
    void testGetOneProduct() throws Exception {
        Product product = insertProduct("Hamburger", 50);

        String apiPath = "/products/" + product.getId();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(apiPath);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    void testSortProductsByPrice() throws Exception {
        Product product1 = insertProduct("Hamburger", 50);
        Product product2 = insertProduct("Coke", 20);
        Product product3 = insertProduct("Sandwich", 40);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .param("sortField", "price")
                .param("sortDirection", "desc");

        mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(product1.getId()))
                .andExpect(jsonPath("$[1].id").value(product3.getId()))
                .andExpect(jsonPath("$[2].id").value(product2.getId()));
    }

    @Test
    void testGetManyProduct() throws Exception {
        Product product1 = insertProduct("Hamburger", 50);
        Product product2 = insertProduct("Coke", 20);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products");
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse httpResponse = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), httpResponse.getStatus());

        String responseBody = httpResponse.getContentAsString();
        List<Product> actualProducts = objectMapper.readValue(responseBody, new TypeReference<List<Product>>() {});
        List<String> actualIds = actualProducts.stream()
                .map(Product::getId)
                .toList();
        List<String> expectedIds = List.of(product1.getId(), product2.getId());

        assertTrue(actualIds.containsAll(expectedIds));
        assertTrue(expectedIds.containsAll(actualIds));
    }

    @Test
    void testCreateProduct() throws Exception {
        Product productRequest = new Product();
        productRequest.setName("Coke");
        productRequest.setPrice(20);
        String requestBody = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse httpResponse = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        String location = httpResponse.getHeader(HttpHeaders.LOCATION);
        assertNotNull(location);

        String productId = location.substring(location.lastIndexOf("/") + 1);
        Product resultProduct = productRepository.findById(productId);

        assertEquals(productRequest.getName(), resultProduct.getName());
        assertEquals(productRequest.getPrice(), resultProduct.getPrice());
    }

    private Product insertProduct(String name, int price) {
        var product = new Product();
        product.setName(name);
        product.setPrice(price);

        productCount++;
        return productRepository.insert(product);
    }
}
