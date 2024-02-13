package br.com.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.microservice.model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);

    // Define custom query using JPQL with named parameters
    @Query("select p from Person p where p.firstName = :firstName and p.lastName = :lastName")
    Person findByQuery(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // Define custom query using Native SQL with named parameters
//    @Query(value = "select * from person p where p.first_name = :firstName and p.last_name = :lastName", nativeQuery = true)
//    Person findByQuery(@Param("firstName") String firstName, @Param("lastName") String lastName);
}