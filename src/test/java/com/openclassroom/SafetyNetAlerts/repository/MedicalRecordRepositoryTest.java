package com.openclassroom.SafetyNetAlerts.repository;

//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;

//import com.openclassroom.SafetyNetAlerts.repository.JsonReader;
//import com.openclassroom.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassroom.SafetyNetAlerts.model.MedicalRecord;
//import com.openclassroom.SafetyNetAlerts.services.MedicalRecordService;
import com.openclassroom.SafetyNetAlerts.utility.JsonReader;

@SpringBootTest
public class    MedicalRecordRepositoryTest {

    public MedicalRecordRepository medicalRecordRepository;
    public JsonReader dataReaderMock;

    @Autowired
    public MedicalRecordRepositoryTest(JsonReader dataReaderMock) throws Exception {
        this.dataReaderMock = dataReaderMock; 
        this.medicalRecordRepository = new MedicalRecordRepository(dataReaderMock);
    }

    @Test
    public void testGetMedicalRecord() throws Exception {
        //arrange
        JsonReader dataReaderMock = mock(JsonReader.class);
        MedicalRecord medicalRecord1 = new MedicalRecord(
            "Olivier",
            "Serra",
            "03/06/1984",
            Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
            Arrays.asList("nillacilan"));
        List<MedicalRecord> medicalRecordList = Arrays.asList(medicalRecord1);
        //act
        when(dataReaderMock.getMedicalRecordsData()).thenReturn(medicalRecordList);
        MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepository(dataReaderMock); 
        List<MedicalRecord> result = medicalRecordRepository.getMedicalRecords();
        //assert
        assertEquals(1, result.size());
        assertEquals(medicalRecord1, result.get(0));
    }

/****************************************** test to save   *********************************/
    @Test
    public void testSaveMedicalRecord() throws Exception {
        //arrange
        JsonReader dataReaderMock = mock(JsonReader.class);
        MedicalRecord medicalRecordToSave = new MedicalRecord(
            "Olivier",
            "Serra",
            "03/06/1984",
            Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
            Arrays.asList("nillacilan"));
        //act
        MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepository(dataReaderMock);
        medicalRecordRepository.saveMedicalRecord(medicalRecordToSave);
        //assert 
        List<MedicalRecord> result = medicalRecordRepository.getMedicalRecords();
        assertEquals(1, result.size());
        assertEquals(medicalRecordToSave, result.get(0));
    }

/******************************************** test to delete    *******************************/

    @Test
    public void testDeleteMedicalRecord() throws Exception {
        //arrange
        JsonReader dataReaderMock = mock(JsonReader.class);
        MedicalRecord medicalRecordToSave = new MedicalRecord(
            "Olivier",
            "Serra",
            "03/06/1984",
            Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
            Arrays.asList("nillacilan"));
        //act
        MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepository(dataReaderMock);
        medicalRecordRepository.saveMedicalRecord(medicalRecordToSave);

        List<MedicalRecord> initialmedicalRecords = medicalRecordRepository.getMedicalRecords();
        assertEquals(1, initialmedicalRecords.size());

        medicalRecordRepository.deleteMedicalRecord("Olivier", "Serra");
 
        List<MedicalRecord> result = medicalRecordRepository.getMedicalRecords();
        assertEquals(0, result.size());
    }

/**************** test to update   *********************/

     @Test
    public void testUpdateMedicalRecord() throws Exception {
        //arrange
        JsonReader dataReaderMock = mock(JsonReader.class);
        String firstNameToUpdate = "Olivier";
        String lastNameToUpdate = "Serra";
        MedicalRecord medicalRecordToUpdate = new MedicalRecord(
            firstNameToUpdate,
            lastNameToUpdate,
            "03/06/1984",
            Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
            Arrays.asList("nillacilan"));
        MedicalRecord updatedMedicalRecord = new MedicalRecord(
            firstNameToUpdate,
            lastNameToUpdate,
            "03/06/1985",
            Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
            Arrays.asList("nillacilan"));    
        //act
        MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepository(dataReaderMock);
        medicalRecordRepository.saveMedicalRecord(medicalRecordToUpdate);
        medicalRecordRepository.updateMedicalRecord(firstNameToUpdate,lastNameToUpdate, updatedMedicalRecord);
        //assert 
        List<MedicalRecord> result = medicalRecordRepository.getMedicalRecords();
        assertEquals(1, result.size());
        assertEquals(updatedMedicalRecord, result.get(0));
    }
}

