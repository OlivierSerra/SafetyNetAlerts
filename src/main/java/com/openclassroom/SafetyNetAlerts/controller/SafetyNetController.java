package com.openclassroom.SafetyNetAlerts.controller;

import com.openclassroom.SafetyNetAlerts.model.Firestation;
import com.openclassroom.SafetyNetAlerts.model.Household;
import com.openclassroom.SafetyNetAlerts.model.Person;
import com.openclassroom.SafetyNetAlerts.services.FirestationService;
import com.openclassroom.SafetyNetAlerts.services.MedicalRecordService;
import com.openclassroom.SafetyNetAlerts.services.PersonService;
import com.openclassroom.SafetyNetAlerts.utility.CrossingPerson_MedicalDatas;
import com.openclassroom.SafetyNetAlerts.utility.IdentifyKids;
import com.openclassroom.SafetyNetAlerts.utility.KidsLivingInThisHouse;
import com.openclassroom.SafetyNetAlerts.utility.MedicalRecordData;
import com.openclassroom.SafetyNetAlerts.utility.ParentsAndKidsTotal;
import com.openclassroom.SafetyNetAlerts.utility.PersonAge;
import com.openclassroom.SafetyNetAlerts.utility.PersonAndFirestationLink;
import com.openclassroom.SafetyNetAlerts.utility.PersonPhone;

import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import static org.apache.logging.log4j.LogManager.getLogger;
//import org.slf4j.LoggerFactory;

@RestController
public class SafetyNetController {
    private static final Logger logger = getLogger(MedicalRecordController.class);
    private static final Logger logger2 = getLogger(FirestationController.class);
    private static final Logger logger3 = getLogger(PersonController.class);
    //@Autowired
    private PersonService personService;
    //@Autowired
    private FirestationService firestationService;
    //@Autowired
    private MedicalRecordService medicalRecordService;

