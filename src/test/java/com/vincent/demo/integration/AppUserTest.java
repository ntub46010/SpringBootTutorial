package com.vincent.demo.integration;

import com.vincent.demo.entity.app_user.AppUser;
import com.vincent.demo.entity.app_user.AppUserRequest;
import com.vincent.demo.entity.app_user.UserAuthority;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class AppUserTest extends BaseTest {
    private final String URL_USER = "/users";

    @Test
    public void testCreateUser() throws Exception {
        logout();

        AppUserRequest request = new AppUserRequest();
        request.setEmailAddress("admin@gmail.com");
        request.setPassword("123456");
        request.setName("Admin");
        request.setAuthorities(Arrays.asList(UserAuthority.ADMIN, UserAuthority.NORMAL));

        MvcResult result = mockMvc.perform(post(URL_USER)
                .headers(httpHeaders)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        JSONObject responseBody = new JSONObject(result.getResponse().getContentAsString());
        String userId = responseBody.getString("id");

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(RuntimeException::new);
        Assert.assertEquals(request.getEmailAddress(), user.getEmailAddress());
        Assert.assertNotNull(user.getPassword());
        Assert.assertEquals(request.getName(), user.getName());
        Assert.assertArrayEquals(request.getAuthorities().toArray(), user.getAuthorities().toArray());
    }

    @Test
    public void testGetUser() throws Exception {
        AppUser user = createUser("Vincent", Collections.singletonList(UserAuthority.NORMAL));

        login(user.getEmailAddress());
        mockMvc.perform(get(URL_USER + "/" + user.getId())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.emailAddress").value(user.getEmailAddress()))
                .andExpect(jsonPath("$.password").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.authorities", hasSize(1)))
                .andExpect(jsonPath("$.authorities[0]").value(UserAuthority.NORMAL.name()));
    }

    @Test
    public void testGetUsers() throws Exception {
        AppUser adminUser = createUser("Vincent", Arrays.asList(UserAuthority.ADMIN, UserAuthority.NORMAL));
        createUser("Peggy", Collections.singletonList(UserAuthority.NORMAL));

        login(adminUser.getEmailAddress());
        mockMvc.perform(get(URL_USER)
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetUsersByAuthority() throws Exception {
        AppUser adminUser =
                createUser("Vincent", Collections.singletonList(UserAuthority.ADMIN));
        AppUser normalUser =
                createUser("Peggy", Collections.singletonList(UserAuthority.NORMAL));

        login(adminUser.getEmailAddress());
        mockMvc.perform(get(URL_USER)
                .param("authorities", UserAuthority.NORMAL.name())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(normalUser.getId()));

        mockMvc.perform(get(URL_USER)
                .param("authorities", UserAuthority.ADMIN.name() + "," + UserAuthority.NORMAL.name())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void test422WhenCreateUserWithExistingEmail() throws Exception {
        AppUser existingUser = createUser("Vincent", Collections.singletonList(UserAuthority.NORMAL));

        AppUserRequest request = new AppUserRequest();
        request.setEmailAddress(existingUser.getEmailAddress());
        request.setPassword("123456");
        request.setName("Admin");
        request.setAuthorities(Arrays.asList(UserAuthority.ADMIN, UserAuthority.NORMAL));

        mockMvc.perform(post(URL_USER)
                .headers(httpHeaders)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void test404WhenGetNonExistentUser() throws Exception {
        AppUser user = createUser("Vincent", Collections.singletonList(UserAuthority.NORMAL));

        login(user.getEmailAddress());
        mockMvc.perform(get(URL_USER + "/123")
                .headers(httpHeaders))
                .andExpect(status().isNotFound());
    }
}
