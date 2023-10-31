package com.vincent.demo.service;

import com.vincent.demo.model.AppUserDetails;
import com.vincent.demo.model.LoginRequest;
import com.vincent.demo.model.LoginResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenService {
    private static final String KEY_USERNAME = "username";
    private final Key secretKey;
    private final JwtParser jwtParser;
    private final int accessTokenTtlSeconds;
    private final int refreshTokenTtlSeconds;
    private final AuthenticationProvider authenticationProvider;

    public TokenService(Key jwtSecretKey, JwtParser jwtParser,
                        int accessTokenTtlSeconds, int refreshTokenTtlSeconds,
                        AuthenticationProvider authenticationProvider) {
        this.secretKey = jwtSecretKey;
        this.jwtParser = jwtParser;
        this.accessTokenTtlSeconds = accessTokenTtlSeconds;
        this.refreshTokenTtlSeconds = refreshTokenTtlSeconds;
        this.authenticationProvider = authenticationProvider;
    }

    public LoginResponse createToken(LoginRequest request) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authToken = authenticationProvider.authenticate(authToken);
        AppUserDetails userDetails = (AppUserDetails) authToken.getPrincipal();

        String accessToken = createAccessToken(userDetails.getUsername());
        String refreshToken = createRefreshToken(userDetails.getUsername());

        LoginResponse res = new LoginResponse();
        res.setAccessToken(accessToken);
        res.setRefreshToken(refreshToken);
        res.setUserId(userDetails.getId());
        res.setEmail(userDetails.getUsername());
        res.setUserAuthority(userDetails.getUserAuthority());
        res.setPremium(userDetails.isPremium());
        res.setTrailExpiration(userDetails.getTrailExpiration());

        return res;
    }

    public String refreshAccessToken(String refreshToken) {
        Map<String, Object> payload = parseToken(refreshToken);
        String username = (String) payload.get(KEY_USERNAME);

        return createAccessToken(username);
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
        claims.put(KEY_USERNAME, username);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    private String createRefreshToken(String username) {
        long expirationMillis = Instant.now()
                .plusSeconds(refreshTokenTtlSeconds)
                .getEpochSecond()
                * 1000;

        Claims claims = Jwts.claims();
        claims.setSubject("Refresh Token");
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(expirationMillis));
        claims.put(KEY_USERNAME, username);

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