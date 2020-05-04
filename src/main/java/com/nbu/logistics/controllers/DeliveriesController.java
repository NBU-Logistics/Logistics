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
        MyUserPrincipal loggedInUser = this.authService.getLoggedInUser();
        model.addAttribute("allDeliveries", deliveriesService.getAll());
        model.addAttribute("registeredDeliveries", deliveriesService.getRegistered());
        model.addAttribute("sentUndelivered", deliveriesService.getSentUndelivered());
        model.addAttribute("clientSentDelivered", deliveriesService.getSentDelivered(loggedInUser));
        model.addAttribute("clientSentUndelivered", deliveriesService.getSentUndelivered(loggedInUser));
        model.addAttribute("clientReceivedDelivered", deliveriesService.getReceivedDelivered(loggedInUser));
        model.addAttribute("clientReceivedUndelivered", deliveriesService.getReceivedUndelivered(loggedInUser));
    }

    @GetMapping("/deliveries/create")
    public String createDelivery(Model model) {
        model.addAttribute("delivery", new Delivery());

        return "create-delivery";
    }

    @PostMapping("/deliveries/create")
    public String addDelivery(Model model, @ModelAttribute @Valid Delivery delivery, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        try {
            deliveriesService.addDelivery(delivery);
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
        // this.getDeliveriesPage(model);
        model.addAttribute("editDelivery", deliveriesService.findDelivery(id));
        // model.addAttribute("success", "Successfully edited delivery!");
        // model.addAttribute("");
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