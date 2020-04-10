package com.nbu.logistics.controllers;

import com.nbu.logistics.config.MyUserPrincipal;
import com.nbu.logistics.entities.Delivery;
import com.nbu.logistics.services.DeliveriesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class DeliveriesController {

    @Autowired
    private DeliveriesService deliveriesService;

    @GetMapping("deliveries/create")
    public String createDelivery(Model model) {
        model.addAttribute("delivery", new Delivery());
        return "create-delivery";
    }

    @PostMapping("/create")
    public String addDelivery(@ModelAttribute @Valid Delivery delivery, Model model, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "index";
        }
        deliveriesService.addDelivery(delivery);
        return "message-added-delivery";
    }

    @RequestMapping("delete/{id}")
    public String deleteDelivery(@PathVariable String id, Model model) {

        deliveriesService.deleteDelivery(id);
       // model.addAttribute("success", "Successfully deleted delivery!");
        return "redirect:/message-added-delivery";
    }

    @RequestMapping("/message-added-delivery")
    public String del( Model model) {


        // model.addAttribute("success", "Successfully deleted delivery!");
        return "message-added-delivery";
    }

    @RequestMapping("/deliveries")
    public String showAllDeliveries(Model model, Delivery delivery) {
        MyUserPrincipal loggedInUser = deliveriesService.getLoggedInUser();

        model.addAttribute("allDeliveries", deliveriesService.getAllDeliveries());
        model.addAttribute("clientSentDeliveries", deliveriesService.getSentDeliveries(loggedInUser));
        model.addAttribute("clientAwaitByDeliveries", deliveriesService.getAwaitByDeliveries(loggedInUser));
        model.addAttribute("clientAwaitToDeliveries", deliveriesService.getAwaitToDeliveries(loggedInUser));
        model.addAttribute("clientDeliveredDeliveries", deliveriesService.getDeliveredDeliveries(loggedInUser));
        return "deliveries";
    }



   /* @RequestMapping("/deliveries/client")
    public String showClientDeliveries(Model model, Delivery delivery)
    {
        model.addAttribute("clientDeliveries", deliveriesService.getClientDeliveries(deliveriesService.getLoggedInUser()));

        return "deliveries";
    }*/


}