package com.nbu.logistics.controllers;

import com.nbu.logistics.services.ClientsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientsController {
    @Autowired
    private ClientsService clientsService;

    @RequestMapping("/clients")
    public String showClients() {
        return "clients";
    }
}