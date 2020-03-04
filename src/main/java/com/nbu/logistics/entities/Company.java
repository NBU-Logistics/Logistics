package com.nbu.logistics.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company extends BaseEntity {
    @Column
    private String name;

    @Column
    private String address;

    @Column
    private String ownerName;

    @Column
    private String phoneNumber;

    @Column
    private String email;
}