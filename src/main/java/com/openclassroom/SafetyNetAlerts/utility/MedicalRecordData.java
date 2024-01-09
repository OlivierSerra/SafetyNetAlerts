package com.openclassroom.SafetyNetAlerts.utility;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MedicalRecordData {
    private String firstName;
    private String lastName;
    private String phone;
    private long age;
    private List<String> medications;
    private List<String> allergies;
}
