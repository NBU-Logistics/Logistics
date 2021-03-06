package com.nbu.logistics.controllers;

import com.nbu.logistics.config.MyUserPrincipal;
import com.nbu.logistics.entities.Delivery;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * The deliveries controller.
 */
@Controller
@RequestMapping("/deliveries")
public class DeliveriesController {
    @Autowired
    private DeliveriesService deliveriesService;

    @Autowired
    private AuthService authService;

    /**
     * Returns the page with the deliveries.
     * 
     * @param model the controller model
     * @return the deliveries page
     */
    private String getDeliveriesPage(Model model) {
        model.addAttribute("allDeliveries", this.deliveriesService.getAll());
        model.addAttribute("registeredDeliveries", this.deliveriesService.getRegistered());
        model.addAttribute("sentUndelivered", this.deliveriesService.getSentUndelivered());
        model.addAttribute("clientSentDelivered", this.deliveriesService.getClientSentDelivered());
        model.addAttribute("clientSentUndelivered", this.deliveriesService.getClientSentUndelivered());
        model.addAttribute("clientReceivedDelivered", this.deliveriesService.getClientReceivedDelivered());
        model.addAttribute("clientReceivedUndelivered", this.deliveriesService.getClientReceivedUndelivered());
        model.addAttribute("employeeRegisteredDeliveries", this.deliveriesService.getRegisteredByCurrentEmployee());

        return "deliveries";
    }

    /**
     * /deliveries/create get request handler
     * 
     * @param model the controller model
     * @return the create-delivery page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_CLIENT')")
    @GetMapping("/create")
    public String createDelivery(Model model) {
        model.addAttribute("delivery", new Delivery());

        return "create-delivery";
    }

    /**
     * /deliveries/create post request handler
     * 
     * @param model         the controller model
     * @param delivery      the delivery model
     * @param bindingResult the controller BindingResult
     * @return the create-delivery page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_CLIENT')")
    @PostMapping("/create")
    public String addDelivery(Model model, @Valid @ModelAttribute Delivery delivery, BindingResult bindingResult) {
        MyUserPrincipal loggedInUser = this.authService.getLoggedInUser();

        if (bindingResult.hasErrors()) {
            return "create-delivery";
        }

        try {
            this.deliveriesService.addDelivery(delivery, loggedInUser.getEmail());
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return "create-delivery";
        }

        return this.showAllDeliveries(model, delivery);
    }

    /**
     * /deliveries/delete post request handler
     * 
     * @param id    the delivery id
     * @param model the controller model
     * @return the deliveries page
     */
    @PreAuthorize("isAuthenticated() && (hasRole('ROLE_ADMIN') || hasRole('ROLE_OFFICE_EMPLOYEE'))")
    @PostMapping("/delete")
    public String deleteDelivery(@RequestParam("id") String id, Model model) {
        try {
            this.deliveriesService.deleteDelivery(id);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return this.getDeliveriesPage(model);
        }

        model.addAttribute("success", "Successfully deleted delivery!");

        return this.getDeliveriesPage(model);

    }

    /**
     * /deliveries/info post request handler
     * 
     * @param model the controller model
     * @param name  the delivery name
     * @return the edit-delivery page
     */
    @PreAuthorize("isAuthenticated() && (hasRole('ROLE_ADMIN') || hasRole('ROLE_OFFICE_EMPLOYEE') || hasRole('ROLE_COURIER'))")
    @PostMapping("/info")
    public String infoDelivery(Model model, @RequestParam("name") String name) {
        model.addAttribute("delivery", this.deliveriesService.findDelivery(name));

        return "edit-delivery";
    }

    /**
     * /deliveries/edit post request handler
     * 
     * @param model         the controller model
     * @param delivery      the delivery model
     * @param bindingResult the controller BindingResult
     * @return the edit-delivery-page
     */
    @PreAuthorize("isAuthenticated() && (hasRole('ROLE_ADMIN') || hasRole('ROLE_OFFICE_EMPLOYEE') || hasRole('ROLE_COURIER'))")
    @PostMapping("/edit")
    public String editDelivery(Model model, @ModelAttribute("delivery") @Valid Delivery delivery,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-delivery";
        }

        try {
            this.deliveriesService.editDelivery(delivery, delivery.getName());
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return "edit-delivery";
        }

        return this.getDeliveriesPage(model);
    }

    /**
     * /deliveries get request handler
     * 
     * @param model    the controller model
     * @param delivery the delivery model
     * @return the deliveries page
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public String showAllDeliveries(Model model, Delivery delivery) {
        return this.getDeliveriesPage(model);
    }
}