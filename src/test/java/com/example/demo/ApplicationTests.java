package com.example.demo;

import com.example.demo.bean.CreateUserRequest;
import com.example.demo.bean.CreateUserResponse;
import com.example.demo.bean.ListUserResponse;
import com.example.demo.bean.SingleUserResponse;
import com.example.demo.reqres.ReqresFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private ReqresFeignClient client;

    @Test
    void testGetUserById() {
        SingleUserResponse user = client.getUserById(2);
        printJSON(user);
    }

    @Test
    void testGetUserList() {
        ListUserResponse response = client.getUsers(1, 3);
        printJSON(response);
    }

    @Test
    void testCreateUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("morpheus");
        request.setJob("leader");

        CreateUserResponse response = client.createUser(request);
        printJSON(response);
    }

    private void printJSON(Object obj) {
        try {
            String json = om.writeValueAsString(obj);
            System.out.println(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
