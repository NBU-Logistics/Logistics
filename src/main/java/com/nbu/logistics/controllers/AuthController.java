package com.nbu.logistics.controllers;

import com.nbu.logistics.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;

    @RequestMapping("/login")
    public ModelAndView showLogin(ModelAndView modelAndView) {
        modelAndView.setViewName("login");

        return modelAndView;
    }

    @RequestMapping("/register")
    public String showRegister() {
        return "register";
    }
}