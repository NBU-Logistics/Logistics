package com.nbu.logistics.controllers;

import javax.validation.Valid;

import com.nbu.logistics.entities.*;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String showRegister(Client client) {
        return "register";
    }

    @PostMapping("/register")
    public String registerClient(Model model, @Valid Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            this.authService.registerClient(client);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return "register";
        }
        
        model.addAttribute("success", "Successfully registered!");

        return "register";
    }

    @RequestMapping("/profile")
    public String showProfile() {
        return "profile";
    }
}