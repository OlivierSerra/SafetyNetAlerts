package com.openclassroom.SafetyNetAlerts.services;

import com.openclassroom.SafetyNetAlerts.model.Person;
import com.openclassroom.SafetyNetAlerts.repository.PersonRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Data
@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    public PersonService (PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    public List<Person> findAll() {
        return personRepository.getPersons();
    }

/*
* add person in the list
*/
    public Person save(Person person) {
        Person addedPerson = personRepository.save(person); 
        return addedPerson;
    }

/*
* used to delete a person in the list
*/
    public Person delete(String firstName, String lastName) {
        return personRepository.deletePerson(firstName, lastName);
    }

/*
* return person if criterias match  
*/       
    public Person update(String firstName, String lastName, Person PersonToUpdate) {
        return personRepository.updatePerson(firstName, lastName, PersonToUpdate);   
    }

/*
 * get one person un the list 
 */
    public Person getPerson(String firstName, String lastName) {
        return personRepository.find(firstName, lastName);
    }

/*
 * find household with Kids and what kids 
 */
    @lombok.Generated
    public List<Person> getHouseholdComposedByKids(String firstName, String lastName) {
        return personRepository.getHouseholdComposedByKids(firstName, lastName);
    }
    
/*
 * find people living at one address 
 */ 
    @lombok.Generated
    public List<Person> findOnePersonByAddress(String address) {
        return this.personRepository.findPersonsWithHisAddress(address);
    }
}

