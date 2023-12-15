package com.openclassroom.SafetyNetAlerts.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.openclassroom.SafetyNetAlerts.model.Firestation;
import com.openclassroom.SafetyNetAlerts.model.Person;
import com.openclassroom.SafetyNetAlerts.services.FirestationService;
import com.openclassroom.SafetyNetAlerts.services.MedicalRecordService;
import com.openclassroom.SafetyNetAlerts.services.PersonService;
import com.openclassroom.SafetyNetAlerts.utility.IdentifyKids;
import com.openclassroom.SafetyNetAlerts.utility.KidsLivingInThisHouse;
import com.openclassroom.SafetyNetAlerts.utility.MedicalRecordData;
import com.openclassroom.SafetyNetAlerts.utility.PersonAge;
import com.openclassroom.SafetyNetAlerts.utility.PersonAndFirestationLink;
import com.openclassroom.SafetyNetAlerts.utility.PersonPhone;
import lombok.Data;

@Data
@WebMvcTest(SafetyNetController.class)
@ExtendWith(MockitoExtension.class)
public class SafetyNetControllerTest {
    
    @MockBean
    private PersonService personService;

    @MockBean
    private FirestationService firestationService;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private SafetyNetController safetyNetController;

    @Test
    public void testGetPersonsLinkedToStation() {
        //Arrange
        String station = "5";
        String stationAddress = "1510 Culver St";
        //act
        when(firestationService.findFirestationByAdress(station)).thenReturn(Collections.singletonList(stationAddress));
        Person person1 = new Person( 
            "Olivier",
            "Serra",
            "1565 Culver St",
            "Culver",
            "97451",
            "841-874-6521",
            "OlivierSerra@email.com");
        Person person2 = new Person(
            "Johnwhoo",
            "Boydo",
            "1509 Culver St",
            "Culver",
            "97451",
            "841-874-6512",
            "jaboyd@email.com");
        List<Person> allPersons = Arrays.asList(person1, person2);
        when(personService.findAll()).thenReturn(allPersons);
        // Act
        ResponseEntity<PersonAndFirestationLink> responseEntity = safetyNetController.getPersonsLinkedToStation(station);
        PersonAndFirestationLink result = responseEntity.getBody();
        assertNotNull(result);
        List<PersonPhone> personsPhoneList = result.getPersons();
        //assert
        assertNotNull(personsPhoneList);
        HashMap<String, Integer> parentsAndKidsList = result.getAdultsAndChildren();
        assertNotNull(parentsAndKidsList);
    }   

