package com.openclassroom.SafetyNetAlerts.utility;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openclassroom.SafetyNetAlerts.model.Firestation;
import com.openclassroom.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroom.SafetyNetAlerts.model.Person;
import lombok.*;
import java.util.List;

/**
 * Content class is used by Object Mapper to read the json file containing
 * all the data for creating Lists of Objects Persons, Firestations and MedicalRecords.
 */
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class Content {
    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalrecords;

}
