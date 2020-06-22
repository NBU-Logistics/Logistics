package com.nbu.logistics.controllers;

import com.nbu.logistics.entities.User;
import com.nbu.logistics.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * The index controller.
 */
@Controller
public class IndexController {
    @Autowired
    private AuthService authService;

    /**
     * / get request handler
     * 
     * @param model the controller model
     * @param admin the admin model
     * @return either the register-admin or index page
     */
    @RequestMapping("/")
    public String showIndex(Model model, @ModelAttribute("user") User admin) {
        if (!this.authService.adminExists()) {
            return "register-admin";
        }

        return "index";
    }

    /**
     * /denied get request handler
     * 
     * @return the denied page
     */
    @GetMapping("/denied")
    public String showDeniedPage() {
        return "denied";
    }
}