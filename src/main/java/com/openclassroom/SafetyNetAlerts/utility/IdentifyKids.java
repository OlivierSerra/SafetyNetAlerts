package com.openclassroom.SafetyNetAlerts.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.openclassroom.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroom.SafetyNetAlerts.model.Person;

public class IdentifyKids {

    public static List<PersonAge> associateKidsWithParents(List<Person> peopleLivingAtAddress, List<MedicalRecord> medicalRecords, List<PersonAge> kids) {
        List<PersonAge> kidsWithParents = new ArrayList<>();
        for (Person child : peopleLivingAtAddress) {
            for (PersonAge personAge : kids) {
                for (MedicalRecord medicalRecord : medicalRecords){
                    if (Objects.equals(child.getFirstName(), personAge.getFirstName()) &&
                        Objects.equals(child.getLastName(), personAge.getLastName()) &&
                        Objects.equals(personAge.getFirstName(), personAge.getFirstName()) &&
                        Objects.equals(personAge.getLastName(), personAge.getLastName())) {
                        kidsWithParents.add(personAge);
                        break;
                        }
                    }
                }
            }
        return kidsWithParents;
        }

    public static List<PersonAge> calculateAgeFromMedicalRecord(List<PersonAge> kids, List<MedicalRecord> medicalRecords) {
        List<PersonAge> kidsWithAge = new ArrayList<>();
        for (PersonAge personAge : kids) {
            for (MedicalRecord medicalRecord : medicalRecords) {
                if (Objects.equals(personAge.getFirstName(), medicalRecord.getFirstName()) &&
                        Objects.equals(personAge.getLastName(), medicalRecord.getLastName())) {
                    if (ParentsAndKidsTotal.calculateAge(medicalRecord.getBirthdate()) < 18) {
                        personAge.setAge(ParentsAndKidsTotal.calculateAge(medicalRecord.getBirthdate()));
                        kidsWithAge.add(personAge);
                    }
                    break;  
                }
            }
        }
        return kidsWithAge;
        }
}


