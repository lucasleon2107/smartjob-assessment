package cl.smartjob.smartjobassessment.model.dtos.requests;

import cl.smartjob.smartjobassessment.annotations.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UserRequest(
        @NotBlank
        String name,
        @Email
        String email,
        @ValidPassword
        String password,
        List<PhoneRequest> phones
) {
}
