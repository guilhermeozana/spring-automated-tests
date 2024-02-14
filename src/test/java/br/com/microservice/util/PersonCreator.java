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

    public static Person buildUpdatedPerson() {
        return Person.builder()
                .firstName("Teste")
                .lastName("Campos")
                .email("teste@teste.com")
                .address("Rio de Janeiro, RJ, Brasil")
                .gender("Masculino")
                .build();
    }
}
