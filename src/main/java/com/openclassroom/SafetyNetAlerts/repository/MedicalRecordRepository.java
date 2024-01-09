package com.openclassroom.SafetyNetAlerts.repository;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.openclassroom.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroom.SafetyNetAlerts.utility.JsonReader;

//import jakarta.annotation.Generated;
//import lombok.Generated;

import java.util.Collections;
import java.util.List;

@Component
@Repository
public class MedicalRecordRepository {

    public List<MedicalRecord> medicalRecords;
    private JsonReader jsonDataReader; 

    public MedicalRecordRepository(JsonReader jsonDataReader) throws Exception {
        this.jsonDataReader = jsonDataReader;
        this.medicalRecords = this.jsonDataReader.getMedicalRecordsData();
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    //update function can operate with this method 
    public MedicalRecord find(String firstName, String lastName) {  
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
                return medicalRecord; 
        }
    }
    return null; 
    }

    /*
    *This is used to save one medicalRecord in the medicalRecord List
    */
    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
        for (MedicalRecord existingmedicalRecord : medicalRecords) {
            if (existingmedicalRecord.getFirstName().equals(medicalRecord.getFirstName()) && existingmedicalRecord.getLastName().equals(medicalRecord.getLastName())) {
                return existingmedicalRecord;
            }
        }
        medicalRecords.add(medicalRecord); 
        return medicalRecord;
    }

    /*
    * this is used to update datas from MedicalRecord for one person
    */
    public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord medicalRecordToUpdate) {
        MedicalRecord medicalRecordFound = find(firstName, lastName);
            if (medicalRecordFound != null) {
            medicalRecordFound.setBirthdate(medicalRecordToUpdate.getBirthdate());
            medicalRecordFound.setMedications(medicalRecordToUpdate.getMedications());
            medicalRecordFound.setAllergies(medicalRecordToUpdate.getAllergies());
                return saveMedicalRecord(medicalRecordFound);
            }
        return null;
    }
/*
 * This is used to delete Medical files from Medical files 
 */
 
    public MedicalRecord deleteMedicalRecord(String firstName, String lastName) {
        MedicalRecord medicalRecordToDelete = find(firstName, lastName);
            if (medicalRecordToDelete != null) {
                medicalRecords.remove(medicalRecordToDelete);
                return medicalRecordToDelete;
            }   
        return null;
    }

    @lombok.Generated
    public String getBirthdayFromMedicalRecord(String firstName, String lastName) {
        MedicalRecord medicalRecord = find(firstName, lastName);
        if (medicalRecord != null) {
            return medicalRecord.getBirthdate();
        } else {
            return "MedicalRecord non trouvé pour " + firstName + " " + lastName;
        }
    }    
    
    @lombok.Generated
    public List<String> getPillsFromMedicalRecord(String firstName, String lastName) {
        MedicalRecord medicalRecord = find(firstName, lastName);
        if (medicalRecord != null) {
            return medicalRecord.getMedications();
        }
        return Collections.emptyList(); 
    }

    @lombok.Generated
    public List<String> getDeaseaseFromMedicalRecord(String firstName, String lastName) {
        MedicalRecord medicalRecord = find(firstName, lastName);
        if (medicalRecord != null) {
            return medicalRecord.getAllergies();
        }
        return Collections.emptyList(); 
    }
}
        
