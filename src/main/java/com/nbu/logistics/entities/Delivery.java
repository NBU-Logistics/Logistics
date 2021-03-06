package com.nbu.logistics.entities;

import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;

import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Where(clause = "is_deleted='false'")
public class Delivery extends BaseEntity {
    @Length(min = 3, max = 30)
    private String name;

    @Column
    private DeliveryStatus status = DeliveryStatus.POSTED;

    @ManyToOne
    private Client sender;

    @ManyToOne
    private Client recipient;

    @Length(min = 3, max = 30)
    private String address;

    @Column
    private boolean isOfficeDelivery = false;

    @DecimalMin(value = "0.1")
    private double weight;

    private double price = 0.1;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdOn = LocalDate.now();
}