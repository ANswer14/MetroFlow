package com.metroflow.controller;

import com.metroflow.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CongestionController {

    private final UserService USERSERVICE;

    @GetMapping("/goCongestion")
    public String congestion(Model model) {
        model.addAttribute("sessionUser", USERSERVICE.getUserObject());
        return "congestion";
    }
}
