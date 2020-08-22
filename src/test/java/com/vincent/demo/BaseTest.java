package com.vincent.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincent.demo.auth.AuthRequest;
import com.vincent.demo.entity.app_user.AppUser;
import com.vincent.demo.entity.app_user.UserAuthority;
import com.vincent.demo.repository.AppUserRepository;
import com.vincent.demo.repository.ProductRepository;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class BaseTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected AppUserRepository appUserRepository;

    protected HttpHeaders httpHeaders;
    protected final ObjectMapper mapper = new ObjectMapper();
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final String USER_PASSWORD = "123456";

    @Before
    public void initHttpHeader() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @Before
    public void initTestUser() throws Exception {
        for (TestUser user : TestUser.values()) {
            createTestUser(user.getEmailAddress(), user.getAuthorities());
        }
        login(TestUser.ANDY);
    }

    @After
    public void clearDB() {
        productRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    protected AppUser createTestUser(String emailAddress, List<UserAuthority> authorities) {
        if (!emailAddress.contains("@")) {
            throw new RuntimeException("Email address format is incorrect.");
        }

        String name = emailAddress.substring(0, emailAddress.indexOf("@"));
        AppUser user = new AppUser();
        user.setEmailAddress(emailAddress);
        user.setPassword(passwordEncoder.encode(USER_PASSWORD));
        user.setName(name);
        user.setAuthorities(authorities);

        return appUserRepository.insert(user);
    }

    protected void login(String emailAddress) throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername(emailAddress);
        request.setPassword(USER_PASSWORD);

        MvcResult result = mockMvc.perform(post("/auth")
                .headers(httpHeaders)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject responseBody = new JSONObject(result.getResponse().getContentAsString());
        String accessToken = responseBody.getString("token");
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    }

    protected void login(TestUser user) throws Exception {
        login(user.getEmailAddress());
    }

    protected void logout() {
        httpHeaders.remove(HttpHeaders.AUTHORIZATION);
    }
}