    public SafetyNetController(PersonService personService, FirestationService firestationService, MedicalRecordService medicalRecordService){
        this.personService = personService;
        this.firestationService = firestationService;
        this.medicalRecordService = medicalRecordService;
    }

@ResponseBody
@GetMapping("/firestation/{station}")
public ResponseEntity<PersonAndFirestationLink> getPersonsLinkedToStation(@RequestParam(value = "station") String station){
    try {
        logger2.info("Attempting to get persons linked to station ", station);    
        List<String> station_Addresses = this.firestationService.findFirestationByAdress(station);
        List<PersonPhone> personsPhoneList = new ArrayList<>();
        HashMap<String, Integer> ParentsAndkidsList = new HashMap<>();
        if (station_Addresses.size() > 0) {
            List<Person> allPersons = this.personService.findAll();
                for (Person person : allPersons) {
                    for (String address : station_Addresses) {
                        if (Objects.equals(person.getAddress(), address)) {
                            personsPhoneList.add(new PersonPhone(person.getFirstName(), person.getLastName(), person.getPhone()));
                                break; // Sortir de la boucle interne une fois que la correspondance est trouvée
                                }
                            }
                        }
            ParentsAndkidsList = ParentsAndKidsTotal.parentsAndkidsNumber(personsPhoneList , this.medicalRecordService.findAll());
        }
        else {
            logger2.warn("could not get persons who are attached to station " + station);
            return ResponseEntity.noContent().build();
        }
            PersonAndFirestationLink result = new PersonAndFirestationLink(personsPhoneList, ParentsAndkidsList);
            logger2.info("getting all persons attached to firestation number");
            return ResponseEntity.ok().body(result);   
    } catch (Exception e) {
            logger2.error("An error occured while the request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

@ResponseBody
@GetMapping("/childAlert/{address}")
    public ResponseEntity<List<KidsLivingInThisHouse>> getkidsLivingAtThisAddress(@RequestParam(value = "address") String address){
    //get all people living at this address
    List<Person> peopleLivingAtAddress = this.personService.findOnePersonByAddress(address);
    if(peopleLivingAtAddress.isEmpty()){
        logger.error("could not find anyone living at this address: " + address);
        return ResponseEntity.noContent().build();
    }
    //get all kids out of this list with their age
    List<PersonAge> kids = IdentifyKids.associateKidsWithParents(peopleLivingAtAddress, this.medicalRecordService.findAll(), new ArrayList<>());
    //get all kids with their household
    List<KidsLivingInThisHouse> kidsWithAge = new ArrayList<>();
        for (PersonAge kid : kids) {
            String firstName = kid.getFirstName();
            String lastName = kid.getLastName();
            Long age = kid.getAge();
            Household household = (Household) personService.getHouseholdComposedByKids(firstName, lastName);
        KidsLivingInThisHouse KidsintheCouple = new KidsLivingInThisHouse(lastName, lastName, age, household);
            KidsintheCouple.setFirstname(firstName);
            KidsintheCouple.setLastname(lastName);
            KidsintheCouple.setAge(age);
            KidsintheCouple.setHousehold(household);
            kidsWithAge.add(KidsintheCouple);
        }
    logger.info("getting all kids and their household attached to address");
    return ResponseEntity.ok().body(kidsWithAge);
}

@ResponseBody
@GetMapping("/phoneAlert/{station}")
public ResponseEntity<List<String>> getPhoneNumbersLinkedToStationNumber(@RequestParam(value="station") String station){
    List<String> firestationAddresses = this.firestationService.findFirestationByAdress(station);
        if(firestationAddresses.isEmpty()){
            logger.error("could not find anyone attached to this firestation number: " + station);
            return ResponseEntity.noContent().build();
        }
    List<String> phoneNumbersList = new ArrayList<>();
        for (Person person : this.personService.findAll()) {
            for (String address : firestationAddresses) {
                if (Objects.equals(person.getAddress(), address)) {
                    phoneNumbersList.add(person.getPhone());
                    break; // Stop searching for this person once found
                }
            }
        }
    logger3.info("getting all phone Numbers attached to this firestation");
    return ResponseEntity.ok().body(phoneNumbersList);
}
        
@ResponseBody
@GetMapping("/fire/{address}")
public ResponseEntity<HashMap<String, List<MedicalRecordData>>> getAllThePersonsAtThisAddress(@RequestParam(value="address") String address) {
    List<Person> personList = this.personService.findOnePersonByAddress(address);
        if (personList.isEmpty()) {
            logger.error("could not find anyone living at this address: " + address);
            return ResponseEntity.noContent().build();
        }
    String stationNumber = this.firestationService.findStationByAddress(address);
    HashMap<String, List<MedicalRecordData>> result = new HashMap<>();
    List<MedicalRecordData> medicalRecordDatas = new ArrayList<>();
        for (Person person : personList) {
            String firstName = person.getFirstName();
            String lastName = person.getLastName();
            String phone = person.getPhone();
            String birthdayString = this.medicalRecordService.getBirthdayFromMedicalRecord(firstName, lastName);
            LocalDate birthday = LocalDate.parse(birthdayString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            long age = ParentsAndKidsTotal.calculateAge(birthday.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            List<String> medications = this.medicalRecordService.getTreatmentFromMedicalRecord(firstName, lastName);
            List<String> allergies = this.medicalRecordService.getIllnessFromMedicalRecord(firstName, lastName);
            MedicalRecordData medicalRecordData = new MedicalRecordData(firstName, lastName, phone, age, medications, allergies);
            medicalRecordDatas.add(medicalRecordData);
        }
    result.put(stationNumber, medicalRecordDatas);
    logger.info("getting people living at address");
    return ResponseEntity.ok().body(result);
}
    
@ResponseBody
@GetMapping("/flood/stations")
public ResponseEntity<HashMap<String, List<MedicalRecordData>>> getHouseholdLinkedToFirestation(@RequestParam("stations") List<String> stationNumbers) {
    List<String> allAddresses = new ArrayList<>();
        for (Firestation firestation : this.firestationService.findAll()) {
            for ( String number : stationNumbers) {
                if (Objects.equals(firestation.getStation(), number)) {
                    allAddresses.add(firestation.getAddress());
                    break; 
                }
            }
        }
        if (allAddresses.isEmpty()) {
            logger.error("could not find anyone household attached to this list of firestation numbers: " + stationNumbers);
            return ResponseEntity.noContent().build();
        }
    HashMap<String, List<MedicalRecordData>> result = new HashMap<>();
        for (String address : allAddresses) {
            List<MedicalRecordData> persons = new ArrayList<>();
            for (Person person : this.personService.findOnePersonByAddress(address)) {
                MedicalRecordData medicalRecordData = new MedicalRecordData(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getPhone(),
                        ParentsAndKidsTotal.calculateAge(
                                this.medicalRecordService.getBirthdayFromMedicalRecord(
                                        person.getFirstName(), person.getLastName()
                                )
                        ),
                        this.medicalRecordService.getIllnessFromMedicalRecord(
                                person.getFirstName(), person.getLastName()
                        ),
                        this.medicalRecordService.getIllnessFromMedicalRecord(
                                person.getFirstName(), person.getLastName()
                        )
                );
                persons.add(medicalRecordData);
            }
            result.put(address, persons);
        }
    logger.info("getting household attached to firestation");
    return ResponseEntity.ok().body(result);
}

@ResponseBody
@GetMapping("/personInfo")
public ResponseEntity<List<CrossingPerson_MedicalDatas>> getListOfPersons(
        @RequestParam(value="firstName") String firstName,
        @RequestParam(value="lastName") String lastName) {
    List<CrossingPerson_MedicalDatas> persons = new ArrayList<>();
        for (Person person : this.personService.findAll()) {
            if (Objects.equals(person.getFirstName(), firstName) && Objects.equals(person.getLastName(), lastName)) {
                CrossingPerson_MedicalDatas personData = new CrossingPerson_MedicalDatas(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        ParentsAndKidsTotal.calculateAge(
                                this.medicalRecordService.getBirthdayFromMedicalRecord(
                                        person.getFirstName(), person.getLastName()
                                )
                        ),
                        person.getEmail(),
                        this.medicalRecordService.getTreatmentFromMedicalRecord(
                                person.getFirstName(), person.getLastName()
                        ),
                        this.medicalRecordService.getIllnessFromMedicalRecord(
                                person.getFirstName(), person.getLastName()
                        )
                );
                persons.add(personData);
            }
        }
        if (persons.isEmpty()) {
            logger3.error("could not get a list of persons with this firstname and lastname");
            return ResponseEntity.noContent().build();
        }
    logger3.info("getting all persons info by first and last names");
    return ResponseEntity.ok().body(persons);
}
    
@ResponseBody
@GetMapping("/communityEmail")
public ResponseEntity<List<String>> getAllTheEmailFromTheCity(@RequestParam(value="city") String city) {
    List<String> mailingList = new ArrayList<>();
        for (Person person : this.personService.findAll()) {
            if (Objects.equals(person.getCity(), city) && !mailingList.contains(person.getEmail())) {
                mailingList.add(person.getEmail());
            }
        }
        if (mailingList.isEmpty()) {
            logger3.error("could not get a list of persons living in this city: " + city);
            return ResponseEntity.noContent().build();
        }
    logger3.info("getting emails of all persons living at: " + city);
    return ResponseEntity.ok().body(mailingList);
    }
}

