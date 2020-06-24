package com.nbu.logistics.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.Where;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Where(clause = "is_deleted='false'")
public class UserRole extends BaseEntity {
    @Column(nullable = false, unique = true)
    @NotEmpty
    private String name;
}