package com.example.demo.client;

import com.example.demo.model.*;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

public class ReqresClient {
    private static final String BASE_URL = "https://reqres.in/api";
    private final RestTemplate restTemplate;

    public ReqresClient() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(10));
        factory.setReadTimeout(Duration.ofSeconds(30));

        this.restTemplate = new RestTemplate(factory);

        this.restTemplate
                .getInterceptors()
                .add((request, body, execution) -> {
                    request.getHeaders().add("x-api-key", "reqres-free-v1");
                    return execution.execute(request, body);
                });
    }

    public Optional<UserResponse> getUserById(int id) {
        Map<String, String> params = Map.of("id", String.valueOf(id));
        String urlTemplate = BASE_URL + "/users/{id}";

        ResponseEntity<SingleUserResponse> responseEntity =
                restTemplate.exchange(urlTemplate, HttpMethod.GET, HttpEntity.EMPTY, SingleUserResponse.class, params);

        return Optional.ofNullable(responseEntity)
                .map(ResponseEntity::getBody)
                .map(SingleUserResponse::getData);
    }

    public CreateUserResponse createUser(CreateUserRequest request) {
        String url = BASE_URL + "/users";

        HttpEntity<CreateUserRequest> entity = new HttpEntity<>(request);

        ResponseEntity<CreateUserResponse> responseEntity =
                restTemplate.exchange(url, HttpMethod.POST, entity, CreateUserResponse.class);

        return responseEntity.getBody();
    }

    public ListUserResponse getUsers(Map<String, String> queryStrings) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(BASE_URL + "/users");
        queryStrings.forEach(uriBuilder::queryParam);
        String url = uriBuilder.build().toString();

        ResponseEntity<ListUserResponse> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, ListUserResponse.class, queryStrings);

        return responseEntity.getBody();
    }
}
