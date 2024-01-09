package com.openclassroom.SafetyNetAlerts.utility;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrossingPerson_MedicalDatas {
    private String firstName;
    private String lastName;
    private String address;
    private long age;
    private String email;
    private List<String> medications;
    private List<String> allergies;

}
