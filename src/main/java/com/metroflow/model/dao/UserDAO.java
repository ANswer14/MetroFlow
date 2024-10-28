package com.metroflow.model.dao;

import com.metroflow.model.dto.User;
import com.metroflow.model.dto.UserRegisterForm;
import com.metroflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class UserDAO {

    private final UserRepository USERREPOSITORY;
    private final BCryptPasswordEncoder PASSWORDENCODER;

    // 회원가입 메소드
    public void register(UserRegisterForm user, String imagePath) {
        Set<String> set = new HashSet<>();
        set.add("user");
        // 엔티티 객체
        User registerUser = new User();
        registerUser.setUserId(user.getUserId());
        registerUser.setUserEmail(user.getUserEmail());
        registerUser.setUserRole(set);
        registerUser.setPassword(PASSWORDENCODER.encode(user.getPassword()));
        registerUser.setUserImgPath(imagePath);
        registerUser.setNickname(user.getNickname());
        USERREPOSITORY.save(registerUser);
    }

}