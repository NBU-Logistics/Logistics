package com.nbu.logistics.controllers;

import com.nbu.logistics.config.MyUserPrincipal;
import com.nbu.logistics.services.AuthService;
import com.nbu.logistics.services.OfficesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AboutController {

    @Autowired
    private AuthService authService;

    @Autowired
    private OfficesService officesService;

    @RequestMapping("/about")
    private String getOfficesPage(Model model) {

        model.addAttribute("allOffices", officesService.getAllOffices());

        return "about";
    }
}