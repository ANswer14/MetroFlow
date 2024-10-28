package com.metroflow.model.service;

import com.metroflow.model.dto.User;
import com.metroflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository USERREPOSITORY;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = USERREPOSITORY.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId()) // userId
                .password(user.getPassword()) // 비밀번호
                .authorities(user.getUserRole().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())) // 권한 받아옴(리스트 형태)
                .accountExpired(false) // 계정 만료 여부
                .accountLocked(false) // 계정 잠금 여부
                .credentialsExpired(false) // 자격 증명 만료 여부
                .disabled(false) // 비활성화 여부
                .build();
    }
}