package com.nbu.logistics.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Where(clause = "is_deleted='false'")
public class Office extends BaseEntity {
    @Column(nullable = false)
    @Length(min = 3, max = 30)
    @NotEmpty
    private String name;

    @Column(nullable = false)
    @Length(min = 3, max = 30)
    @NotEmpty
    private String address;
}