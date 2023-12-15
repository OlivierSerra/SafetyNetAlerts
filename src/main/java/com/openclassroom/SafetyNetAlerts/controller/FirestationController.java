package com.openclassroom.SafetyNetAlerts.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.openclassroom.SafetyNetAlerts.model.Firestation;
import com.openclassroom.SafetyNetAlerts.services.FirestationService;
import org.apache.logging.log4j.Logger;
import static org.apache.logging.log4j.LogManager.getLogger;

@RestController
public class FirestationController {

    @Autowired
    private FirestationService firestationService;
    private static final Logger logger = getLogger(FirestationController.class);
    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

/*
 * This method is used to put all the firestation in a list
*/
    @GetMapping("/firestation") 
    public List<Firestation>firestations() {
        logger.info("getting all firestations");
        return firestationService.findAll();
    }
 
/*  
 * This method is used to add one firestation in the list
*/
    @PostMapping("/firestation")
    public Firestation addfirestation(@RequestBody Firestation firestation) {
        logger.info("firestation added successfully");
        return firestationService.save(firestation);
    }

/*
* This method is used to delete one firestation inside the list
*/ 
    @DeleteMapping("/firestation/{address}/{station}")
    public Firestation deletefirestation(
        @PathVariable("address") String address,
        @PathVariable("station") String station,
        @RequestBody Firestation firestation) {   
            logger.info("firestation deleted successfully");
            return firestationService.delete(address, station);
    }

/*
* This method is used to update info for one firestation in the list
*/      
    @PutMapping("/firestation/{address}/{station}")
    public Firestation updatefirestation(
        @PathVariable("address") String address,
        @PathVariable("station") String station,
        @RequestBody Firestation FirestationToUpdate)  {
            logger.info("firestation updated successfully");
            return firestationService.update(address, station, FirestationToUpdate);        
    }
}






    