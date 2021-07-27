package com.vincent.demo.entity.app_user;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class AppUserRequest {
    @Schema(description = "The email address of user.", example = "vincent@gmail.com")
    @NotBlank
    private String emailAddress;

    @Schema(description = "The password of user.", example = "123456", minLength = 6)
    @NotBlank
    private String password;

    @Schema(description = "The full name of user.", example = "Vincent Zheng")
    @NotBlank
    private String name;

    @Schema(description = "The authority of user.", required = true)
    @NotEmpty
    private List<UserAuthority> authorities;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<UserAuthority> authorities) {
        this.authorities = authorities;
    }
}
