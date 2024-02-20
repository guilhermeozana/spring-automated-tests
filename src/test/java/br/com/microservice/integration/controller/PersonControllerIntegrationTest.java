package br.com.microservice.integration.controller;

import br.com.microservice.config.TestConfig;
import br.com.microservice.integration.testcontainer.AbstractIntegrationTest;
import br.com.microservice.domain.model.Person;
import br.com.microservice.util.PersonCreator;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static Person person;

    @BeforeAll
    public static void setup() {
        //Given
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        person = PersonCreator.buildPerson();
    }

    @Test
    @Order(1)
    @DisplayName("Given Person object when create then returns Person object")
    void integrationTest_givenPersonObject_whenCreate_ThenReturnsPersonObject() throws IOException {
        var content = given()
                        .spec(specification)
                        .contentType(TestConfig.CONTENT_TYPE_JSON)
                        .body(person)
                    .when()
                        .post()
                    .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        var createdPerson = objectMapper.readValue(content, Person.class);

        person = createdPerson;

        assertNotNull(createdPerson);
        assertTrue(createdPerson.getId() > 0);
        assertEquals("Guilherme", createdPerson.getFirstName());
        assertEquals("Campos", createdPerson.getLastName());
        assertEquals("Rio de Janeiro, RJ, Brasil", createdPerson.getAddress());
        assertEquals("Masculino", createdPerson.getGender());
        assertEquals("guilhermeozana@hotmail.com", createdPerson.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("Given Person Object when Update then Returns Updated Person Object")
    void integrationTest_givenPersonObject_whenUpdate_thenReturnsUpdatedPersonObject() throws IOException {

        BeanUtils.copyProperties(PersonCreator.buildUpdatedPerson(), person, "id");

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person updatedPerson = objectMapper.readValue(content, Person.class);

        person = updatedPerson;

        assertNotNull(updatedPerson);
        assertTrue(updatedPerson.getId() > 0);
        assertEquals("Teste", updatedPerson.getFirstName());
        assertEquals("teste@teste.com", updatedPerson.getEmail());
    }

    @Test
    @Order(3)
    @DisplayName("Given Person object when findById then returns Person object")
    void integrationTest_givenPersonObject_whenFindById_thenReturnsPersonObject() throws IOException {

        var content = given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person foundPerson = objectMapper.readValue(content, Person.class);

        assertNotNull(foundPerson);
        assertTrue(foundPerson.getId() > 0);
        assertEquals("Teste", foundPerson.getFirstName());
        assertEquals("Campos", foundPerson.getLastName());
        assertEquals("Rio de Janeiro, RJ, Brasil", foundPerson.getAddress());
        assertEquals("Masculino", foundPerson.getGender());
        assertEquals("teste@teste.com", foundPerson.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("Given Person Object when findAll then Returns Person List")
    void integrationTest_givenPersonObject_whenFindAll_thenReturnsPersonList() throws IOException {

        var content = given().spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person[] personArray = objectMapper.readValue(content, Person[].class);
        List<Person> personList = Arrays.asList(personArray);

        Person foundPerson = personList.get(0);

        assertNotNull(foundPerson);
        assertTrue(foundPerson.getId() > 0);
        assertEquals("Teste", foundPerson.getFirstName());
        assertEquals("Campos", foundPerson.getLastName());
        assertEquals("Rio de Janeiro, RJ, Brasil", foundPerson.getAddress());
        assertEquals("Masculino", foundPerson.getGender());
        assertEquals("teste@teste.com", foundPerson.getEmail());
    }

    @Test
    @Order(5)
    @DisplayName("Given Person object when delete then returns no content")
    void integrationTest_givenPersonObject_whenDelete_thenReturnsNoContent() throws JsonMappingException, JsonProcessingException {

        given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

}
