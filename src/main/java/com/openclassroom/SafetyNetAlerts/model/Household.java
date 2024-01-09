package com.openclassroom.SafetyNetAlerts.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Household {
    private String address;
    private List<Person> members;

    public Household(String address, List<Person> members) {
        this.address =address;
        this.members = members;
    }
}
