package com.metroflow.config;

import com.metroflow.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final UserRepository USERREPOSITORY;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        HttpSession session = request.getSession();

        String username = request.getParameter("username"); // 사용자의 ID

        if (USERREPOSITORY.findByUserId(username).isEmpty()) {
            session.setAttribute("IDError", "해당 아이디는 존재하지 않습니다.");
            session.setAttribute("PWError", ""); // 에러 메세지 초기화
        } else {
            session.setAttribute("PWError", "부적절한 비밀번호 입니다.");
            session.setAttribute("IDError", ""); // 에러 메세지 초기화
        }

        response.sendRedirect("/login");
    }
}
