package com.example.demo;

import com.example.demo.client.ReqresClient;
import com.example.demo.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTests {
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ReqresClient client = new ReqresClient();

    @Test
    public void testGetSingleUser() {
        // 定義 API endpoint
        Map<String, String> params = Map.of("id", "2");
        String urlTemplate = "https://reqres.in/api/users/{id}";

        // 準備 request header
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "reqres-free-v1");

        // 包裝 request body 與 header
        HttpEntity<Void> entity = new HttpEntity<>(null, headers);

        // 發送 request
        ResponseEntity<SingleUserResponse> responseEntity =
                restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, SingleUserResponse.class, params);

        // 運用 response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());

        SingleUserResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);

        UserResponse data = responseBody.getData();
        assertEquals(2, data.getId());
        assertEquals("janet.weaver@reqres.in", data.getEmail());
        assertEquals("Janet", data.getFirstName());
        assertEquals("Weaver", data.getLastName());
        assertEquals("https://reqres.in/img/faces/2-image.jpg", data.getAvatar());
    }

    @Test
    public void testCreateUser() {
        String url = "https://reqres.in/api/users";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "reqres-free-v1");

        CreateUserRequest request = new CreateUserRequest();
        request.setName("morpheus");
        request.setJob("leader");
        HttpEntity<CreateUserRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<CreateUserResponse> responseEntity =
                restTemplate.exchange(url, HttpMethod.POST, entity, CreateUserResponse.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        CreateUserResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals(request.getName(), responseBody.getName());
        assertEquals(request.getJob(), responseBody.getJob());
        assertNotNull(responseBody.getId());
        assertNotNull(responseBody.getCreatedAt());
    }

    @Test
    public void testGetManyUser() {
        Map<String, String> queryStrings = new HashMap<>();
        queryStrings.put("per_page", "3");
        queryStrings.put("page", "2");

        // 定義 URL 網域
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://reqres.in/api/users");

        // 填入 query string
        queryStrings.forEach(uriBuilder::queryParam);

        String url = uriBuilder.build().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "reqres-free-v1");

        HttpEntity<Void> entity = new HttpEntity<>(null, headers);

        ResponseEntity<ListUserResponse> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, entity, ListUserResponse.class, queryStrings);

        ListUserResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(12, responseBody.getTotal());
        assertEquals(4, responseBody.getTotalPages());

        List<UserResponse> users = responseBody.getData();
        assertEquals(3, users.size());

        UserResponse user1 = users.get(0);
        assertEquals(4, user1.getId());
        assertEquals("Eve", user1.getFirstName());

        UserResponse user2 = users.get(1);
        assertEquals(5, user2.getId());
        assertEquals("Charles", user2.getFirstName());

        UserResponse user3 = users.get(2);
        assertEquals(6, user3.getId());
        assertEquals("Tracey", user3.getFirstName());
    }

    @Test
    public void testGetSingleUserByClient() {
        Optional<UserResponse> userOp = client.getUserById(2);
        assertTrue(userOp.isPresent());

        UserResponse user = userOp.get();
        assertEquals(2, user.getId());
        assertEquals("janet.weaver@reqres.in", user.getEmail());
        assertEquals("Janet", user.getFirstName());
        assertEquals("Weaver", user.getLastName());
        assertEquals("https://reqres.in/img/faces/2-image.jpg", user.getAvatar());
    }

    @Test
    public void testCreateUserByClient() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("morpheus");
        request.setJob("leader");

        CreateUserResponse userRes = client.createUser(request);
        assertEquals(request.getName(), userRes.getName());
        assertEquals(request.getJob(), userRes.getJob());
        assertNotNull(userRes.getId());
        assertNotNull(userRes.getCreatedAt());
    }

    @Test
    public void testGetManyUserByClient() {
        Map<String, String> queryStrings = new HashMap<>();
        queryStrings.put("per_page", "3");
        queryStrings.put("page", "2");

        ListUserResponse response = client.getUsers(queryStrings);
        List<UserResponse> users = response.getData();
        assertEquals(3, users.size());

        UserResponse user1 = users.get(0);
        assertEquals(4, user1.getId());
        assertEquals("Eve", user1.getFirstName());

        UserResponse user2 = users.get(1);
        assertEquals(5, user2.getId());
        assertEquals("Charles", user2.getFirstName());

        UserResponse user3 = users.get(2);
        assertEquals(6, user3.getId());
        assertEquals("Tracey", user3.getFirstName());
    }
}
