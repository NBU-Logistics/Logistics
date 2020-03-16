package com.nbu.logistics.controllers;

import javax.validation.Valid;

import com.nbu.logistics.entities.User;
import com.nbu.logistics.services.ClientsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clients")
public class ClientsController {
    @Autowired
    private ClientsService clientsService;

    @Autowired
    private AuthController authController;

    @GetMapping("/")
    public String showClients() {
        return "clients";
    }

    @GetMapping("/register")
    public String showRegister(User user) {
        return "register";
    }

    @PostMapping("/register")
    public String registerClient(Model model, @Valid User user, BindingResult bindingResult) {
        return this.authController.registerUser(model, user, bindingResult, "register", "Successfully registered!",
                "CLIENT", () -> {
                    this.clientsService.createClient(user);
                });
    }
}