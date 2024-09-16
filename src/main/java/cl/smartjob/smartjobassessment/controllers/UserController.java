package cl.smartjob.smartjobassessment.controllers;

import cl.smartjob.smartjobassessment.model.dtos.requests.UserRequest;
import cl.smartjob.smartjobassessment.model.dtos.responses.UserResponse;
import cl.smartjob.smartjobassessment.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final AuthenticationService authenticationService;

    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> signUp(@RequestBody @Valid UserRequest userRequest) {
        return new ResponseEntity<>(authenticationService.signUp(userRequest), HttpStatus.CREATED);
    }
}
