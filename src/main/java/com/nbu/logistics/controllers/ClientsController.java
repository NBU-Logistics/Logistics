package com.nbu.logistics.controllers;

import com.nbu.logistics.entities.Client;
import com.nbu.logistics.services.ClientsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientsController {
    @Autowired
    private ClientsService clientsService;

    private void getClientsPage(Model model) {
        model.addAttribute("allClients", clientsService.getAllClients());
    }

    @RequestMapping("/clients")
    public String showAllClients(Model model, Client client) {
        this.getClientsPage(model);

        return "clients";
    }
}