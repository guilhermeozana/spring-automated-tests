package br.com.microservice.util;

import br.com.microservice.model.Person;

public class PersonCreator {

    public static Person buildPerson() {
        return Person.builder()
                .firstName("Guilherme")
                .lastName("Campos")
                .email("guilhermeozana@hotmail.com")
                .address("Rio de Janeiro, RJ, Brasil")
                .gender("Masculino")
                .build();
    }
}
