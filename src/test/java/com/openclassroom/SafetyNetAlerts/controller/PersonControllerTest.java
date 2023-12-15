package com.openclassroom.SafetyNetAlerts.controller;

import com.openclassroom.SafetyNetAlerts.model.Person;
import com.openclassroom.SafetyNetAlerts.services.PersonService;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mockito.Mock;

@WebMvcTest(PersonControllerTest.class)
public class PersonControllerTest {

    @Mock
    private PersonService personService;
    private PersonController controller;

    @Mock
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        controller = new PersonController(personService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
    
    @Test
    public void testAddPerson() throws Exception {
        //arrange
        String jsonInput = "{ \"firstName\":\"Olivier\", \"lastName\":\"Serra\", \"address\":\"1565 Culver St\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-6521\", \"email\":\"OlivierSerra@email.com\" }";
        Person inputPerson = new Person(
            "Olivier",
            "Serra",
            "1565 Culver St",
            "Culver",
            "97451",
            "841-874-6521",
            "OlivierSerra@email.com");
        Person savedPerson = new Person(
            "Olivier",
            "Serra",
            "1565 Culver St",
            "Culver",
            "97451",
            "841-874-6521",
            "OlivierSerra@email.com");
        //act
        when(personService.save(inputPerson)).thenReturn(savedPerson);
        //assert 
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonInput))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Olivier"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Serra"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("1565 Culver St"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Culver"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zip").value("97451"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("841-874-6521"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("OlivierSerra@email.com"));
    verify(personService, times(1)).save(inputPerson);
    }
    
    @Test
    void testPersons() throws Exception {
        String jsonInput = "{ \"firstName\":\"Olivier\", \"lastName\":\"Serra\", \"address\":\"1565 Culver St\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-6521\", \"email\":\"OlivierSerra@email.com\" }";
        //arrange
        Person personToFind  = new Person(
            "Olivier",
            "Serra",
            "1565 Culver St",
            "Culver",
            "97451",
            "841-874-6521",
            "OlivierSerra@email.com");
        List<Person> persons = Arrays.asList(personToFind);
        //act
        when(personService.findAll()).thenReturn(persons);
        //assert
        mockMvc.perform(MockMvcRequestBuilders.put("/person/Olivier/Serra")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonInput))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void testUpdatePerson() throws Exception {
        //arrange
        String jsonInput = "{ \"firstName\":\"Olivier\", \"lastName\":\"Serra\", \"address\":\"1565 Culver St\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-6521\", \"email\":\"OlivierSerra@email.com\" }";
        Person personToUpdate = new Person(
            "Olivier",
            "Serra",
            "1565 Culver St",
            "Culver",
            "97451",
            "841-874-6521",
            "OlivierSerra@email.com");
        Person updatedPerson = new Person(
            "Olivier",
            "Serra",
            "1565 Culver St",
            "Culver",
            "97451",
            "841-874-6521",
            "OlivierSerra@email.com");
        //act
        when(personService.update("Olivier", "Serra", personToUpdate)).thenReturn(updatedPerson);
        //assert
        mockMvc.perform(MockMvcRequestBuilders.put("/person/Olivier/Serra")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonInput))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Olivier"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Serra"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("1565 Culver St"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Culver"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zip").value("97451"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("841-874-6521"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("OlivierSerra@email.com"));

        verify(personService, times(1)).update("Olivier", "Serra", personToUpdate);
    }
 
    @Test
    public void testDeletePerson() throws Exception {
        //arrange
        String jsonInput = "{ \"firstName\":\"Olivier\", \"lastName\":\"Serra\", \"address\":\"1565 Culver St\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-6521\", \"email\":\"OlivierSerra@email.com\" }";
        Person deletedPerson = new Person(
            "Olivier",
            "Serra",
            "1565 Culver St",
            "Culver",
            "97451",
            "841-874-6521",
            "OlivierSerra@email.com");
        //act
        when(personService.delete("Olivier", "Serra")).thenReturn(deletedPerson);
        //assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/person/Olivier/Serra")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonInput))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Olivier"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Serra"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("1565 Culver St"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Culver"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zip").value("97451"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("841-874-6521"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("OlivierSerra@email.com"));

        verify(personService, times(1)).delete("Olivier", "Serra");
    }   
}
 