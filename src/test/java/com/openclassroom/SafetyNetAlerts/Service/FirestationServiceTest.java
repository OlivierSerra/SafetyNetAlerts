package com.openclassroom.SafetyNetAlerts.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.openclassroom.SafetyNetAlerts.model.Firestation;
import com.openclassroom.SafetyNetAlerts.repository.FirestationRepository;
import com.openclassroom.SafetyNetAlerts.services.FirestationService;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FirestationServiceTest {
    
    @MockBean
    private FirestationRepository firestationRepository;

    @Autowired
    private FirestationService firestationService;
 
     @Test
    public void testSavefirestation() {
        //arrange    
        Firestation firestationToSave = new Firestation(
            "50 15th St", 
            "10" );
        //act    
        when(firestationRepository.saveFirestation(any(Firestation.class))).thenReturn(firestationToSave);
        Firestation savedfirestation = firestationService.save(firestationToSave);
        verify(firestationRepository, times(1)).saveFirestation(firestationToSave);
        //assert
        assertEquals(firestationToSave, savedfirestation);
    }

    @Test
    public void testDeletefirestation() {
        //arrange
        Firestation firestationToDelete = new Firestation( 
            "29 15th St", 
            "2" );
        //act-//assert
        when(firestationRepository.deleteFirestation(anyString(), anyString())).thenReturn(null);
        firestationService.delete(firestationToDelete.getAddress(), firestationToDelete.getStation());
        verify(firestationRepository, times(1)).deleteFirestation(firestationToDelete.getAddress(), firestationToDelete.getStation());
    }

    @Test
    public void testUpdatefirestation() {
        //arrangeFailed to load ApplicationContext for [WebMergedContextCo
        Firestation firestationToUpdate = new Firestation(
                "29 15th St", 
                "2");
        //act
        when(firestationRepository.updateFirestation(anyString(), anyString(), any(Firestation.class))).thenReturn(firestationToUpdate);
        Firestation updatedfirestation = firestationService.update("29 15th St", "10", firestationToUpdate);
        verify(firestationRepository, times(1)).updateFirestation("29 15th St", "10", firestationToUpdate);
        //assert
        assertEquals(firestationToUpdate, updatedfirestation);
    }

    @Test
    public void testGetfirestation() {
        // arrange
        Firestation expectedfirestation = new Firestation(
                "50 15th St", 
                "10");
        //act
        when(firestationRepository.find("29 15th St", "10")).thenReturn(expectedfirestation);
        Firestation actualfirestation = firestationService.getFirestation("29 15th St", "10");
        verify(firestationRepository, times(1)).find("29 15th St", "10");
        //assert
        assertEquals(expectedfirestation, actualfirestation);
    }
}
