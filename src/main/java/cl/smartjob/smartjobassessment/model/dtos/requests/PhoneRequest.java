package cl.smartjob.smartjobassessment.model.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record PhoneRequest(
        @NotBlank(message = "El número de teléfono no debe estar vacío")
        String number,
        @NotBlank(message = "El código de ciudad no debe estar vacío")
        String cityCode,
        @NotBlank(message = "El código de teléfono no debe estar vacío")
        String countryCode
) {
}
