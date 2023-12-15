package com.openclassroom.SafetyNetAlerts.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.SafetyNetAlerts.model.Firestation;
import com.openclassroom.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroom.SafetyNetAlerts.model.Person;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * this is used to red dats form the json file and put it on person list, firestation list, medicalRecord list
 */

@Component
public class JsonReader {
    
    List<Person> persons = new ArrayList<>();
    List<Firestation> firestations = new ArrayList<>();
    List<MedicalRecord> medicalRecords = new ArrayList<>();
    String filePath = "src/main/resources/data.json";
    Content content;
    public JsonReader(){
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(this.filePath);
        try{
            content = objectMapper.readValue(jsonFile, Content.class);
            this.persons = this.content.getPersons();
            this.firestations = this.content.getFirestations();
            this.medicalRecords = this.content.getMedicalrecords();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Person> getPersonsData(){
        return this.persons;
    }

    public List<Firestation> getFirestationsData() {
        return this.firestations;
    }

    public List<MedicalRecord> getMedicalRecordsData() {
        return this.medicalRecords;
    }
}
