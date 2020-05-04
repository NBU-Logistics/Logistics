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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveriesService {
    @Autowired
    private DeliveriesRepository deliveriesRepository;

    @Autowired
    private ClientsRepository clientsRepository;

    public Client findUser(String email) {
        return clientsRepository.findByUserEmail(email);
    }

    public void checkDeliveryName(Delivery del) throws InvalidDataException {
        boolean isPresent = deliveriesRepository.findAll().stream()
                .anyMatch(delivery -> delivery.getName().equals(del.getName()));

        if (isPresent) {
            throw new InvalidDataException("This delivery name already exists. Please, enter a different one.");
        }
    }

    public void checkDeliveryWeight(double weight) throws InvalidDataException {
        if(weight <= 0) {
            throw new InvalidDataException("Please, enter valid weight.");
        }
    }

    public void addDelivery(Delivery delivery) throws InvalidDataException {
        this.checkDeliveryName(delivery);
        this.checkDeliveryWeight(delivery.getWeight());

        String senderEmail = delivery.getSender().getUser().getEmail();
        String recipientEmail = delivery.getRecipient().getUser().getEmail();

        Client sender = this.findUser(senderEmail);
        Client recipient = this.findUser(recipientEmail);

        sender.getDeliveries().add(delivery);
        recipient.getDeliveries().add(delivery);

        delivery.setPrice(delivery.getWeight() * 3);
        delivery.setSender(sender);
        delivery.setRecipient(recipient);
        delivery.setStatus(DeliveryStatus.POSTED);
        delivery.setCreatedOn(new Date());
        deliveriesRepository.save(delivery);
    }

    public Delivery findDelivery(String id) {
        return deliveriesRepository.findByName(id);
    }

    public void editDelivery(Delivery newDelivery, String id) throws InvalidDataException {

        Delivery delivery = deliveriesRepository.findByName(id);

        if (delivery == null) {
            throw new InvalidDataException("Delivery does not exist!");
        }

        if(delivery.getAddress() != null && !delivery.getAddress().isBlank()) {
            delivery.setAddress(newDelivery.getAddress());
        }

        if(delivery.getWeight() > 0) {
            delivery.setWeight(newDelivery.getWeight());
            delivery.setPrice(delivery.getWeight() * 3);
        }

        if(delivery.getStatus() != null) {
            delivery.setStatus(newDelivery.getStatus());
        }

        delivery.setOfficeDelivery(newDelivery.isOfficeDelivery());
        delivery.setCreatedOn(new Date());

      /*  if(delivery.getSender() != null && !delivery.getSender().getUser().getEmail().isBlank()) {
            delivery.setSender(newDelivery.getSender());
            Client sender = this.findUser(newDelivery.getSender().getUser().getEmail());
            if(sender.getDeliveries().stream().noneMatch(del -> del.getName().equals(delivery.getName())))
               sender.getDeliveries().add(delivery);
        }

        if(delivery.getRecipient() != null && !delivery.getRecipient().getUser().getEmail().isBlank()) {
            delivery.setRecipient(newDelivery.getRecipient());
            Client recipient = this.findUser(newDelivery.getRecipient().getUser().getEmail());
            if(recipient.getDeliveries().stream().noneMatch(del -> del.getName().equals(delivery.getName())))
               recipient.getDeliveries().add(delivery);
        }*/

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
        return deliveriesRepository.findAll().stream()
                .filter(delivery -> delivery.getStatus() == (DeliveryStatus.REGISTERED) && delivery.getStatus() != null)
                .collect(Collectors.toList());
    }

    public List<Delivery> getSentUndelivered() {
        return deliveriesRepository.findAll().stream()
                .filter(delivery -> delivery.getStatus() != (DeliveryStatus.DELIVERED) && delivery.getStatus() != null)
                .collect(Collectors.toList());
    }

    public List<Delivery> getSentDelivered(MyUserPrincipal user) {
        String email = user.getEmail();

        List<Delivery> deliveries = this.clientsRepository.findByUserEmail(email).getDeliveries();
        List<Delivery> sentDelivered = deliveries.stream()
                .filter(delivery -> delivery.getSender().getUser().getEmail().equals(email)
                        && delivery.getStatus() == (DeliveryStatus.DELIVERED) && delivery.getStatus() != null)
                .collect(Collectors.toList());

        return sentDelivered;
    }

    public List<Delivery> getSentUndelivered(MyUserPrincipal user) {
        String email = user.getEmail();

        List<Delivery> deliveries = this.clientsRepository.findByUserEmail(email).getDeliveries();
        List<Delivery> sentUndelivered = deliveries.stream()
                .filter(delivery -> delivery.getSender().getUser().getEmail().equals(email)
                        && delivery.getStatus() != (DeliveryStatus.DELIVERED) && delivery.getStatus() != null)
                .collect(Collectors.toList());

        return sentUndelivered;
    }

    public List<Delivery> getReceivedDelivered(MyUserPrincipal user) {
        String email = user.getEmail();

        List<Delivery> deliveries = this.clientsRepository.findByUserEmail(email).getDeliveries();
        List<Delivery> receivedDelivered = deliveries.stream()
                .filter(delivery -> delivery.getRecipient().getUser().getEmail().equals(email)
                        && delivery.getStatus() == (DeliveryStatus.DELIVERED))
                .collect(Collectors.toList());

        return receivedDelivered;
    }

    public List<Delivery> getReceivedUndelivered(MyUserPrincipal user) {
        String email = user.getEmail();

        List<Delivery> deliveries = this.clientsRepository.findByUserEmail(email).getDeliveries();
        List<Delivery> receivedUndelivered = deliveries.stream()
                .filter(delivery -> delivery.getRecipient().getUser().getEmail().equals(email)
                        && delivery.getStatus() != (DeliveryStatus.DELIVERED) && delivery.getStatus() != null)
                .collect(Collectors.toList());

        return receivedUndelivered;
    }
}

