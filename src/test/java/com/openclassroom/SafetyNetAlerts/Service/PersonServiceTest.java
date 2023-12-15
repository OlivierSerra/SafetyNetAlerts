package com.openclassroom.SafetyNetAlerts.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
//import java.util.Arrays;
//import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.openclassroom.SafetyNetAlerts.model.Person;
import com.openclassroom.SafetyNetAlerts.repository.PersonRepository;
import com.openclassroom.SafetyNetAlerts.services.PersonService;

@SpringBootTest
public class PersonServiceTest {
 
    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;
 
     @Test
    public void testSavePerson() {
        //arrange
        Person personToSave = new Person(
        "Olivier", 
        "Serra",
        "1565 Culver St",
        "Culver",
        "97451",
        "841-874-6565",
        "olivierSerra@email.com");
        //act
        when(personRepository.save(any(Person.class))).thenReturn(personToSave);
        Person savedPerson = personService.save(personToSave);
        verify(personRepository, times(1)).save(personToSave);
        //assert
        assertEquals(personToSave, savedPerson);
    }

    @Test
    public void testDeletePerson() {
        //arrange
        Person personToDelete = new Person("Olivier", 
        "Serra",
        "1565 Culver St",
        "Culver",
        "97451",
        "841-874-6565",
        "olivierSerra@email.com");
        //act
        when(personRepository.deletePerson(anyString(), anyString())).thenReturn(null);
        personService.delete(personToDelete.getFirstName(), personToDelete.getLastName());
        //assert
        verify(personRepository, times(1)).deletePerson(personToDelete.getFirstName(), personToDelete.getLastName());
    }

    @Test
    public void testUpdatePerson() {
        //arrange
        Person personToUpdate = new Person("Olivier",
            "Serra",
            "1565 Culver St",
            "Culver",
            "97451",
            "841-874-6565",
            "olivierSerra@email.com");
        //assert
        when(personRepository.updatePerson(anyString(), anyString(), any(Person.class))).thenReturn(personToUpdate);
        Person updatedPerson = personService.update("Olivier", "Serra", personToUpdate);
        verify(personRepository, times(1)).updatePerson("Olivier", "Serra", personToUpdate);
        //assert
        assertEquals(personToUpdate, updatedPerson);
    }
    
    @Test
    public void testGetPerson() {
        //arrange
        Person expectedPerson = new Person(
            "Olivier",
            "Serra",
            "1565 Culver St",
            "Culver",
            "97451",
            "841-874-6565",
            "olivierSerra@email.com");
        //act
        when(personRepository.find("Olivier", "Serra")).thenReturn(expectedPerson);
        Person actualPerson = personService.getPerson("Olivier", "Serra");
        verify(personRepository, times(1)).find("Olivier", "Serra");
        //assert
        assertEquals(expectedPerson, actualPerson);
    }   
}


