package com.nbu.logistics.controllers;

import javax.validation.Valid;

import com.nbu.logistics.entities.*;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String showLogin(User user) {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(Model model, User user) {
        try {
            this.authService.loginUser(user);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());

            return "login";
        }

        model.addAttribute("success", "Successfully logged in!");

        return "login";
    }

    @GetMapping("/register")
    public String showRegister(User user) {
        return "register";
    }

    @PostMapping("/register")
    public String registerClient(Model model, @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            this.authService.registerUser(user);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return "register";
        }
        
        model.addAttribute("success", "Successfully registered!");

        return "register";
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "profile";
    }
}