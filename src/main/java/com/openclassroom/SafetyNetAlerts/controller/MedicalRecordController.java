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
import com.openclassroom.SafetyNetAlerts.services.MedicalRecordService;
import com.openclassroom.SafetyNetAlerts.model.MedicalRecord;
import org.apache.logging.log4j.Logger;
import static org.apache.logging.log4j.LogManager.getLogger;

@RestController
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;
    private static final Logger logger = getLogger(MedicalRecordController.class);

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

/*
 * This method is used to put all the medicalRecord in the list 
 */
    @GetMapping("/medicalRecord") 
    public List<MedicalRecord>medicalRecords() {
        logger.info("getting all medical records");
        return medicalRecordService.findAll();
    }
 
/*  
* This méthod is used to add one medicalRecord on the list
*/
    @PostMapping("/medicalRecord")
    public MedicalRecord addmedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("medical record added successfully");
        return medicalRecordService.save(medicalRecord);
    }

/*
* This method is used to delete one medicalRecord in the list
*/ 
    @DeleteMapping("/medicalRecord/{firstName}/{lastName}")
    public MedicalRecord deletemedicalRecord(
        @PathVariable("firstName") String firstName,
        @PathVariable("lastName") String lastName,
        @RequestBody MedicalRecord medicalRecord) { 
            logger.info("medical record delete successfully");
            return medicalRecordService.delete(firstName, lastName);
    }

/*
* This method is used to update info for one medicalRecord in the list
*/      
    @PutMapping("/medicalRecord/{firstName}/{lastName}")
    public MedicalRecord updatemedicalRecord(
        @PathVariable("firstName") String firstName,
        @PathVariable("lastName") String lastName,
        @RequestBody MedicalRecord medicalRecordToUpdate)  {
            logger.info("medical records updated successfully");
            return medicalRecordService.update(firstName, lastName, medicalRecordToUpdate);        
        }
}





    