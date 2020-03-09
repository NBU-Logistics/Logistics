package com.nbu.logistics.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AboutController {
    @RequestMapping("/about")
    public String showAbout() {
        return "about";
    }
}