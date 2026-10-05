package com.prepcv.pms.controller;

import com.prepcv.pms.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/protected")
    public String protectedEndpoint(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return "Authenticated successfully. User: "
                + user.getEmail()
                + ", Role: "
                + user.getRole().name();
    }
}