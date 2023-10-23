package com.vincent.demo;

import com.vincent.demo.model.*;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApplicationTests {
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testGetForSingleData() {
        // 發送請求並取得回應
        ResponseEntity<GetUserResponse> resEntity = restTemplate.getForEntity(
                "https://reqres.in/api/users/1",
                GetUserResponse.class
        );

        // 確認 HTTP 狀態碼
        assertEquals(HttpStatus.OK, resEntity.getStatusCode());

        // 確認 response header
        assertNotNull(resEntity.getHeaders().getContentType());
        assertEquals("application/json;charset=utf-8", resEntity.getHeaders().getContentType().toString());

        // 確認 response body
        GetUserResponse resBody = resEntity.getBody();
        assertNotNull(resBody);

        UserResponse data = resBody.getData();
        assertEquals(1, data.getId());
        assertEquals("george.bluth@reqres.in", data.getEmail());
        assertEquals("George", data.getFirstName());
        assertEquals("Bluth", data.getLastName());
        assertEquals("https://reqres.in/img/faces/1-image.jpg", data.getAvatar());
    }

    @Test
    public void testPostData_PostForObject() {
        CreateUserRequest createReq = CreateUserRequest.of("morpheus", "leader");
        CreateUserResponse createRes = restTemplate.postForObject(
                "https://reqres.in/api/users",
                createReq,
                CreateUserResponse.class
        );

        assertNotNull(createRes);
        assertEquals(createReq.getName(), createRes.getName());
        assertEquals(createReq.getJob(), createRes.getJob());
        assertNotNull(createRes.getId());
        assertNotNull(createRes.getCreatedAt());
    }

    @Test
    public void testPostData_Exchange() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("your_access_token");

        CreateUserRequest createReq = CreateUserRequest.of("morpheus", "leader");
        HttpEntity<CreateUserRequest> httpEntity = new HttpEntity<>(createReq, headers);

        ResponseEntity<CreateUserResponse> resEntity = restTemplate.exchange(
                "https://reqres.in/api/users",
                HttpMethod.POST,
                httpEntity,
                CreateUserResponse.class
        );

        var createRes = resEntity.getBody();
        assertNotNull(createRes);
        assertEquals(createReq.getName(), createRes.getName());
        assertEquals(createReq.getJob(), createRes.getJob());
        assertNotNull(createRes.getId());
        assertNotNull(createRes.getCreatedAt());
    }

    @Test
    public void testGetForMultipleData() {
        GetUserListResponse res = restTemplate.getForObject(
                "https://reqres.in/api/users?page={page}&per_page={per_page}",
                GetUserListResponse.class,
                Map.of("page", 2, "per_page", 6)
        );
        assertNotNull(res);

        List<UserResponse> users = res.getData();
        assertEquals(6, users.size());
        assertEquals(7, users.get(0).getId());
        assertEquals(8, users.get(1).getId());
        assertEquals(9, users.get(2).getId());
        assertEquals(10, users.get(3).getId());
        assertEquals(11, users.get(4).getId());
        assertEquals(12, users.get(5).getId());
    }
}