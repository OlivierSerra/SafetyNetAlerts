package com.openclassroom.SafetyNetAlerts.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroom.SafetyNetAlerts.services.MedicalRecordService;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordControllerTest {

    @Mock
    private MedicalRecordService medicalRecordService;
    private MedicalRecordController controller;

    @Mock
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        controller = new MedicalRecordController(medicalRecordService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testAddMedicalRecord() throws Exception {
        // arrange
        MedicalRecord medicalRecordToAdd = new MedicalRecord("JohnWhoo", "Boydo", "03/06/1984", Arrays.asList("aznol:350mg", "hydrapermazol:100mg"), Arrays.asList("nillacilan"));
        MedicalRecord savedMedicalRecord = new MedicalRecord("JohnWhoo", "Boydo", "03/06/1984", Arrays.asList("aznol:350mg", "hydrapermazol:100mg"), Arrays.asList("nillacilan"));
        //act
        when(medicalRecordService.save(medicalRecordToAdd)).thenReturn(savedMedicalRecord);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInput = objectMapper.writeValueAsString(medicalRecordToAdd);
        //assert
        mockMvc.perform(post("/medicalRecord")
                .contentType("application/json")
                .content(jsonInput))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("JohnWhoo"))
                .andExpect(jsonPath("$.lastName").value("Boydo"))
                .andExpect(jsonPath("$.birthdate").value("03/06/1984"))
                .andExpect(jsonPath("$.medications[0]").value("aznol:350mg"))
                .andExpect(jsonPath("$.medications[1]").value("hydrapermazol:100mg"))
                .andExpect(jsonPath("$.allergies[0]").value("nillacilan"))
                .andReturn();
    }
   
    @Test
    void testMedicalRecords() throws Exception {
        //arrange
        MedicalRecord medicalRecordToFind1  = new MedicalRecord("JohnWhoo", "Boydo", "03/06/1984", Arrays.asList("aznol:350mg", "hydrapermazol:100mg"), Arrays.asList("nillacilan"));
        MedicalRecord medicalRecordToFind2  = new MedicalRecord("Olivier", "Serra", "21/10/1975", Arrays.asList("aznol:350mg", "hydrapermazol:100mg"), Arrays.asList("nillacilan"));
        List<MedicalRecord> medicalRecords = Arrays.asList(medicalRecordToFind1, medicalRecordToFind2);
        //act
        when(medicalRecordService.findAll()).thenReturn(medicalRecords);
        //assert
        mockMvc.perform(get("/medicalRecord"))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    void testDeleteMedicalRecord() throws Exception {
        //arrange
        MedicalRecord medicalRecordToDelete = new MedicalRecord("JohnWhoo", "Boydo", "03/06/1984",
            Arrays.asList("aznol:350mg", "hydrapermazol:100mg"), Arrays.asList("nillacilan"));
        //act
        when(medicalRecordService.delete("JohnWhoo", "Boydo")).thenReturn(medicalRecordToDelete);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInput = objectMapper.writeValueAsString(medicalRecordToDelete);
        //assert
        mockMvc.perform(delete("/medicalRecord/{firstName}/{lastName}", "JohnWhoo", "Boydo")
            .contentType("application/json")
            .content(jsonInput))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("JohnWhoo"))
                .andExpect(jsonPath("$.lastName").value("Boydo"))
                .andExpect(jsonPath("$.birthdate").value("03/06/1984"))
                .andExpect(jsonPath("$.medications[0]").value("aznol:350mg"))
                .andExpect(jsonPath("$.medications[1]").value("hydrapermazol:100mg"))
                .andExpect(jsonPath("$.allergies[0]").value("nillacilan"))
                .andReturn();
    }

    @Test
    void testUpdateMedicalRecord() throws Exception {
        //arrange
        MedicalRecord medicalRecordToUpdate = new MedicalRecord(
            "JohnWhoo",
            "Boydo",
            "03/06/1984",
            Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
            Arrays.asList("nillacilan"));
        MedicalRecord updatedMedicalRecord = new MedicalRecord(
            "JohnWhoo",
            "Boydo",
            "03/06/1985",
            Arrays.asList("aznol:350mg", "hydrapermazol:100mg"), 
            Arrays.asList("nillacilan"));
        //act
        when(medicalRecordService.update(
            "JohnWhoo",
            "Boydo",
            medicalRecordToUpdate)).thenReturn(updatedMedicalRecord);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInput = objectMapper.writeValueAsString(medicalRecordToUpdate);
        //assert 
        mockMvc.perform(put("/medicalRecord/{firstName}/{lastName}", "JohnWhoo", "Boydo")
                .contentType("application/json")
                .content(jsonInput))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("JohnWhoo"))
                .andExpect(jsonPath("$.lastName").value("Boydo"))
                .andExpect(jsonPath("$.birthdate").value("03/06/1985"))
                .andExpect(jsonPath("$.medications[0]").value("aznol:350mg"))
                .andExpect(jsonPath("$.medications[1]").value("hydrapermazol:100mg"))
                .andExpect(jsonPath("$.allergies[0]").value("nillacilan"))
                .andReturn();
    }             
}