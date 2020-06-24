package com.nbu.logistics.entities;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Where(clause = "is_deleted='false'")
public class Client extends BaseEntity {
    @OneToOne
    private User user;

    @ManyToMany
    private List<Delivery> sentDeliveries;

    @ManyToMany
    private List<Delivery> receivedDeliveries;
}