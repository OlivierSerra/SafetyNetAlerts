package com.openclassroom.SafetyNetAlerts.model;

import lombok.*;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicalRecord {
    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;
}
