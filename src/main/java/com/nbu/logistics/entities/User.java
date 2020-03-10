package com.nbu.logistics.entities;

import javax.persistence.Entity;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User extends BaseEntity {
    @Size(min = 3, max = 20)
    private String firstName;

    @Size(min = 3, max = 20)
    private String lastName;

    @Size(min = 3, max = 20)
    private String email;

    @Size(min = 3, max = 20)
    private String password;
}