package com.nbu.logistics.controllers.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserViewModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}