package com.openclassroom.SafetyNetAlerts.Service;


//import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.openclassroom.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroom.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassroom.SafetyNetAlerts.services.MedicalRecordService;

@SpringBootTest
public class MedicalRecordServiceTest {
    
    
    @MockBean
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private MedicalRecordService medicalRecordService;
 
     @Test
    public void testSaveMedicalRecord() {
        //arrange
        MedicalRecord medicalRecordToSave = new MedicalRecord(
            "Olivier",
            "Serra",
            "03/06/1984",
            Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
            Arrays.asList("nillacilan"));
        //act
            when(medicalRecordRepository.saveMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecordToSave);
        MedicalRecord savedMedicalRecord = medicalRecordService.save(medicalRecordToSave);
        verify(medicalRecordRepository, times(1)).saveMedicalRecord(medicalRecordToSave);
        //assert
        assertEquals(medicalRecordToSave, savedMedicalRecord);
    }

    @Test
    public void testDeleteMedicalRecord() {
        //arrange
        MedicalRecord medicalRecordToDelete = new MedicalRecord(
            "Olivier",
            "Serra",
            "03/06/1984",
            Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
            Arrays.asList("nillacilan"));
        //act-assert
        when(medicalRecordRepository.deleteMedicalRecord(anyString(), anyString())).thenReturn(null);;
        medicalRecordService.delete(medicalRecordToDelete.getFirstName(), medicalRecordToDelete.getLastName());
        verify(medicalRecordRepository, times(1)).deleteMedicalRecord(medicalRecordToDelete.getFirstName(), medicalRecordToDelete.getLastName());
    }

    @Test
    public void testUpdateMedicalRecord() {
        //arrange    
        MedicalRecord medicalRecordToUpdate = new MedicalRecord("Olivier",
            "Serra",
            "03/06/1984",
            Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
            Arrays.asList("nillacilan"));
        //act
        when(medicalRecordRepository.updateMedicalRecord(anyString(), anyString(), any(MedicalRecord.class))).thenReturn(medicalRecordToUpdate);
        MedicalRecord updatedMedicalRecord = medicalRecordService.update("Olivier", "Serra", medicalRecordToUpdate);
        verify(medicalRecordRepository, times(1)).updateMedicalRecord("Olivier", "Serra", medicalRecordToUpdate);
        //assert
        assertEquals(medicalRecordToUpdate, updatedMedicalRecord);
    }

    
    @Test
    public void testGetMedicalRecord() {
        //arrange
        MedicalRecord expectedMedicalRecord = new MedicalRecord(
            "Olivier",
            "Serra",
            "03/06/1984",
            Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
            Arrays.asList("nillacilan"));
        //act
        when(medicalRecordRepository.find("Olivier", "Serra")).thenReturn(expectedMedicalRecord);
        MedicalRecord actualMedicalRecord = medicalRecordService.getMedicalRecord("Olivier", "Serra");
        verify(medicalRecordRepository, times(1)).find("Olivier", "Serra");
        //assert
        assertEquals(expectedMedicalRecord, actualMedicalRecord);
    }
}
