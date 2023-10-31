package com.vincent.demo;

import com.vincent.demo.model.AppUser;
import com.vincent.demo.model.LoginRequest;
import com.vincent.demo.model.LoginResponse;
import com.vincent.demo.model.UserAuthority;
import com.vincent.demo.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class DemoController {

    @Autowired
    private UserIdentity userIdentity;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody AppUser user) {
        if (userIdentity.isPremium()) {
            user.setCreatorId(userIdentity.getId());
        }

        userRepository.insert(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        AppUser deletingUser = userRepository.findById(id);
        String myUserId = userIdentity.getId();
        UserAuthority myAuthority = userIdentity.getUserAuthority();

        if (id.equals(myUserId) ||
                myUserId.equals(deletingUser.getCreatorId()) ||
                myAuthority == UserAuthority.ADMIN) {

            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<AppUser> getUser(@PathVariable String id) {
        var user = userRepository.findById(id);
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse res = tokenService.createToken(request);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String accessToken = tokenService.refreshAccessToken(refreshToken);
        Map<String, String> res = Map.of("accessToken", accessToken);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/parse-token")
    public ResponseEntity<Map<String, Object>> parseToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        Map<String, Object> jwtPayload = tokenService.parseToken(authorization);
        return ResponseEntity.ok(jwtPayload);
    }
}