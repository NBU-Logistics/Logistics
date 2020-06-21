package com.nbu.logistics.services;

import com.nbu.logistics.entities.Client;
import com.nbu.logistics.entities.Delivery;
import com.nbu.logistics.entities.DeliveryStatus;
import com.nbu.logistics.entities.OfficeEmployee;
import com.nbu.logistics.entities.Settings;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.ClientsRepository;
import com.nbu.logistics.repositories.DeliveriesRepository;
import com.nbu.logistics.repositories.OfficeEmployeesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class DeliveriesService {
    @Autowired
    private DeliveriesRepository deliveriesRepository;

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private OfficeEmployeesRepository officeEmployeesRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private SettingsService settingsService;

    public Client findUser(String email) {
        return clientsRepository.findByUserEmail(email);
    }

    public void checkDeliverySender(String senderEmail, String recipientEmail) throws InvalidDataException {
        if (senderEmail.equals(recipientEmail)) {
            throw new InvalidDataException("You can not send deliveries to yourself!");
        }
    }

    public void checkDeliveryRecipient(String recipientEmail) throws InvalidDataException {
        if (this.findUser(recipientEmail) == null) {
            throw new InvalidDataException("This recipient is not a client of this company.");
        }
    }

    public void checkDeliveryName(Delivery delivery) throws InvalidDataException {
        boolean isPresent = deliveriesRepository.existsByName(delivery.getName());

        if (isPresent) {
            throw new InvalidDataException("This delivery name already exists. Please, enter a different one.");
        }
    }

    public void checkDeliveryWeight(double weight) throws InvalidDataException {
        if (weight <= 0) {
            throw new InvalidDataException("Please, enter valid weight.");
        }
    }

    @Transactional
    public void addDelivery(Delivery delivery, String senderEmail) throws InvalidDataException {
        String recipientEmail = delivery.getRecipient().getUser().getEmail();

        this.checkDeliveryName(delivery);
        this.checkDeliveryWeight(delivery.getWeight());
        this.checkDeliverySender(senderEmail, recipientEmail);
        this.checkDeliveryRecipient(recipientEmail);

        Client sender = this.findUser(senderEmail);
        Client recipient = this.findUser(recipientEmail);

        Settings settings = this.settingsService.getSettings();
        if (delivery.getWeight() > 0) {
            delivery.setPrice(delivery.getWeight() * settings.getPricePerKilogram());
        }

        if (delivery.isOfficeDelivery()) {
            double discountAmount = delivery.getPrice() * settings.getSentToOfficeDiscount() / 100.0;
            delivery.setPrice(delivery.getPrice() - discountAmount);
        }

        delivery.setSender(sender);
        delivery.setRecipient(recipient);
        delivery.setStatus(DeliveryStatus.POSTED);
        delivery.setCreatedOn(LocalDate.now());
        deliveriesRepository.save(delivery);

        sender.getSentDeliveries().add(delivery);
        recipient.getReceivedDeliveries().add(delivery);
        clientsRepository.save(sender);
        clientsRepository.save(recipient);
    }

    public Delivery findDelivery(String id) {
        return deliveriesRepository.findByName(id);
    }

    public void editDelivery(Delivery newDelivery, String id) throws InvalidDataException {
        Delivery delivery = this.deliveriesRepository.findByName(id);

        if (delivery == null) {
            throw new InvalidDataException("Delivery does not exist!");
        }

        if (delivery.getAddress() != null && !delivery.getAddress().isBlank()) {
            delivery.setAddress(newDelivery.getAddress());
        }

        Settings settings = this.settingsService.getSettings();
        if (delivery.getWeight() > 0) {
            delivery.setWeight(newDelivery.getWeight());
            delivery.setPrice(delivery.getWeight() * settings.getPricePerKilogram());
        }

        if (delivery.isOfficeDelivery()) {
            double discountAmount = delivery.getPrice() * settings.getSentToOfficeDiscount() / 100.0;
            delivery.setPrice(delivery.getPrice() - discountAmount);
        }

        if (delivery.getStatus() != null) {
            if (newDelivery.getStatus() == DeliveryStatus.REGISTERED) {
                if (this.authService.isInRole("ROLE_OFFICE_EMPLOYEE")) {
                    OfficeEmployee employee = this.officeEmployeesRepository
                            .findByUserEmail(this.authService.getLoggedInUser().getEmail());
                    if (!employee.getDeliveries().contains(delivery)) {
                        employee.getDeliveries().add(delivery);
                        this.officeEmployeesRepository.save(employee);
                    }
                }
            }

            delivery.setStatus(newDelivery.getStatus());
        }

        delivery.setOfficeDelivery(newDelivery.isOfficeDelivery());
        delivery.setCreatedOn(LocalDate.now());

        this.deliveriesRepository.save(delivery);
    }

    public void deleteDelivery(String name) throws InvalidDataException {
        Delivery delivery = this.deliveriesRepository.findByName(name);

        if (delivery == null) {
            throw new InvalidDataException("Delivery does not exist!");
        }

        delivery.setDeleted(true);
        this.deliveriesRepository.save(delivery);
    }

    public List<Delivery> getAll() {
        return deliveriesRepository.findAll();
    }

    public List<Delivery> getRegistered() {
        return deliveriesRepository.findByStatus(DeliveryStatus.REGISTERED);
    }

    public List<Delivery> getSentUndelivered() {
        return deliveriesRepository.findByStatusNot(DeliveryStatus.DELIVERED);
    }

    public List<Delivery> getClientSentDelivered() {
        if (!this.authService.isInRole("ROLE_CLIENT")) {
            return new ArrayList<Delivery>();
        }

        String email = this.authService.getLoggedInUser().getEmail();
        Client client = this.clientsRepository.findByUserEmail(email);
        if (client == null) {
            return new ArrayList<Delivery>();
        }

        List<Delivery> sentDelivered = client.getSentDeliveries().stream()
                .filter(delivery -> delivery.getStatus() == DeliveryStatus.DELIVERED).collect(Collectors.toList());

        return sentDelivered;
    }

    public List<Delivery> getClientSentUndelivered() {
        if (!this.authService.isInRole("ROLE_CLIENT")) {
            return new ArrayList<Delivery>();
        }

        String email = this.authService.getLoggedInUser().getEmail();
        Client client = this.clientsRepository.findByUserEmail(email);
        if (client == null) {
            return new ArrayList<Delivery>();
        }

        List<Delivery> sentUndelivered = client.getSentDeliveries().stream()
                .filter(delivery -> delivery.getStatus() != DeliveryStatus.DELIVERED && delivery.getStatus() != null)
                .collect(Collectors.toList());

        return sentUndelivered;
    }

    public List<Delivery> getClientReceivedDelivered() {
        if (!this.authService.isInRole("ROLE_CLIENT")) {
            return new ArrayList<Delivery>();
        }

        String email = this.authService.getLoggedInUser().getEmail();
        Client client = this.clientsRepository.findByUserEmail(email);
        if (client == null) {
            return new ArrayList<Delivery>();
        }

        List<Delivery> receivedDelivered = client.getReceivedDeliveries().stream()
                .filter(delivery -> delivery.getStatus() == DeliveryStatus.DELIVERED).collect(Collectors.toList());

        return receivedDelivered;
    }

    public List<Delivery> getClientReceivedUndelivered() {
        if (!this.authService.isInRole("ROLE_CLIENT")) {
            return new ArrayList<Delivery>();
        }

        String email = this.authService.getLoggedInUser().getEmail();
        Client client = this.clientsRepository.findByUserEmail(email);
        if (client == null) {
            return new ArrayList<Delivery>();
        }

        List<Delivery> receivedUndelivered = client.getReceivedDeliveries().stream()
                .filter(delivery -> delivery.getStatus() != DeliveryStatus.DELIVERED && delivery.getStatus() != null)
                .collect(Collectors.toList());

        return receivedUndelivered;
    }

    public List<Delivery> getRegisteredByCurrentEmployee() {
        if (!this.authService.isInRole("ROLE_OFFICE_EMPLOYEE")) {
            return new ArrayList<Delivery>();
        }

        String email = this.authService.getLoggedInUser().getEmail();
        OfficeEmployee employee = this.officeEmployeesRepository.findByUserEmail(email);
        if (employee == null) {
            return new ArrayList<Delivery>();
        }

        return employee.getDeliveries();
    }
}
