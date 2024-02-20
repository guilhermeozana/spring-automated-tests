package br.com.microservice.controller;

import br.com.microservice.exception.ResourceNotFoundException;
import br.com.microservice.domain.model.Person;
import br.com.microservice.service.PersonService;
import br.com.microservice.util.PersonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    private Person person;

    @BeforeEach
    public void setup() {
        //Given
        person = PersonCreator.buildPerson();
    }

    @Test
    @DisplayName("Given Person Object when create then Return Saved Person")
    void testGivenPersonObject_whenCreate_thenReturnSavedPerson() throws Exception {
        // Given
        given(personService.create(any(Person.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // When
        ResultActions response = mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)));

        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @Test
    @DisplayName("Given Person List when findAll then Return Person List")
    void testGivenPersonList_whenFindAll_thenReturnPersonList() throws Exception {
        // Given
        List<Person> personList = List.of(person);

        given(personService.findAll()).willReturn(personList);

        // When
        ResultActions response = mockMvc.perform(get("/person"));

        // Then
        response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(personList.size())));
    }

    @Test
    @DisplayName("Given personId when findById then Return Person Object")
    void testGivenPersonId_WhenFindById_thenReturnPersonObject() throws Exception {

        // Given / Arrange
        long personId = 1L;
        given(personService.findById(personId)).willReturn(person);

        // When / Act
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));

        // Then / Assert
        response.
                andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @Test
    @DisplayName("Given Invalid PersonId when findById then Return Not Found")
    void testGivenInvalidPersonId_WhenFindById_thenReturnNotFound() throws Exception {

        // Given
        long personId = 1L;
        given(personService.findById(personId)).willThrow(ResourceNotFoundException.class);

        // When
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));

        // Then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("JUnit test for Given Updated Person when Update then Return Updated Person Object")
    void testGivenUpdatedPerson_WhenUpdate_thenReturnUpdatedPersonObject() throws JsonProcessingException, Exception {

        // Given / Arrange
        long personId = 1L;
        given(personService.findById(personId)).willReturn(person);
        given(personService.update(any(Person.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // When / Act
        Person updatedPerson = PersonCreator.buildUpdatedPerson();

        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPerson)));

        // Then / Assert
        response.
                andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedPerson.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedPerson.getEmail())));
    }

    @Test
    @DisplayName("Given Nonexistent Person when Update then Return Not Found")
    void testGivenNonexistentPerson_WhenUpdate_thenReturnNotFound() throws JsonProcessingException, Exception {

        // Given / Arrange
        long personId = 1L;
        given(personService.findById(personId)).willThrow(ResourceNotFoundException.class);
        given(personService.update(any(Person.class)))
                .willAnswer((invocation) -> invocation.getArgument(1));

        // When
        Person updatedPerson = PersonCreator.buildUpdatedPerson();

        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPerson)));

        // Then
        response.
                andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Given personId when Delete then Return No Content")
    void testGivenPersonId_WhenDelete_thenReturnNotContent() throws Exception {

        // Given
        long personId = 1L;
        willDoNothing().given(personService).delete(personId);

        // When
        ResultActions response = mockMvc.perform(delete("/person/{id}", personId));

        // Then
        response.
                andExpect(status().isNoContent())
                .andDo(print());
    }

}
