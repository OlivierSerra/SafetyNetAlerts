package com.openclassroom.SafetyNetAlerts.repository;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.openclassroom.SafetyNetAlerts.model.Person;
import com.openclassroom.SafetyNetAlerts.utility.JsonReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Repository
public class PersonRepository {

    public List<Person> persons;
    private JsonReader jsonDataReader; 
    public PersonRepository(JsonReader jsonDataReader) throws Exception {
        this.jsonDataReader = jsonDataReader;
        this.persons = this.jsonDataReader.getPersonsData();
    }
    public List<Person> getPersons() {
        return persons;
    }

/* 
 * These are used to find one Person in the list 
 */    
    public Person find(String firstName, String lastName) {  
        for (Person person : persons) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                return person; 
            }
        }
        return null; 
    }

/*
 * This is used to save a person in the list
 */
    public Person save(Person person) {
        for (Person existingPerson : persons) {
            if (existingPerson.getFirstName().equals(person.getFirstName()) && existingPerson.getLastName().equals(person.getLastName())) {
                return existingPerson;
            }
        }
        persons.add(person); 
        return person;
    }
       
    /*
    * This is used to update a person in the list
    */
    public Person updatePerson(String firstName, String lastName, Person personToUpdate) {
        Person personFound = find(firstName, lastName);
        if (personFound != null) {
            personFound.setAddress(personToUpdate.getAddress());
            personFound.setCity(personToUpdate.getCity());
            personFound.setZip(personToUpdate.getZip());
            personFound.setPhone(personToUpdate.getPhone());
            personFound.setEmail(personToUpdate.getEmail());
            return save(personFound);
        }
        return null;
    }

/*
 * This is used to delete one person from the person list 
 */
    public Person deletePerson(String firstName, String lastName) {
        Person personToDelete = find(firstName, lastName);
        if (personToDelete != null) {
            persons.remove(personToDelete);
            return personToDelete;
        }
        return null;
    }

    @lombok.Generated
    public Person findPersonByFirstnameAndLastname(String firstname, String lastname) {
        for (Person person : this.persons) {
            if (Objects.equals(person.getFirstName(), firstname) &&
                    Objects.equals(person.getLastName(), lastname)) {
                return person;
            }
        }
        return null;
    }

    @lombok.Generated
    public List<Person> getHouseholdComposedByKids(String firstname, String lastname) {
        Person child = findPersonByFirstnameAndLastname(firstname, lastname);
        List<Person> household = new ArrayList<>();
        for (Person person : this.persons) {
            if (Objects.equals(person.getLastName(), child.getLastName()) &&
                    Objects.equals(person.getAddress(), child.getAddress()) &&
                    !Objects.equals(person.getFirstName(), child.getFirstName())) {
                household.add(person);
            }
        }
        return household;
    }

    @lombok.Generated
    public List<Person> findPersonsWithHisAddress(String address) {
        List<Person> matchingPersons = new ArrayList<>();
        for (Person person : this.persons) {
            if (Objects.equals(person.getAddress(), address)) {
                matchingPersons.add(person);
            }
        }    
        return matchingPersons;
    }
}



