package com.openclassroom.SafetyNetAlerts.services;

import com.openclassroom.SafetyNetAlerts.model.Firestation;
import com.openclassroom.SafetyNetAlerts.repository.FirestationRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Data
@Service
public class FirestationService {
    @Autowired
    private FirestationRepository firestationRepository;
    public FirestationService (FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
    }
    public List<Firestation> findAll() {
        return firestationRepository.getFirestations();
    }

/*
* add firestation in the list
*/
    public Firestation save(Firestation firestation ) {
        Firestation addedFirestation = firestationRepository.saveFirestation(firestation); 
        return addedFirestation;
    }

/*
* This is used to delete a firestation from the list
*/
    public Firestation delete(String address, String station) {
        return firestationRepository.deleteFirestation(address, station);
    }

/*
* this is used to find a firestation in the list  
*/       
    public Firestation update(String address, String station, Firestation firestationToUpdate) {
        return firestationRepository.updateFirestation(address, station, firestationToUpdate);   
    }

    public Firestation getFirestation (String address, String station) {
        return firestationRepository.find(address, station);
    }

    @lombok.Generated
    public List<String> findFirestationByAdress(String station) {
        return this.firestationRepository.findAddressesByFirestation(station);
    }

    @lombok.Generated
    public String findStationByAddress(String address) {
        return this.firestationRepository.findStationByAddress(address);
    }
}

