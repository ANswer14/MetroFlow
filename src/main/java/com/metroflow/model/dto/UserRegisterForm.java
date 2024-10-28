package com.metroflow.model.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterForm {

    @Id
    @NotEmpty(message = "아이디를 적어주세요.")
    @Size(max = 15, min = 3, message = "3자 이상 15자 이하로 적어주세요.")
    private String userId;

    @NotEmpty(message = "비밀번호를 적어주세요.")
    @Size(max = 20, min = 3, message = "3자 이상 20자 이하로 적어주세요.")
    private String password;

    @NotEmpty(message = "비밀번호 확인을 위해 적어주세요.")
    @Size(max = 20, min = 3, message = "3자 이상 20자 이하로 적어주세요.")
    private String passwordCheck;

    @NotEmpty(message = "닉네임을 적어주세요.")
    @Size(max = 10, message = "10자 이하로 적어주세요.")
    private String nickname;

    private String userImgPath;

    @Email
    @NotEmpty(message = "이메일을 적어주세요.")
    private String userEmail;

}
