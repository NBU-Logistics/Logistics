package com.nbu.logistics.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

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
public class User extends BaseEntity {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @ManyToMany
    private List<UserRole> roles;
}