package com.vincent.demo.controller;

import com.vincent.demo.exception.OperateAbsentItemsException;
import com.vincent.demo.model.DeleteByIdRequest;
import com.vincent.demo.model.User;
import com.vincent.demo.util.DateUtil;
import com.vincent.demo.util.ToSearchTextEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final Map<String, User> userDB = new LinkedHashMap<>();

    @PostConstruct
    private void initData() {
        var users = List.of(
                new User("U1", "Vincent Chang", "vincent@gmail.com"),
                new User("U2", "Ivy Chang", "ivy@gmail.com"),
                new User("U3", "Dora Pan", "dora@gmail.com")
        );
        users.forEach(x -> userDB.put(x.getId(), x));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUsers(@RequestBody DeleteByIdRequest request) {
        var itemIds = request.getIds();
        var absentIds = itemIds.stream()
                .filter(Predicate.not(userDB::containsKey))
                .collect(Collectors.toList());
        if (!absentIds.isEmpty()) {
            throw new OperateAbsentItemsException(absentIds);
        }

        itemIds.forEach(userDB::remove);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        var newName = Optional.ofNullable(name).orElse("");
        var newEmail = Optional.ofNullable(email).orElse("");

        var users = userDB.values()
                .stream()
                .filter(u -> u.getName().toLowerCase().contains(newName))
                .filter(u -> u.getEmail().equalsIgnoreCase(newEmail))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}
