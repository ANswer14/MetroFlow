package com.metroflow.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserForm {
    private String userId;
    private String password;
    private String nickname;
    private String userImgPath;
    private String userEmail;
    private Set<String> userRole;

    public UserForm(String userId, String nickname, Set<String> userRole) {
        this.userId = userId;
        this.nickname = nickname;
        this.userRole = userRole;
    }
}
