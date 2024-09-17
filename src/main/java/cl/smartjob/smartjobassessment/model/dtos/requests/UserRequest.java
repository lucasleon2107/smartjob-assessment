package cl.smartjob.smartjobassessment.model.dtos.requests;

import cl.smartjob.smartjobassessment.annotations.ValidPassword;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UserRequest(
        @NotBlank(message = "El nombre no debe estar vacío")
        String name,
        @Email(message = "El email es inválido")
        String email,
        @NotBlank(message = "La contraseña no debe estar vacía")
        @ValidPassword(message = "La contraseña no tiene el formato correcto")
        String password,
        @Valid
        @NotEmpty(message = "Debes enviar al menos un teléfono")
        List<PhoneRequest> phones
) {
}
