package com.nbu.logistics.entities;

import java.time.LocalDate;

import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdOn;
}