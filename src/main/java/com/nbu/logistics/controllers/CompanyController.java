package com.nbu.logistics.controllers;

import com.nbu.logistics.services.CompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @RequestMapping("/company")
    public String showCompany() {
        return "company";
    }
}