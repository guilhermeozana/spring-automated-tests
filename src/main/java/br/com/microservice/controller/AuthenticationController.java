package br.com.microservice.controller;

import br.com.microservice.domain.dto.AuthenticationDTO;
import br.com.microservice.domain.dto.LoginResponseDTO;
import br.com.microservice.domain.dto.RegisterDTO;
import br.com.microservice.service.AuthenticationService;
import br.com.microservice.service.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        LoginResponseDTO login = this.authenticationService.login(authenticationDTO);

        return ResponseEntity.ok(login);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerDTO) {
        this.authenticationService.register(registerDTO);

        return ResponseEntity.ok().build();
    }
}
