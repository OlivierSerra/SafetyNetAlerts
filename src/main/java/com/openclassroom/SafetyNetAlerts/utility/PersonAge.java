package com.openclassroom.SafetyNetAlerts.utility;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonAge {
    private String firstName;
    private String lastName;
    private long age;

    public PersonAge(String firstName, String lastName, Long age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
     
}

