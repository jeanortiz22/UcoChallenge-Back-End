package co.edu.uco.ucochallenge.infrastructure.primary.controller;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucochallenge.user.listusers.application.interactor.ListUsersInteractor;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.dto.ListUsersRequestDTO;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.dto.PagedUsersResponseDTO;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.dto.UserResponseDTO;

import co.edu.uco.ucochallenge.user.registeruser.application.interactor.RegisterUserInteractor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserResponseDTO;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    private final RegisterUserInteractor registerUserInteractor;
    private final ListUsersInteractor listUsersInteractor;

    public UserController(
            final RegisterUserInteractor registerUserInteractor,
            final ListUsersInteractor listUsersInteractor) {
        this.registerUserInteractor = registerUserInteractor;
        this.listUsersInteractor = listUsersInteractor;
    }
    
    @GetMapping
    public ResponseEntity<PagedUsersResponseDTO> getAllUsers(
            @RequestParam(name = "page", required = false) final Integer page,
            @RequestParam(name = "size", required = false) final Integer size) {

        final var request = ListUsersRequestDTO.normalize(page, size);
        final var users = listUsersInteractor.execute(request);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<RegisterUserResponseDTO> registerUser(@RequestBody final RegisterUserInputDTO dto) {
        final var normalizedDto = RegisterUserInputDTO.normalize(dto);
        final var response = registerUserInteractor.execute(normalizedDto);
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}
