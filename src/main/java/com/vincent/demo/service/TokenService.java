package com.vincent.demo.service;

import com.vincent.demo.model.LoginRequest;
import com.vincent.demo.model.LoginResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenService {
    private final Key secretKey;
    private final JwtParser jwtParser;
    private final int accessTokenTtlSeconds;
    private final AuthenticationProvider authenticationProvider;

    public TokenService(Key jwtSecretKey, JwtParser jwtParser, int accessTokenTtlSeconds,
                        AuthenticationProvider authenticationProvider) {
        this.secretKey = jwtSecretKey;
        this.jwtParser = jwtParser;
        this.accessTokenTtlSeconds = accessTokenTtlSeconds;
        this.authenticationProvider = authenticationProvider;
    }

    public LoginResponse createToken(LoginRequest request) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authToken = authenticationProvider.authenticate(authToken);
        UserDetails userDetails = (UserDetails) authToken.getPrincipal();

        String accessToken = createAccessToken(userDetails.getUsername());

        LoginResponse res = new LoginResponse();
        res.setAccessToken(accessToken);

        return res;
    }

    private String createAccessToken(String username) {
        long expirationMillis = Instant.now()
                .plusSeconds(accessTokenTtlSeconds)
                .getEpochSecond()
                * 1000;

        Claims claims = Jwts.claims();
        claims.setSubject("Access Token");
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(expirationMillis));
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    public Map<String, Object> parseToken(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return new HashMap<>(claims);
    }
}