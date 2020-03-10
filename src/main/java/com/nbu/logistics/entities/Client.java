package com.nbu.logistics.entities;

import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client extends BaseEntity {
    @OneToOne
    private User user;

    @OneToMany
    private List<Delivery> deliveries;
}