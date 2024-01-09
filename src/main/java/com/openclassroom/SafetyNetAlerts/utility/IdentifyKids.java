package com.openclassroom.SafetyNetAlerts.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.openclassroom.SafetyNetAlerts.model.Household;
import com.openclassroom.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroom.SafetyNetAlerts.model.Person;
import com.openclassroom.SafetyNetAlerts.services.PersonService;

public class IdentifyKids {
    private final PersonService personService;

    public IdentifyKids(PersonService personService){
        this.personService = personService;
    }

    public static List<KidsLivingInThisHouse> associateKidsWithParents(
            List<Person> list,
            List<MedicalRecord> medicalRecords,
            IdentifyKids identifyKids) {

        List<KidsLivingInThisHouse> kidsWithParents = new ArrayList<>();

        if (list != null && !list.isEmpty()) {
        for (Person kids : list) {
            for (MedicalRecord medicalRecord : medicalRecords) {
                if (Objects.equals(kids.getFirstName(), medicalRecord.getFirstName()) &&
                    Objects.equals(kids.getLastName(), medicalRecord.getLastName())) {

                    int age = (int) ParentsAndKidsTotal.calculateAge(medicalRecord.getBirthdate());

                    if (age < 18) {
                        //PersonService personService;
                        List <Person> householdMembers = identifyKids.personService.getHouseholdComposedByKids(kids.getFirstName(), kids.getLastName());
                        Household household = extractHouseholdInfo(householdMembers);
                        KidsLivingInThisHouse kidsLivingInThisHouse = new KidsLivingInThisHouse(
                                kids.getFirstName(),
                                kids.getLastName(),
                                age,
                                household
                        );
                        kidsWithParents.add(kidsLivingInThisHouse);
                        }
                }
            }
        }
    }
        return kidsWithParents;
}


    private static Household extractHouseholdInfo(List<Person> householdMembers) {
        Household householdInfo = new Household();

        // Supposons que la première personne dans la liste représente l'adresse du ménage
        if (!householdMembers.isEmpty()) {
            householdInfo.setAddress(householdMembers.get(0).getAddress());
        }

        // Les membres du ménage sont les personnes restantes dans la liste
        if (householdMembers.size() > 1) {
            List<Person> members = householdMembers.subList(1, householdMembers.size());
            householdInfo.setMembers(members);
    }
        return householdInfo;
}
}


   




