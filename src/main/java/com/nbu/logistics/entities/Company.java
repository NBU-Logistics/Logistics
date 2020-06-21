package com.nbu.logistics.entities;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Where(clause = "is_deleted='false'")
public class Company extends BaseEntity {
    @Length(min = 3, max = 30)
    private String name;

    @Length(min = 3, max = 30)
    private String address;

    @Length(min = 3, max = 30)
    private String ownerName;

    @Length(min = 3, max = 30)
    private String phoneNumber;

    @Length(min = 3, max = 30)
    private String email;
}