package com.vincent.demo;

import com.vincent.demo.service.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter authFilter
    ) throws Exception {
        http
                .authorizeHttpRequests(registry ->
                        registry
                                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/?*").hasAnyAuthority("ADMIN", "NORMAL")
                                .requestMatchers(HttpMethod.GET, "/users").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/refresh-token").permitAll()
                                .requestMatchers(HttpMethod.GET, "/parse-token").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            BCryptPasswordEncoder passwordEncoder
    ) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public TokenService tokenService(
            @Value("${security.jwt.key}") String key,
            @Value("${security.access-token-ttl-seconds}") int accessTokenTtlSeconds,
            @Value("${security.refresh-token-ttl-seconds}") int refreshTokenTtlSeconds,
            AuthenticationProvider authenticationProvider
    ) {
        var jwtSecretKey = Keys.hmacShaKeyFor(key.getBytes());
        var jwtParser = Jwts.parserBuilder().setSigningKey(jwtSecretKey).build();
        return new TokenService(
                jwtSecretKey, jwtParser,
                accessTokenTtlSeconds, refreshTokenTtlSeconds,
                authenticationProvider
        );
    }
}