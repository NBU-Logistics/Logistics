package com.nbu.logistics.entities;

import javax.persistence.Entity;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
public class Settings extends BaseEntity {
    @DecimalMin(value = "0.1")
    private double pricePerKilogram;

    @Min(value = 0)
    @Max(value = 100)
    private int sentToOfficeDiscount;
}