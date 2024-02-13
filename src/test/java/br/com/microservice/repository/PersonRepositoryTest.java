package br.com.microservice.repository;

import br.com.microservice.model.Person;
import br.com.microservice.util.PersonCreator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    private Person person;

    @BeforeEach
    public void setUp() {
        //Given
        person = PersonCreator.buildPerson();
    }

    @Test
    @DisplayName("Given Person Object when save then Return Saved Person")
    void testGivenPersonObject_whenSave_thenReturnSavedPerson() {

        //When
        Person savedPerson = personRepository.save(person);

        //Then
        assertNotNull(savedPerson);
        Assertions.assertTrue(savedPerson.getId() > 0);
    }

    @Test
    @DisplayName("Given Person List when findAll then Return Person List")
    void testGivenPersonList_whenFindAll_thenReturnPersonList() {
        //Given
        personRepository.save(person);

        //When
        List<Person> personList = personRepository.findAll();

        //Then
        assertNotNull(personList);
        assertEquals(1, personList.size());
    }

    @Test
    @DisplayName("Given Person Object when findById then Return Person Object")
    void testGivenPersonObject_whenFindById_thenReturnPersonObject() {
        //Given
        personRepository.save(person);

        //When
        Person savedPerson = personRepository.findById(person.getId()).get();

        //Then
        assertNotNull(savedPerson);
        assertEquals(person.getId(), savedPerson.getId());
    }

    @Test
    @DisplayName("Given Person Object when findByEmail then Return Person Object")
    void testGivenPersonObject_whenFindByEmail_thenReturnPersonObject() {
        //Given
        personRepository.save(person);

        //When
        Person savedPerson = personRepository.findByEmail(person.getEmail()).get();

        //Then
        assertNotNull(savedPerson);
        assertEquals(person.getId(), savedPerson.getId());
    }

    @Test
    @DisplayName("Given Person Object when Update then Return Updated Person Object")
    void testGivenPersonObject_whenUpdatePerson_thenReturnUpdatedPersonObject() {
        //Given
        personRepository.save(person);

        //When
        Person savedPerson = personRepository.findById(person.getId()).get();
        person.setFirstName("Teste");

        Person updatedPerson = personRepository.save(person);

        //Then
        assertNotNull(updatedPerson);
        assertEquals("Teste", updatedPerson.getFirstName());
    }

    @Test
    @DisplayName("Given Person Object when Delete then Remove Person")
    void testGivenPersonObject_whenDelete_thenRemovePersonObject() {
        //Given
        personRepository.save(person);

        //When
        personRepository.deleteById(person.getId());

        Optional<Person> personOptional = personRepository.findById(person.getId());

        //Then
        Assertions.assertTrue(personOptional.isEmpty());
    }

    @DisplayName("Test Given firstName and lastName when findByQuery then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_whenFindByQuery_thenReturnPersonObject() {

        // Given
        personRepository.save(person);

        String firstName = "Guilherme";
        String lastName = "Campos";

        // When
        Person savedPerson = personRepository.findByQuery(firstName, lastName);

        // Then
        assertNotNull(savedPerson);
        assertEquals(firstName, savedPerson.getFirstName());
        assertEquals(lastName, savedPerson.getLastName());
    }

}
