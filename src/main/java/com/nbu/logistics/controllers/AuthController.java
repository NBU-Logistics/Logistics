package com.nbu.logistics.controllers;

import com.nbu.logistics.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;

    @RequestMapping("/login")
    public String showLogin() {
        return "login";
    }

    @RequestMapping("/register")
    public String showRegister() {
        return "register";
    }

    @RequestMapping("/profile")
    public String showProfile() {
        return "profile";
    }
}