package com.openclassroom.SafetyNetAlerts.model;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
}

    

   