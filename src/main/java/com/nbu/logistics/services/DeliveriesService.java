package com.nbu.logistics.services;

import com.nbu.logistics.entities.*;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

/**
 * This is the deliveries service.
 */
@Service
public class DeliveriesService {
    @Autowired
    private DeliveriesRepository deliveriesRepository;

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private CouriersRepository couriersRepository;

    @Autowired
    private OfficeEmployeesRepository officeEmployeesRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private SettingsService settingsService;

    /**
     * Finds a client by given e-mail.
     * 
     * @param email the e-mail
     * @return the client we have found
     */
    public Client findUser(String email) {
        return clientsRepository.findByUserEmail(email);
    }

    /**
     * Verifies if the sender and recipient e-mails belong to the same person.
     * 
     * @param senderEmail    the sender's e-mail
     * @param recipientEmail the recipient's e-mail
     * @throws InvalidDataException when the sender and recipient's e-mails are the
     *                              same
     */
    public void checkDeliverySender(String senderEmail, String recipientEmail) throws InvalidDataException {
        if (senderEmail.equals(recipientEmail)) {
            throw new InvalidDataException("You can not send deliveries to yourself!");
        }
    }

    /**
     * Verifies if a given recipient is a client.
     * 
     * @param recipientEmail the given e-mail
     * @throws InvalidDataException when the given e-mail does not belong to a
     *                              client
     */
    public void checkDeliveryRecipient(String recipientEmail) throws InvalidDataException {
        if (this.findUser(recipientEmail) == null) {
            throw new InvalidDataException("This recipient is not a client of this company.");
        }
    }

    /**
     * Verifies if a delivery with the given one's name already exists.
     * 
     * @param delivery the delivery entity
     * @throws InvalidDataException when a delivery with the provided one's email
     *                              exists already
     */
    public void checkDeliveryName(Delivery delivery) throws InvalidDataException {
        boolean isPresent = deliveriesRepository.existsByName(delivery.getName());

        if (isPresent) {
            throw new InvalidDataException("This delivery name already exists. Please, enter a different one.");
        }
    }

    /**
     * Verifies that given weight is not less than 0
     * 
     * @param weight the weight amount
     * @throws InvalidDataException when the weight is < 0
     */
    public void checkDeliveryWeight(double weight) throws InvalidDataException {
        if (weight <= 0) {
            throw new InvalidDataException("Please, enter valid weight.");
        }
    }

    /**
     * Creates a delivery.
     * 
     * @param delivery    the delivery entity
     * @param senderEmail the sender's e-mail
     * @throws InvalidDataException when validation fails
     */
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

    /**
     * Finds a delivery by given id.
     * 
     * @param id the id
     * @return the delivery with the given id
     */
    public Delivery findDelivery(String id) {
        return deliveriesRepository.findByName(id);
    }

    /**
     * Changes the details of a given delivery
     * 
     * @param newDelivery the new delivery's data
     * @param id          the existing delivery's id
     * @throws InvalidDataException when the delivery does not exist
     */
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
            } else if (newDelivery.getStatus() == DeliveryStatus.IN_COURIER) {
                if (this.authService.isInRole("ROLE_COURIER")) {
                    Courier courier = this.couriersRepository
                            .findByUserEmail(this.authService.getLoggedInUser().getEmail());
                    if (!courier.getDeliveries().contains(delivery)) {
                        courier.getDeliveries().add(delivery);
                        this.couriersRepository.save(courier);
                    }
                }
            }

            delivery.setStatus(newDelivery.getStatus());
        }

        delivery.setOfficeDelivery(newDelivery.isOfficeDelivery());
        delivery.setCreatedOn(LocalDate.now());

        this.deliveriesRepository.save(delivery);
    }

    /**
     * Deletes a delivery by it's name.
     * 
     * @param name the delivery's name
     * @throws InvalidDataException when the delivery does not exist
     */
    public void deleteDelivery(String name) throws InvalidDataException {
        Delivery delivery = this.deliveriesRepository.findByName(name);

        if (delivery == null) {
            throw new InvalidDataException("Delivery does not exist!");
        }

        delivery.setDeleted(true);
        this.deliveriesRepository.save(delivery);
    }

    /**
     * Gets all deliveries from the database.
     * 
     * @return a list of all deliveries
     */
    public List<Delivery> getAll() {
        return deliveriesRepository.findAll();
    }

    /**
     * Gets all registered deliveries from the database.
     * 
     * @return a list of all registered deliveries
     */
    public List<Delivery> getRegistered() {
        return deliveriesRepository.findByStatus(DeliveryStatus.REGISTERED);
    }

    /**
     * Gets all send but not yet delivered deliveries from the database.
     * 
     * @return a list of all send but not yet delivered deliveries
     */
    public List<Delivery> getSentUndelivered() {
        return deliveriesRepository.findByStatusNot(DeliveryStatus.DELIVERED);
    }

    /**
     * Gets all sent and delivered deliveries of the currently logged in client.
     * 
     * @return a list of all sent and delivered deliveries of the currently logged
     *         in client
     */
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

    /**
     * Gets all sent but not delivered deliveries of the currently logged in client.
     * 
     * @return a list of all sent but not delivered deliveries of the currently
     *         logged in client
     */
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

    /**
     * Gets all received deliveries by the currently logged in client.
     * 
     * @return a list of all received deliveries by the currently logged in client
     */
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

    /**
     * Gets all deliveries that are sent to and not yet delivered to the currently
     * logged in client.
     * 
     * @return a list of all deliveries that are sent to and not yet delivered to
     *         the currently logged in client
     */
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

    /**
     * Gets all deliveries that have been registered by the currently logged in
     * employee.
     * 
     * @return a list of all deliveries that have been registered by the currently
     *         logged in employee
     */
    public List<Delivery> getRegisteredByCurrentEmployee() {
        String email = this.authService.getLoggedInUser().getEmail();
        if (this.authService.isInRole("ROLE_OFFICE_EMPLOYEE")) {
            OfficeEmployee employee = this.officeEmployeesRepository.findByUserEmail(email);
            if (employee == null) {
                return new ArrayList<Delivery>();
            }

            return employee.getDeliveries();
        } else if (this.authService.isInRole("ROLE_COURIER")) {
            Courier courier = this.couriersRepository.findByUserEmail(email);
            if (courier == null) {
                return new ArrayList<Delivery>();
            }

            return courier.getDeliveries();
        }

        return new ArrayList<Delivery>();
    }
}
