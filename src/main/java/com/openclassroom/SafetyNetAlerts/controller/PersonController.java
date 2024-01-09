package com.openclassroom.SafetyNetAlerts.controller;

import java.util.List;
//import java.util.Optional;
import org.apache.logging.log4j.Logger;
import static org.apache.logging.log4j.LogManager.getLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.openclassroom.SafetyNetAlerts.services.PersonService;
import com.openclassroom.SafetyNetAlerts.model.Person;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;
    private static final Logger logger = getLogger(PersonController.class);

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

/** This method is used to put all the people in the list *****/
@GetMapping("/person") 
public List<Person>persons() {
    logger.info("getting all persons");
    return personService.findAll();
}
 
/*  
* This method is used to add one person in the people list
*/
@PostMapping("/person")
public Person addPerson(@RequestBody Person person) {
    logger.info("person added successfully");
    return personService.save(person);
}

/*
* This method is used to delete one person in the list
*/ 
@DeleteMapping("/person/{firstName}/{lastName}")
public Person deletePerson(
    @PathVariable("firstName") String firstName,
    @PathVariable("lastName") String lastName,
    @RequestBody Person person) {   
        logger.info("person deleted successfully");
        return personService.delete(firstName, lastName);
}

/*
* This method is used to update info for one person in the list
*/      
@PutMapping("/person/{firstName}/{lastName}")
public Person updatePerson(
    @PathVariable("firstName") String firstName,
    @PathVariable("lastName") String lastName,
    @RequestBody Person PersonToUpdate) {
        logger.info("person updated successfully");
        return personService.update(firstName, lastName, PersonToUpdate);        
    }
}






    