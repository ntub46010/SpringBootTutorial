package com.vincent.demo.controller;

import com.vincent.demo.exception.OperateAbsentItemsException;
import com.vincent.demo.model.DeleteByIdRequest;
import com.vincent.demo.model.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.vincent.demo.util.CommonUtil.toDate;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final Map<String, User> userDB = new LinkedHashMap<>();

    @PostConstruct
    private void initData() {
        var users = List.of(
                new User("U1", "Vincent", "vz0101", "vincent@gmail.com", toDate("1996-01-01")),
                new User("U2", "Ivy", "iv1231", "ivy@gmail.com", toDate("1994-12-31")),
                new User("U3", "Dora", "dr0715", "dora@gmail.com", toDate("1998-07-15"))
        );
        users.forEach(x -> userDB.put(x.getId(), x));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        var users = new ArrayList<>(userDB.values());
        return ResponseEntity.ok(users);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUsers(@RequestBody DeleteByIdRequest request) {
        var itemIds = request.getIds();
        var absentIds = itemIds.stream()
                .filter(Predicate.not(userDB::containsKey))
                .collect(Collectors.toList());
        if (!absentIds.isEmpty()) {
            throw new OperateAbsentItemsException();
        }

        itemIds.forEach(userDB::remove);
        return ResponseEntity.noContent().build();
    }
}