    @Test
    public void testGetKidsLivingAtThisAddress() {
        //Arrange
        String address = "1565 Culver St";
        List<Person> peopleLivingAtAddress = Arrays.asList(
            new Person( 
                "Olivier",
                "Serra",
                "1565 Culver St",
                "Culver",
                "97451",
                "841-874-6521",
                "OlivierSerra@email.com"));
        when(personService.findOnePersonByAddress(address)).thenReturn(peopleLivingAtAddress);
        List<PersonAge> kids = Arrays.asList(
            new PersonAge("Anna", "Serra", 8L), 
            new PersonAge("Victor", "Serra", 5L));
        when(IdentifyKids.associateKidsWithParents(peopleLivingAtAddress, medicalRecordService.findAll(), kids)).thenReturn(kids);
        List<Person> householdList = Arrays.asList(/* ... */ );
        when(personService.getHouseholdComposedByKids("Anna", "Serra")).thenReturn(householdList);
        when(personService.getHouseholdComposedByKids("Victor", "Serra")).thenReturn(householdList);
        // Act
        ResponseEntity<List<KidsLivingInThisHouse>> responseEntity = safetyNetController.getkidsLivingAtThisAddress(address);
        // Assert
        List<KidsLivingInThisHouse> result = responseEntity.getBody();
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    public void testGetPhoneNumbersLinkedToStationNumber_Success() {
        // Arrange
        String station = "3";
        String firestationAddress = "1509 Culver St";
        String phoneNumber = "841-874-6521";
        when(firestationService.findFirestationByAdress(station)).thenReturn(Collections.singletonList(firestationAddress));
        Person person = new Person(
            "Olivier",
            "Serra",
            "1509 Culver St",
            "Culver",
            "97451",
            "841-874-6521",
            "OlivierSerra@email.com");
        when(personService.findAll()).thenReturn(Collections.singletonList(person));

        // Act
        ResponseEntity<List<String>> responseEntity = safetyNetController.getPhoneNumbersLinkedToStationNumber(station);
        
        // Assert
        List<String> result = responseEntity.getBody();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(phoneNumber, result.get(0));
        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllThePersonsAtThisAddress() {
        //Arrange
        String address = "123 Main St";
        String station = "5";

        //Créer une liste de personnes pour l'adresse donnée
        List<Person> personList = Collections.singletonList(
            new Person(
                "Olivier",
                "Serra",
                "1565 Culver St",
                "Culver",
                "97451",
                "841-874-6521",
                "OlivierSerra@email.com"
            ));

        // Configurer les mocks pour les services
        when(personService.findOnePersonByAddress(address)).thenReturn(personList);
        when(firestationService.findStationByAddress(address)).thenReturn(station);

        // Configurer la date de naissance dans le service medicalRecordService
        String birthdayString = "01/01/1990";
        LocalDate birthday = LocalDate.parse(birthdayString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        when(medicalRecordService.getBirthdayFromMedicalRecord("Olivier", "Serra")).thenReturn(birthdayString);

        // Configurer les médicaments et allergies dans le service medicalRecordService
        List<String> medications = Arrays.asList("pharmacol:5000mg", "terazine:10mg");
        List<String> allergies = Arrays.asList("peanut", "shellfish");
        when(medicalRecordService.getTreatmentFromMedicalRecord("Olivier", "Serra")).thenReturn(medications);
        when(medicalRecordService.getIllnessFromMedicalRecord("Olivier", "Serra")).thenReturn(allergies);    
        
        // Act
        ResponseEntity<HashMap<String, List<MedicalRecordData>>> responseEntity = safetyNetController.getAllThePersonsAtThisAddress(address);
        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        HashMap<String, List<MedicalRecordData>> result = responseEntity.getBody();
        assertNotNull(result);
        assertTrue(result.containsKey(station));
        List<MedicalRecordData> medicalRecordDatas = result.get(station);
        assertNotNull(medicalRecordDatas);
        assertEquals(1, medicalRecordDatas.size());
        MedicalRecordData medicalRecordData = medicalRecordDatas.get(0);
        assertEquals("Olivier", medicalRecordData.getFirstName());
        assertEquals("Serra", medicalRecordData.getLastName());
        assertEquals("841-874-6521", medicalRecordData.getPhone());
        assertEquals(33, medicalRecordData.getAge());
        assertEquals(medications, medicalRecordData.getMedications());
        assertEquals(allergies, medicalRecordData.getAllergies());
    }
      
    @Test
    public void testGetHouseholdLinkedToFirestation() {
        // Arrange
        List<String> station = Arrays.asList("1", "2");  // Stations associées à des adresses
    
        Firestation firestation1 = new Firestation("123 Main St", "1");
        Firestation firestation2 = new Firestation("456 Oak St", "2");
        when(firestationService.findAll()).thenReturn(Arrays.asList(firestation1, firestation2));
    
        Person person1 = new Person(
            "Olivier",
            "Serra",
            "1565 Culver St",
            "Culver",
            "97451",
            "841-874-6521",
            "OlivierSerra@email.com");
        Person person2 = new Person(
            "Johnwhoo",
            "Boydo",
            "1509 Culver St",
            "Culver",
            "97451",
            "841-874-6512",
            "jaboyd@email.com");
        when(personService.findOnePersonByAddress("123 Main St")).thenReturn(Collections.singletonList(person1));
        when(personService.findOnePersonByAddress("456 Oak St")).thenReturn(Collections.singletonList(person2));
    
        String birthdayString1 = "01/01/1980";
        LocalDate birthday1 = LocalDate.parse(birthdayString1, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        when(medicalRecordService.getBirthdayFromMedicalRecord("Olivier", "Serra")).thenReturn(birthdayString1);
        List<String> medications = Arrays.asList("pharmacol:5000mg", "terazine:10mg");
        List<String> Allergies = Arrays.asList("peanut", "shellfish");
        when(medicalRecordService.getTreatmentFromMedicalRecord("Olivier", "Serra")).thenReturn(medications);
        when(medicalRecordService.getIllnessFromMedicalRecord("Olivier", "Serra")).thenReturn(Allergies);

        String birthdayString2 = "02/02/1990";
        LocalDate birthday2 = LocalDate.parse(birthdayString2, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        when(medicalRecordService.getBirthdayFromMedicalRecord("Johnwhoo", "Boydo")).thenReturn(birthdayString2);
        List<String> medications2 = Arrays.asList("pharmacol:5000mg", "terazine:10mg");
        List<String> Allergies2 = Arrays.asList("nillacilan", "illisoxian");
        when(medicalRecordService.getTreatmentFromMedicalRecord("Johnwhoo", "Boydo")).thenReturn(medications2);
        when(medicalRecordService.getIllnessFromMedicalRecord("Johnwhoo", "Boydo")).thenReturn(Allergies2);

        // Act
        ResponseEntity<HashMap<String, List<MedicalRecordData>>> responseEntity = safetyNetController.getHouseholdLinkedToFirestation(station);
    
        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    
        HashMap<String, List<MedicalRecordData>> result = responseEntity.getBody();
        assertNotNull(result);
    
        assertTrue(result.containsKey("123 Main St"));
        List<MedicalRecordData> medicalRecordDatas1 = result.get("123 Main St");
        assertNotNull(medicalRecordDatas1);
        assertEquals(1, medicalRecordDatas1.size());
    
        MedicalRecordData medicalRecordData1 = medicalRecordDatas1.get(0);
        assertEquals("Olivier", medicalRecordData1.getFirstName());
        assertEquals("Serra", medicalRecordData1.getLastName());
        assertEquals("841-874-6521", medicalRecordData1.getPhone());
        assertEquals(43, medicalRecordData1.getAge());
        assertIterableEquals(medications, medicalRecordService.getTreatmentFromMedicalRecord("Olivier","Serra"));
        assertIterableEquals(Allergies, medicalRecordService.getIllnessFromMedicalRecord("Olivier","Serra"));
    
        assertTrue(result.containsKey("456 Oak St"));
        List<MedicalRecordData> medicalRecordDatas2 = result.get("456 Oak St");
        assertNotNull(medicalRecordDatas2);
        assertEquals(1, medicalRecordDatas2.size());
    
        MedicalRecordData medicalRecordData2 = medicalRecordDatas2.get(0);
        assertEquals("Johnwhoo", medicalRecordData2.getFirstName());
        assertEquals("Boydo", medicalRecordData2.getLastName());
        assertEquals("841-874-6512", medicalRecordData2.getPhone());
        assertEquals(33, medicalRecordData2.getAge());
        assertIterableEquals(medications, medicalRecordService.getTreatmentFromMedicalRecord("Johnwhoo","Boydo"));
        assertIterableEquals(Allergies2, medicalRecordService.getIllnessFromMedicalRecord("Johnwhoo", "Boydo"));
    }
 
    @Test
    public void testGetAllTheEmailFromTheCity() {
        // Arrange
        String city = "Culver";

        Person person1 = new Person(
            "Olivier",
            "Serra",
            "1565 Culver St",
            "Culver",
            "97451",
            "841-874-6521",
            "OlivierSerra@email.com");
        Person person2 = new Person(
            "Johnwhoo",
            "Boydo",
            "1509 Culver St",
            "Culver",
            "97451",
            "841-874-6512",
            "jaboyd@email.com");
        when(personService.findAll()).thenReturn(List.of(person1, person2));

        // Act
        ResponseEntity<List<String>> responseEntity = safetyNetController.getAllTheEmailFromTheCity(city);

        // Assert
        List<String> mailingList = responseEntity.getBody();
        assertNotNull(mailingList);
        assertEquals(2, mailingList.size());
        assertTrue(mailingList.contains("OlivierSerra@email.com"));
        assertTrue(mailingList.contains("jaboyd@email.com"));
    }
}

