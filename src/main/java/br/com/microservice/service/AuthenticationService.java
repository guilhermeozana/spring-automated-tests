package br.com.microservice.service;

import br.com.microservice.domain.dto.AuthenticationDTO;
import br.com.microservice.domain.dto.LoginResponseDTO;
import br.com.microservice.domain.dto.RegisterDTO;
import br.com.microservice.domain.model.User;
import br.com.microservice.exception.UserAlreadyExistsException;
import br.com.microservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public LoginResponseDTO login(AuthenticationDTO authenticationDTO) {
        var usernamePassoword = new UsernamePasswordAuthenticationToken(authenticationDTO.login(), authenticationDTO.password());

        var auth = this.authenticationManager.authenticate(usernamePassoword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return new LoginResponseDTO(token);
    }

    public void register(RegisterDTO registerDTO) {
        if(this.userRepository.findByLogin(registerDTO.login()) != null) throw new UserAlreadyExistsException("User already exists in database.");

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
        User newUser = new User(registerDTO.login(), encryptedPassword, registerDTO.role());

        this.userRepository.save(newUser);
    }

}
