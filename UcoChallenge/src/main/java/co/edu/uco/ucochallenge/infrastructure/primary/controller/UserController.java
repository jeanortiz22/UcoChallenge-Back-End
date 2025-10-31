package co.edu.uco.ucochallenge.infrastructure.primary.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucochallenge.user.confirmation.application.interactor.SendConfirmationCodeInteractor;
import co.edu.uco.ucochallenge.user.confirmation.application.interactor.VerifyConfirmationCodeInteractor;
import co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto.ConfirmationTokenDTO;
import co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto.SendConfirmationCodeRequestDTO;
import co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto.UserConfirmationResponseDTO;
import co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto.VerifyConfirmationCodeRequestDTO;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.ListUsersInteractor;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.dto.ListUsersRequestDTO;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.dto.PagedUsersResponseDTO;

import co.edu.uco.ucochallenge.user.registeruser.application.interactor.RegisterUserInteractor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserResponseDTO;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    private final RegisterUserInteractor registerUserInteractor;
    private final ListUsersInteractor listUsersInteractor;
    private final SendConfirmationCodeInteractor sendConfirmationCodeInteractor;
    private final VerifyConfirmationCodeInteractor verifyConfirmationCodeInteractor;

    public UserController(
            final RegisterUserInteractor registerUserInteractor,
            final ListUsersInteractor listUsersInteractor,
            final SendConfirmationCodeInteractor sendConfirmationCodeInteractor,
            final VerifyConfirmationCodeInteractor verifyConfirmationCodeInteractor) {
        this.registerUserInteractor = registerUserInteractor;
        this.listUsersInteractor = listUsersInteractor;
        this.sendConfirmationCodeInteractor = sendConfirmationCodeInteractor;
        this.verifyConfirmationCodeInteractor = verifyConfirmationCodeInteractor;
    }
    
    @PreAuthorize("hasAuthority('read:users')")
    @GetMapping
    public ResponseEntity<PagedUsersResponseDTO> getAllUsers(
            @RequestParam(name = "page", required = false) final Integer page,
            @RequestParam(name = "size", required = false) final Integer size) {

        final var request = ListUsersRequestDTO.normalize(page, size);
        final var users = listUsersInteractor.execute(request);
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAuthority('create:users')")
    @PostMapping
    public ResponseEntity<RegisterUserResponseDTO> registerUser(
            @jakarta.validation.Valid @RequestBody final RegisterUserInputDTO dto) {
        final var normalizedDto = RegisterUserInputDTO.normalize(dto);
        final var response = registerUserInteractor.execute(normalizedDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    
    @PreAuthorize("hasAuthority('create:users')")
    @PostMapping("/{userId}/confirmaciones/{channel}")
    public ResponseEntity<UserConfirmationResponseDTO> sendConfirmation(
            @PathVariable("userId") final UUID userId,
            @PathVariable("channel") final String channel) {

        final var request = SendConfirmationCodeRequestDTO.create(userId, channel);
        final var response = sendConfirmationCodeInteractor.execute(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PreAuthorize("hasAuthority('create:users')")
    @PostMapping("/{userId}/confirmaciones/{channel}/verificacion")
    public ResponseEntity<UserConfirmationResponseDTO> verifyConfirmation(
            @PathVariable("userId") final UUID userId,
            @PathVariable("channel") final String channel,
            @RequestBody final ConfirmationTokenDTO tokenDto) {

        final var request = VerifyConfirmationCodeRequestDTO.create(userId, channel, tokenDto.sanitizedToken());
        final var response = verifyConfirmationCodeInteractor.execute(request);
        return ResponseEntity.ok(response);
    }
    
}
