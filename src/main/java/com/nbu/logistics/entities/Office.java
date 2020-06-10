package com.nbu.logistics.entities;

import javax.persistence.*;

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
public class Office extends BaseEntity {
    private String name;
}