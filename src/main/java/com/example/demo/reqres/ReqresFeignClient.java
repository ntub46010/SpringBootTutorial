package com.example.demo.reqres;

import com.example.demo.bean.CreateUserRequest;
import com.example.demo.bean.CreateUserResponse;
import com.example.demo.bean.ListUserResponse;
import com.example.demo.bean.SingleUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "reqres-feign",
        url = "https://reqres.in/api",
        configuration = ReqresFeignConfig.class,
        fallback = ReqresFeignFallback.class)
public interface ReqresFeignClient {

    @GetMapping("/users/{id}")
    SingleUserResponse getUserById(@PathVariable("id") int userId);

    @GetMapping("/users")
    ListUserResponse getUsers(@RequestParam("page") int page, @RequestParam("per_page") int size);

    @PostMapping("/users")
    CreateUserResponse createUser(@RequestBody CreateUserRequest request);
}