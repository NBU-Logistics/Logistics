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

    public void addDelivery(Delivery delivery) {
        String senderEmail = delivery.getSender().getUser().getEmail();
        String recipientEmail = delivery.getRecipient().getUser().getEmail();

        Client sender = this.findUser(senderEmail);
        Client recipient = this.findUser(recipientEmail);

        delivery.setPrice(delivery.getWeight() * 3);
        delivery.setSender(sender);
        delivery.setRecipient(recipient);
        delivery.setStatus(DeliveryStatus.POSTED);
        delivery.setCreatedOn(new Date());
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

    public List<Delivery> getAllDeliveries() {
        return deliveriesRepository.findAll();
    }

    public List<Delivery> getSentDeliveries(MyUserPrincipal user) {
        String email = user.getEmail();
        List<Delivery> deliveries = this.clientsRepository.findByUserEmail(email).getDeliveries();
        List<Delivery> sentDeliveries = deliveries.stream()
                .filter(delivery -> delivery.getSender().getUser().getEmail().equals(email))
                .collect(Collectors.toList());

        return sentDeliveries;
    }

    public List<Delivery> getAwaitByDeliveries(MyUserPrincipal user) {
        String email = user.getEmail();

        return deliveriesRepository.findAll().stream()
                .filter(delivery -> delivery.getSender().getUser().getEmail().equals(email)
                        && delivery.getStatus() != (DeliveryStatus.DELIVERED) && delivery.getStatus() != null)
                .collect(Collectors.toList());
    }

    public List<Delivery> getAwaitToDeliveries(MyUserPrincipal user) {
        String email = user.getEmail();

        return deliveriesRepository.findAll().stream()
                .filter(delivery -> delivery.getRecipient().getUser().getEmail().equals(email)
                        && delivery.getStatus() != (DeliveryStatus.DELIVERED) && delivery.getStatus() != null)
                .collect(Collectors.toList());
    }

    public List<Delivery> getDeliveredDeliveries(MyUserPrincipal user) {
        String email = user.getEmail();

        return deliveriesRepository.findAll().stream()
                .filter(delivery -> delivery.getRecipient().getUser().getEmail().equals(email))
                .collect(Collectors.toList());
    }
}

/*
 * /Client client = clientsRepository.findByUserEmail(email);
 * 
 * List<Delivery> deli = new ArrayList<Delivery>();
 * deliveriesRepository.findAll().forEach(deli::add);
 * 
 * 
 * List<Delivery> deliCl = new ArrayList<Delivery>(); // String nz =
 * delivery.getStatus().name();
 * 
 * for(Delivery del : deli) {
 * if(del.getSender().getUser().getEmail().equals(email) && del.getStatus() !=
 * (DeliveryStatus.DELIVERED) && del.getStatus() != null) {
 * //System.out.println(delivery.getStatus()); deliCl.add(del); } }
 * 
 * return deliCl;
 */