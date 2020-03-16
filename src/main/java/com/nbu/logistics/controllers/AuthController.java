package com.nbu.logistics.controllers;

import java.util.Arrays;

import javax.validation.Valid;

import com.nbu.logistics.entities.*;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    public interface RegisterAction {
        public void execute();
    }

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String showLogin(@RequestParam(required = false) String error, Model model, User user) {
        if (error != null) {
            model.addAttribute("error", "Wrong login information!");
        }

        return "login";
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "profile";
    }

    public String registerUser(Model model, @Valid User user, BindingResult bindingResult, String template,
            String successMessage, String role, RegisterAction registerAction) {
        if (bindingResult.hasErrors()) {
            return template;
        }

        try {
            this.authService.registerUser(user, Arrays.asList(role));
            registerAction.execute();
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return template;
        }

        model.addAttribute("success", successMessage);

        return template;
    }
}