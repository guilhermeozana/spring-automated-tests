package br.com.microservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

import br.com.microservice.exception.ResourceNotFoundException;
import br.com.microservice.model.Person;
import br.com.microservice.repository.PersonRepository;
import br.com.microservice.util.PersonCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository repository;
    
    @InjectMocks
    private PersonService services;

    private Person person;
    
    @BeforeEach
    public void setup() {
        //Given
        person = PersonCreator.buildPerson();
    }
    
    @DisplayName("Test Given Person Object when Save Person then Return Person Object")
    @Test
    void testGivenPersonObject_WhenSavePerson_thenReturnPersonObject() {
        
        // Given / Arrange
        given(repository.findByEmail(anyString())).willReturn(Optional.empty());
        given(repository.save(person)).willReturn(person);
        
        // When / Act
        Person savedPerson = services.create(person);
        
        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals("Guilherme", savedPerson.getFirstName());
    }

    @DisplayName("Test Given Existing Email when Save Person then throws Exception")
    @Test
    void testGivenExistingEmail_WhenSavePerson_thenThrowsException() {

        // Given / Arrange
        given(repository.findByEmail(anyString())).willReturn(Optional.of(person));

        // When / Act
        assertThrows(ResourceNotFoundException.class, () -> {
            services.create(person);
        });

        // Then / Assert
        verify(repository, never()).save(any(Person.class));
    }

    @DisplayName("Test Given Persons List when findAll Persons then Return Persons List")
    @Test
    void testGivenPersonsList_WhenFindAllPersons_thenReturnPersonsList() {

        // Given / Arrange
        Person person1 = PersonCreator.buildPerson();

        given(repository.findAll()).willReturn(List.of(person, person1));

        // When / Act
        List<Person> personsList = services.findAll();

        // Then / Assert
        assertNotNull(personsList);
        assertEquals(2, personsList.size());
    }

    @DisplayName("Test Given Empty Person List when findAll Person then Return Empty Person List")
    @Test
    void testGivenEmptyPersonList_WhenFindAllPerson_thenReturnEmptyPersonList() {

        // Given / Arrange
        given(repository.findAll()).willReturn(Collections.emptyList());

        // When / Act
        List<Person> personList = services.findAll();

        // Then / Assert
        assertTrue(personList.isEmpty());
        assertEquals(0, personList.size());
    }

    @DisplayName("Test Given PersonId when findById then Return Person Object")
    @Test
    void testGivenPersonId_WhenFindById_thenReturnPersonObject() {

        // Given / Arrange
        given(repository.findById(anyLong())).willReturn(Optional.of(person));

        // When / Act
        Person savedPerson = services.findById(1L);

        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals("Guilherme", savedPerson.getFirstName());
    }

    @DisplayName("Test Given Person Object when Update Person then Return Updated Person Object")
    @Test
    void testGivenPersonObject_WhenUpdatePerson_thenReturnUpdatedPersonObject() {

        // Given / Arrange
        person.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person));

        person.setFirstName("Teste");
        person.setEmail("teste@teste.com.br");

        given(repository.save(person)).willReturn(person);

        // When / Act
        Person updatedPerson = services.update(person);

        // Then / Assert
        assertNotNull(updatedPerson);
        assertEquals("Teste", updatedPerson.getFirstName());
        assertEquals("teste@teste.com.br", updatedPerson.getEmail());
    }

    @DisplayName("Test Given PersonID when Delete Person then do Nothing")
    @Test
    void testGivenPersonID_WhenDeletePerson_thenDoNothing() {

        // Given / Arrange
        person.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person));
        willDoNothing().given(repository).delete(person);

        // When / Act
        services.delete(person.getId());

        // Then / Assert
        verify(repository, times(1)).delete(person);
    }
}
