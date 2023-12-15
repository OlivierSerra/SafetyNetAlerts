package com.openclassroom.SafetyNetAlerts.utility;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import com.openclassroom.SafetyNetAlerts.model.MedicalRecord;

public class ParentsAndKidsTotal {
    public static HashMap<String, Integer> parentsAndkidsNumber(List<PersonPhone> personsPhoneList,
        List<MedicalRecord> findAll) {
        HashMap<String, Integer> parentsAndKidsCounts = new HashMap<>();
        Integer ParentsNumber = 0;
        Integer kidsNumber = 0;
        parentsAndKidsCounts.put("ParentsNumber", ParentsNumber);
        parentsAndKidsCounts.put("kidsNumber", kidsNumber);
        for(PersonPhone p: personsPhoneList){
            //Get birthdate from medical record of that person
            String birthdate = null;
            for (MedicalRecord medicalRecord : findAll) {
                if (Objects.equals(medicalRecord.getFirstName(), p.getFirstName()) && Objects.equals(medicalRecord.getLastName(), p.getLastName())) {
                    birthdate = medicalRecord.getBirthdate();
                    break; // Sortir de la boucle une fois que la correspondance est trouvée
                }
            }
            //calculate birthdate
            if(calculateAge(birthdate) > 18){
                Integer count = parentsAndKidsCounts.get("ParentsNumber");
                parentsAndKidsCounts.put("ParentsNumber", count + 1);
            }
            else {
                Integer count = parentsAndKidsCounts.get("kidsNumber");
                parentsAndKidsCounts.put("kidsNumber", count + 1);
            }
        }
        return parentsAndKidsCounts;
        }

    public static long calculateAge(String birthday) {
        LocalDate birthDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }
}
