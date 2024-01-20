package com.openclassroom.SafetyNetAlerts.controller;

import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

//import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import com.openclassroom.SafetyNetAlerts.model.Firestation;
import com.openclassroom.SafetyNetAlerts.model.Household;
import com.openclassroom.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroom.SafetyNetAlerts.model.Person;
import com.openclassroom.SafetyNetAlerts.services.FirestationService;
import com.openclassroom.SafetyNetAlerts.services.MedicalRecordService;
import com.openclassroom.SafetyNetAlerts.services.PersonService;
import com.openclassroom.SafetyNetAlerts.utility.IdentifyKids;
import com.openclassroom.SafetyNetAlerts.utility.KidsLivingInThisHouse;
import com.openclassroom.SafetyNetAlerts.utility.MedicalRecordData;
//import com.openclassroom.SafetyNetAlerts.utility.PersonAge;
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
        Integer stationId = Integer.parseInt(station);
        String stationAddress = "1510 Culver St";
        //act
        when(firestationService.findFirestationByStationNumber(eq(Integer.parseInt(station))))
            .thenReturn(Collections.singletonList(stationAddress));
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
        ResponseEntity<PersonAndFirestationLink> responseEntity = safetyNetController.getPersonsLinkedToStation(stationId);
        PersonAndFirestationLink result = responseEntity.getBody();
        assertNotNull(result);
        List<PersonPhone> personsPhoneList = result.getPersons();
        //assert
        assertNotNull(personsPhoneList);
        HashMap<String, Integer> parentsAndKidsList = result.getAdultsAndChildren();
        assertNotNull(parentsAndKidsList);
    }   

    @Test
    public void testGetKidsLivingAtThisAddressbis() {
        // Arrange
        String address = "1565 Culver St";

        List<Person> peopleLivingAtAddress = Arrays.asList(
            new Person("Olivier", "Serra", address, "Culver", "97451", "841-874-6521", "OlivierSerra@email.com"),
            new Person("Antonella", "Serra", address, "Culver", "97451", "841-874-6521", "jaboyd@email.com"),
            new Person("Anna", "Serra", address, "Culver", "97451", "841-874-6521", "jaboyd@email.com"),
            new Person("Victor", "Serra", address, "Culver", "97451", "841-874-6521", "jaboyd@email.com"));

        List<MedicalRecord> medicalRecords = Arrays.asList(
            new MedicalRecord("Olivier", "Serra", "01/01/2000", Arrays.asList("medication1"), Arrays.asList("allergy1")),
            new MedicalRecord("Antonella", "Serra", "01/01/2002", Arrays.asList("medication2"), Arrays.asList("allergy2")),
            new MedicalRecord("Anna", "Serra", "01/01/2014", Arrays.asList("medication3"), Arrays.asList("allergy3")),
            new MedicalRecord("Victor", "Serra", "01/01/2014", Arrays.asList("medication4"), Arrays.asList("allergy4")));

        IdentifyKids identifyKids = new IdentifyKids(personService);

        List<KidsLivingInThisHouse> kids = Arrays.asList(
            new KidsLivingInThisHouse("Anna", "Serra", 8L, new Household(address, Arrays.asList(
                new Person("Olivier", "Serra", address, "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                new Person("Antonella", "Serra", address, "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                new Person("Victor", "Serra", address, "Culver", "97451", "841-874-6512", "jaboyd@email.com")))),
            new KidsLivingInThisHouse("Victor", "Serra", 8L, new Household(address, Arrays.asList(
                new Person("Olivier", "Serra", address, "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                new Person("Antonella", "Serra", address, "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                new Person("Anna", "Serra", address, "Culver", "97451", "841-874-6512", "jaboyd@email.com")))));

        when(personService.findOnePersonByAddress(address)).thenReturn(peopleLivingAtAddress);
        when(medicalRecordService.findAll()).thenReturn(medicalRecords);

        // Act
        ResponseEntity<List<KidsLivingInThisHouse>> responseEntity = safetyNetController.getkidsLivingAtThisAddress(address);

        // Assert
        List<KidsLivingInThisHouse> result = responseEntity.getBody();
        assertNotNull(result);
        assertEquals(2, result.size()); // Assuming you have two kids in the test data
        // Add more assertions based on your specific requirements
    }

    @Test
    public void testAssociateKidsWithParents() {
        // Arrange
        PersonService personServiceMock = mock(PersonService.class);
        IdentifyKids identifyKids = new IdentifyKids(personServiceMock);

        // Mocking data
        List<Person> peopleList = Arrays.asList(
            new Person(
                "Anna",
                "Serra",
                "1509 Culver St",
                "Culver",
                "97451", 
                "841-874-6512", 
                "jaboyd@email.com" ));

        List<MedicalRecord> medicalRecords = Arrays.asList(
                new MedicalRecord(
                "Olivier",
                "Serra",
                "21/01/1981",
                null,
                null),
                new MedicalRecord(
                "Antonella",
                "Serra",
                "01/04/1982",
                null,
                null),
                new MedicalRecord(
                "Anna",
                "Serra",
                "01/01/2010",
                null,
                null),
                new MedicalRecord(
                "Victor",
                "Serra",
                "01/01/2015",
                null,
                null));

        // Stubbing
        when(personServiceMock.getHouseholdComposedByKids("Anna", "Serra")).thenReturn(Arrays.asList(
            new Person(
                "Olivier",
                "Serra",
                "1509 Culver St",
                "Culver",
                "97451", 
                "841-874-6512", 
                "jaboyd@email.com"),
                new Person(
                "Antonella",
                "Serra",
                "1509 Culver St",
                "Culver",
                "97451", 
                "841-874-6512", 
                "jaboyd@email.com"),
                new Person(
                "Victor",
                "Serra",
                "1509 Culver St",
                "Culver",
                "97451", 
                "841-874-6512", 
                "jaboyd@email.com")));
        
        // Act
        List<KidsLivingInThisHouse> result = IdentifyKids.associateKidsWithParents(peopleList, medicalRecords, identifyKids);

        // Assert
        assertEquals(1, result.size());
        // Add more specific assertions based on your expected results
    }

    @Test
    public void testGetPhoneNumbersLinkedToStationNumber_Success() {
        // Arrange
        String station = "3";
        Integer stationId = Integer.parseInt(station);
        String firestationAddress = "1509 Culver St";
        String phoneNumber = "841-874-6521";
        when(firestationService.findFirestationByStationNumber(stationId)).thenReturn(Collections.singletonList(firestationAddress));
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
        Integer stationId = Integer.parseInt(station);

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
        when(firestationService.findStationByAddress(address)).thenReturn(stationId);

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
        //assertEquals(33, medicalRecordData.getAge());
        assertEquals(medications, medicalRecordData.getMedications());
        assertEquals(allergies, medicalRecordData.getAllergies());
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

