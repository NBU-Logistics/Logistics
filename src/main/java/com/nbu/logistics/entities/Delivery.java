package com.nbu.logistics.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Where(clause = "is_deleted='false'")
public class Delivery extends BaseEntity {
    @Column
    private String name;

    @Column
    private DeliveryStatus status;

    @ManyToOne
    private Client sender;

    @ManyToOne
    private Client recipient;

    @Column
    private String address;

    @Column
    private boolean isOfficeDelivery;

    @Column
    private double weight;

    @Column
    private double price;

    @Column
    private Date createdOn;
}