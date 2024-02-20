package br.com.microservice.domain.dto;

import br.com.microservice.domain.enums.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
