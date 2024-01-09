package com.openclassroom.SafetyNetAlerts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.SafetyNetAlerts.model.Firestation;
import com.openclassroom.SafetyNetAlerts.services.FirestationService;
//import org.mockito.InjectMocks;
//import org.junit.jupiter.api.*;
//import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FirestationControllerTest {

    @Mock
    private FirestationService firestationService;
    private FirestationController controller;

    @Mock
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        controller = new FirestationController(firestationService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
    
    @Test
    void testFirestationsBis() throws Exception {
    // arrange
    Firestation firestation1 = new Firestation("10 Av St James","5");
    Firestation firestation2 = new Firestation("15 av St James", "9");
    List<Firestation> firestations = Arrays.asList(firestation1, firestation2);

    // act
    when(firestationService.findAll()).thenReturn(firestations);

    // assert
    mockMvc.perform(get("/firestation/all"))
            .andExpect(status().isOk())
            .andReturn();
}

    @Test
    void testAddFirestation() throws Exception {
        //arrange
        Firestation firestation = new Firestation("11 Culver St","5");
        //act 
        when(firestationService.save(any(Firestation.class))).thenReturn(firestation);
        //assert
        mockMvc.perform(post("/firestation").
             contentType(MediaType.APPLICATION_JSON).
             content(new ObjectMapper().writeValueAsString(firestation))).
             andExpect(status().isOk()).
             andExpect(jsonPath("$.address").value("11 Culver St")).
             andExpect(jsonPath("$.station").value("5")).
             andReturn();
    }

    @Test
    void testDeleteFirestation() throws Exception {
        //arrange
        Firestation firestationToDelete = new Firestation("1509 Culver St", "3");
        //act
        when(firestationService.delete("1509 Culver St", "3")).thenReturn(firestationToDelete);
        String jsonInput = "{ \"address\":\"1509 Culver St\", \"station\":\"3\" }";
        //assert
        mockMvc.perform(delete("/firestation/{address}/{station}", "1509 Culver St", "3").
             contentType("application/json").
             content(jsonInput)).
             andExpect(status().isOk()).
             andExpect(jsonPath("$.address").value("1509 Culver St")).
             andExpect(jsonPath("$.station").value("3")).
             andReturn();
    }

    @Test
    void testUpdateFirestation() throws Exception {
        //arrange
        Firestation firestationToUpdate = new Firestation("1509 Culver St", "5");
        Firestation updatedFirestation = new Firestation("1509 Culver St", "9");
        //act
        when(firestationService.update("1509 Culver St", "5", firestationToUpdate)).thenReturn(updatedFirestation);
        String jsonInput = new ObjectMapper().writeValueAsString(firestationToUpdate);
        //assert
        mockMvc.perform(put("/firestation/{address}/{station}", "1509 Culver St", "5").
             contentType("application/json").
             content(jsonInput)).
             andExpect(status().isOk()).
             andExpect(jsonPath("$.address").value("1509 Culver St")).
             andExpect(jsonPath("$.station").value("9")).
             andReturn();
    }
/*
    @Test
    public void addfirestation() {
        String address = "abc";
        Firestation firestation = new Firestation();
        Firestation expected = new Firestation();
        Firestation actual = controller.addfirestation(address, firestation);

        assertEquals(expected, actual);
    }
     */

}
