package com.nbu.logistics.controllers;

import com.nbu.logistics.entities.User;
import com.nbu.logistics.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @Autowired
    private AuthService authService;

    @RequestMapping("/")
    public String showIndex(Model model, @ModelAttribute("user") User admin) {
        if (!this.authService.adminExists()) {
            return "register-admin";
        }

        return "index";
    }

    @GetMapping("/denied")
    public String showDeniedPage() {
        return "denied";
    }
}