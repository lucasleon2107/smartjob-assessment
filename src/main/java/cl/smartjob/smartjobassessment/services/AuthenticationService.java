package cl.smartjob.smartjobassessment.services;

import cl.smartjob.smartjobassessment.exceptions.UserAlreadyExistsException;
import cl.smartjob.smartjobassessment.model.dtos.requests.UserRequest;
import cl.smartjob.smartjobassessment.model.dtos.responses.UserResponse;
import cl.smartjob.smartjobassessment.model.entities.Phone;
import cl.smartjob.smartjobassessment.model.entities.User;
import cl.smartjob.smartjobassessment.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserResponse signUp(UserRequest userRequest) {
        validateExistingUser(userRequest);

        User user = createUserFromRequest(userRequest);

        User savedUser = userRepository.save(user);

        authenticateUser(userRequest);

        String token = jwtService.generateToken(savedUser);

        updateUserWithTokenAndLastLogin(savedUser, token);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getCreated(),
                savedUser.getModified(),
                savedUser.getLastLogin(),
                savedUser.getToken(),
                savedUser.isActive()
        );
    }

    private void updateUserWithTokenAndLastLogin(User savedUser, String token) {
        savedUser.setToken(token);
        savedUser.setLastLogin(LocalDateTime.now());
        userRepository.save(savedUser);
    }

    private void authenticateUser(UserRequest userRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.email(),
                        userRequest.password()
                )
        );
    }

    private User createUserFromRequest(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.name());
        user.setEmail(userRequest.email());
        user.setPassword(passwordEncoder.encode(userRequest.password()));

        userRequest.phones().forEach(phoneRequest -> {
            Phone phone = new Phone();
            phone.setNumber(phoneRequest.number());
            phone.setCountryCode(phoneRequest.countryCode());
            phone.setCityCode(phoneRequest.cityCode());
            user.addPhone(phone);
        });
        return user;
    }

    private void validateExistingUser(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.email()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + userRequest.email() + " already exists");
        }
    }
}
