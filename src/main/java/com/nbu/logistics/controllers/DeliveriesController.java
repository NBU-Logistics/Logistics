package com.nbu.logistics.controllers;

import com.nbu.logistics.config.MyUserPrincipal;
import com.nbu.logistics.entities.Delivery;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.AuthService;
import com.nbu.logistics.services.DeliveriesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class DeliveriesController {

    @Autowired
    private DeliveriesService deliveriesService;

    @Autowired
    private AuthService authService;

    private void getDeliveriesPage(Model model) {
        model.addAttribute("allDeliveries", this.deliveriesService.getAll());
        model.addAttribute("registeredDeliveries", this.deliveriesService.getRegistered());
        model.addAttribute("sentUndelivered", this.deliveriesService.getSentUndelivered());
        model.addAttribute("clientSentDelivered", this.deliveriesService.getClientSentDelivered());
        model.addAttribute("clientSentUndelivered", this.deliveriesService.getClientSentUndelivered());
        model.addAttribute("clientReceivedDelivered", this.deliveriesService.getClientReceivedDelivered());
        model.addAttribute("clientReceivedUndelivered", this.deliveriesService.getClientReceivedUndelivered());
        model.addAttribute("employeeRegisteredDeliveries", this.deliveriesService.getRegisteredByCurrentEmployee());
    }

    @GetMapping("/deliveries/create")
    public String createDelivery(Model model) {
        model.addAttribute("delivery", new Delivery());

        return "create-delivery";
    }

    @PostMapping("/deliveries/create")
    public String addDelivery(Model model, @ModelAttribute @Valid Delivery delivery, BindingResult bindingResult) {
        MyUserPrincipal loggedInUser = this.authService.getLoggedInUser();

        if (bindingResult.hasErrors()) {
            return "index";
        }

        try {
            deliveriesService.addDelivery(delivery, loggedInUser.getEmail());
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return "create-delivery";
        }

        return showAllDeliveries(model, delivery);
    }

    @PostMapping("/deliveries/delete")
    public String deleteDelivery(@RequestParam("id") String id, Model model) {
        try {
            deliveriesService.deleteDelivery(id);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return "deliveries";
        }

        this.getDeliveriesPage(model);

        model.addAttribute("success", "Successfully deleted delivery!");

        return "deliveries";
    }

    @PostMapping("/deliveries/info")
    public String infoDelivery(@RequestParam("id") String id, Model model) {
        model.addAttribute("editDelivery", deliveriesService.findDelivery(id));

        return "edit-delivery";
    }

    @PostMapping("/deliveries/edit")
    public String editDelivery(Model model, @ModelAttribute @Valid Delivery delivery, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        try {
            deliveriesService.editDelivery(delivery, delivery.getName());
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return "index";
        }

        this.getDeliveriesPage(model);

        return "deliveries";
    }

    @RequestMapping("/deliveries")
    public String showAllDeliveries(Model model, Delivery delivery) {
        this.getDeliveriesPage(model);

        return "deliveries";
    }
}