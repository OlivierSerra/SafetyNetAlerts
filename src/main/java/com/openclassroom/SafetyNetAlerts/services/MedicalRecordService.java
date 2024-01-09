package com.openclassroom.SafetyNetAlerts.services;

import com.openclassroom.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroom.SafetyNetAlerts.repository.MedicalRecordRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.util.List;

@Data
@Service
public class MedicalRecordService {
    
    private MedicalRecordRepository medicalRecordRepository;
    public MedicalRecordService (MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }
    public List<MedicalRecord> findAll() {
        return medicalRecordRepository.getMedicalRecords();
    }

/*
* add medicalRecord in the list
*/
    public MedicalRecord save(MedicalRecord medicalRecord ) {
        MedicalRecord addedmedicalRecord = medicalRecordRepository.saveMedicalRecord(medicalRecord); 
        return addedmedicalRecord;
    }

/*
* This is used to delete a medicalRecord from the list
*/
    public MedicalRecord delete(String firstName, String lastName) {
        return medicalRecordRepository.deleteMedicalRecord(firstName, lastName);
    }

/*
* This is used to return a medicalRecord if criterias match  
*/       
    public MedicalRecord update(String firstName, String lastName, MedicalRecord medicalRecordToUpdate) {
        return medicalRecordRepository.updateMedicalRecord(firstName, lastName, medicalRecordToUpdate);   
    }

/*
* This is used to return one medicalRecord if criterias match  
*/

    public MedicalRecord getMedicalRecord (String firstName, String lastName) {
        return medicalRecordRepository.find(firstName, lastName);
    }

    @lombok.Generated
    public String getBirthdayFromMedicalRecord(String firstName, String lastName) {
        return this.medicalRecordRepository.getBirthdayFromMedicalRecord(firstName, lastName);
    }

    @lombok.Generated
    public List<String> getTreatmentFromMedicalRecord(String firstName, String lastName) {
        return this.medicalRecordRepository.getPillsFromMedicalRecord(firstName, lastName);
    }

    @lombok.Generated
    public List<String> getIllnessFromMedicalRecord(String firstName, String lastName) {
        return this.medicalRecordRepository.getDeaseaseFromMedicalRecord(firstName, lastName);
    }
}

