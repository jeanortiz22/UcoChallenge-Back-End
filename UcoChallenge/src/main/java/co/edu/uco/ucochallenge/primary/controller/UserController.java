package co.edu.uco.ucochallenge.primary.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucochallenge.user.registeruser.application.interactor.RegisterUserInteractor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    private final RegisterUserInteractor registerUserInteractor;

    public UserController(final RegisterUserInteractor registerUserInteractor) {
        this.registerUserInteractor = registerUserInteractor;
    }
    
    @GetMapping
    public ResponseEntity<String> checkUsersEndpoint() {
        return ResponseEntity.ok("User endpoint is available");
    }

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody final RegisterUserInputDTO dto) {
        final var normalizedDto = RegisterUserInputDTO.normalize(
            dto.idType(),
            dto.idNumber(),
            dto.firstName(),
            dto.secondName(),
            dto.firstSurname(),
            dto.secondSurname(),
            dto.homeCity(),
            dto.email(),
            dto.mobileNumber());

        registerUserInteractor.execute(normalizedDto);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }
}
