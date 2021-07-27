package com.vincent.demo.controller;

import com.vincent.demo.entity.app_user.AppUserRequest;
import com.vincent.demo.entity.app_user.AppUserResponse;
import com.vincent.demo.entity.app_user.UserAuthority;
import com.vincent.demo.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppUserController {

    @Autowired
    private AppUserService service;

    @PostMapping
    public ResponseEntity<AppUserResponse> createUser(@Valid @RequestBody AppUserRequest request) {
        AppUserResponse user = service.createUser(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(user);
    }

    @Operation(
            summary = "Query specific user.",
            description = "Query specific user info according to user id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get user info successfully"),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Only authenticated user can get user info.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The user doesn't exist.",
                            content = @Content)
            })
    @GetMapping("/{id}")
    public ResponseEntity<AppUserResponse> getUser(@PathVariable("id") String id) {
        AppUserResponse user = service.getUserResponseById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<AppUserResponse>> getUsers(
            @Parameter(description = "Define authorities that found user have at least one.", required = false, allowEmptyValue = true)
            @RequestParam(name = "authorities", required = false) List<UserAuthority> authorities) {
        List<AppUserResponse> users = service.getUserResponses(authorities);
        return ResponseEntity.ok(users);
    }
}
