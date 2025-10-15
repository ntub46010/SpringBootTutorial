package com.example.demo.reqres;

import com.example.demo.bean.CreateUserRequest;
import com.example.demo.bean.CreateUserResponse;
import com.example.demo.bean.ListUserResponse;
import com.example.demo.bean.SingleUserResponse;
import org.springframework.stereotype.Component;

@Component
public class ReqresFeignFallback implements ReqresFeignClient {

    @Override
    public SingleUserResponse getUserById(int userId) {
        return new SingleUserResponse();
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        return new CreateUserResponse();
    }

    @Override
    public ListUserResponse getUsers(int page, int size) {
        return new ListUserResponse();
    }
}