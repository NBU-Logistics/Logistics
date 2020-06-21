package com.nbu.logistics.controllers;

import com.nbu.logistics.entities.Client;
import com.nbu.logistics.services.ClientsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientsController {
    @Autowired
    private ClientsService clientsService;

    @PreAuthorize("isAuthenticated() && (hasRole('ROLE_ADMIN') || hasRole('ROLE_OFFICE_EMPLOYEE'))")
    @RequestMapping("/clients")
    public String showAllClients(Model model, Client client) {
        model.addAttribute("allClients", clientsService.getAllClients());

        return "clients";
    }
}