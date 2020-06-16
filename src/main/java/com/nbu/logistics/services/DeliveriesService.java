package com.nbu.logistics.services;

import com.nbu.logistics.config.MyUserPrincipal;
import com.nbu.logistics.entities.Client;
import com.nbu.logistics.entities.Delivery;
import com.nbu.logistics.entities.DeliveryStatus;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.ClientsRepository;
import com.nbu.logistics.repositories.DeliveriesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class DeliveriesService {
    @Autowired
    private DeliveriesRepository deliveriesRepository;

    @Autowired
    private ClientsRepository clientsRepository;

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

        delivery.setPrice(delivery.getWeight() * 3);
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
        Delivery delivery = deliveriesRepository.findByName(id);

        if (delivery == null) {
            throw new InvalidDataException("Delivery does not exist!");
        }

        if (delivery.getAddress() != null && !delivery.getAddress().isBlank()) {
            delivery.setAddress(newDelivery.getAddress());
        }

        if (delivery.getWeight() > 0) {
            delivery.setWeight(newDelivery.getWeight());
            delivery.setPrice(delivery.getWeight() * 3);
        }

        if (delivery.getStatus() != null) {
            delivery.setStatus(newDelivery.getStatus());
        }

        delivery.setOfficeDelivery(newDelivery.isOfficeDelivery());
        delivery.setCreatedOn(LocalDate.now());

        deliveriesRepository.save(delivery);
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

    public List<Delivery> getSentDelivered(MyUserPrincipal user) {
        String email = user.getEmail();

        List<Delivery> sentDelivered = this.clientsRepository.findByUserEmail(email).getSentDeliveries().stream()
                .filter(delivery -> delivery.getStatus() == DeliveryStatus.DELIVERED).collect(Collectors.toList());

        return sentDelivered;
    }

    public List<Delivery> getSentUndelivered(MyUserPrincipal user) {
        String email = user.getEmail();

        List<Delivery> sentUndelivered = this.clientsRepository.findByUserEmail(email).getSentDeliveries().stream()
                .filter(delivery -> delivery.getStatus() != DeliveryStatus.DELIVERED && delivery.getStatus() != null)
                .collect(Collectors.toList());

        return sentUndelivered;
    }

    public List<Delivery> getReceivedDelivered(MyUserPrincipal user) {
        String email = user.getEmail();

        List<Delivery> receivedDelivered = this.clientsRepository.findByUserEmail(email).getReceivedDeliveries()
                .stream().filter(delivery -> delivery.getStatus() == DeliveryStatus.DELIVERED)
                .collect(Collectors.toList());

        return receivedDelivered;
    }

    public List<Delivery> getReceivedUndelivered(MyUserPrincipal user) {
        String email = user.getEmail();

        List<Delivery> receivedUndelivered = this.clientsRepository.findByUserEmail(email).getReceivedDeliveries()
                .stream()
                .filter(delivery -> delivery.getStatus() != DeliveryStatus.DELIVERED && delivery.getStatus() != null)
                .collect(Collectors.toList());

        return receivedUndelivered;
    }
}
