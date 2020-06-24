package com.nbu.logistics.entities;

import javax.persistence.Entity;
import javax.validation.constraints.*;

import org.hibernate.annotations.Where;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Where(clause = "is_deleted='false'")
public class Settings extends BaseEntity {
    @DecimalMin(value = "0.1")
    private double pricePerKilogram;

    @Min(value = 0)
    @Max(value = 100)
    private int sentToOfficeDiscount;
}