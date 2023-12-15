package com.openclassroom.SafetyNetAlerts.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.openclassroom.SafetyNetAlerts.model.Firestation;
import com.openclassroom.SafetyNetAlerts.utility.JsonReader;

//import lombok.Generated;
//import lombok.EqualsAndHashCode.Exclude;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Repository
public class FirestationRepository {
/*
 * this is the jsonReader to inject info in the firestation list 
 */
    
    @Autowired
    
    MedicalRecordRepository medicalRecordRepository;

    public List<Firestation> firestations;
    private JsonReader jsonDataReader; 

    public FirestationRepository(JsonReader jsonDataReader) throws Exception {
        this.jsonDataReader = jsonDataReader;
        this.firestations = this.jsonDataReader.getFirestationsData();
    }
/*
 * this is used to have all the firestion
 */
    public List<Firestation> getFirestations() {
        return firestations;
}

/* 
 * This is used to find one firestation from the list of firestation
 */    
    public Firestation find(String address, String station) {  
        for (Firestation firestation : firestations) {
            if (firestation.getAddress().equals(address) && firestation.getStation().equals(station)) {
                return firestation; 
        }
    }
    return null; 
    }

/*
 * this is used to save one foirestation in the list of firestatiuon
 */
    public Firestation saveFirestation(Firestation firestation) {
        for (Firestation existingFirestation : firestations) {
            if (existingFirestation.getAddress().equals(firestation.getAddress()) && existingFirestation.getStation().equals(firestation.getStation())) {
                return existingFirestation;
            }
        }
        firestations.add(firestation); 
        return firestation;
    }
       
    /*
    * This is used to update info for one firestation in the Firestation list 
    */
    public Firestation updateFirestation(String address, String station, Firestation FirestationToUpdate) {
        Firestation firestationFound = find(address, station);
            if (firestationFound != null) {
                firestationFound.setStation(FirestationToUpdate.getStation());
                    return saveFirestation(firestationFound);
        }
        return null;
    }

    
/*
 * This is used to delete a firestation from the firestation list 
 */
    public Firestation deleteFirestation(String address, String station) {
        Firestation firestationToDelete = find(address, station);
            if (firestationToDelete != null) {
                firestations.remove(firestationToDelete);
                    return firestationToDelete;
        }
        return null;
    }
    @lombok.Generated
    public List<String> findAddressesByFirestation(String station) {
    List<String> addresses = new ArrayList<>();
        for (Firestation firestation : this.firestations) {
            if (firestation.getStation() == station) {
                addresses.add(firestation.getAddress());
            }
        }
    return addresses;
    }
    
    @lombok.Generated
    public String findStationByAddress(String address) {
        for (Firestation firestation : this.firestations) {
            if (Objects.equals(firestation.getAddress(), address)) {
                return firestation.getStation();
            }
        }
        // Aucune station trouvée pour l'adresse donnée
        return null;
    }
}
