package com.nbu.logistics.entities;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Where(clause = "is_deleted='false'")
public class User extends BaseEntity {
    @Length(min = 3, max = 30)
    private String firstName;

    @Length(min = 3, max = 30)
    private String lastName;

    @Length(min = 3, max = 30)
    private String email;

    @Length(min = 3)
    private String password;

    @ManyToMany
    private List<UserRole> roles;
}